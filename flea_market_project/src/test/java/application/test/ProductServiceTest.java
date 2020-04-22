package application.test;

import lombok.extern.slf4j.Slf4j;
import model.bean.ProductComment;
import model.bean.ProductDetail;
import model.bean.ProductPic;
import model.bean.ProductSummary;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.AdminService;
import service.ProductService;
import service.UserService;

import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ProductServiceTest {

    private ProductService productService;
    private UserService userService;
    private AdminService adminService;

    @Autowired
    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Test
    public void releaseAndCensorProduct() {
        assert userService.register("productRelease1", "productRelease1", "password");
        String token = userService.authorize("productRelease1", "password");
        assert token != null;
        String adminToken = userService.authorize("initAdmin", "initAdmin");
        assert adminToken != null;

        // release product and edit information
        Long productId = productService.newProduct(token);
        assert productId != null;
        assert productService.editProductName(token, productId, "product release 1");
        assert productService.editProductDetail(token, productId, "product release detail 1");
        assert productService.editExpectedPrice(token, productId, 233.3);
        Long picId = productService.editAddPic(token, productId, "pic://product_release_1");
        assert productService.editFinish(token, productId);

        // product information check
        ProductDetail product = productService.getProductDetail(productId);
        assert product != null;
        log.info("product detail : pic count: {}", product.getPics().size());
        for (ProductPic pic : product.getPics()) {
            log.info("product pic: id={}, url={}", pic.getProductPicId(), pic.getProductPicUrl());
        }
        assert product.getPics().size() == 1;
        assert product.getPics().get(0).getProductPicId().equals(picId);
        assert product.getPics().get(0).getProductPicUrl().equals("pic://product_release_1");
        assert product.getSeller() != null;
        assert product.getSeller().getUsername().equals("productRelease1");
        assert product.getBuyer() == null;

        // product should not be editable after finish
        assert !productService.editProductName(token, productId, "test edit protect");

        // product could not be visible before censored
        assert productService.getProductList().size() == 0;

        // product should be visible to the seller no matter what happen.
        List<ProductSummary> productList = productService.getMyProductList(token);
        assert productList != null;
        assert productList.size() == 1;

        // get censor list and disapprove product;
        List<ProductSummary> productSummaryList0 = adminService.getCensoringProducts(adminToken);
        assert productSummaryList0 != null;
        assert productSummaryList0.size() == 1;
        assert adminService.censorProduct(adminToken, productSummaryList0.get(0).getProductId(), false);
        assert productSummaryList0.get(0).getProductId().equals(productId);

        // censor list should be empty after disapproved product
        List <ProductSummary> productSummaryList1 = adminService.getCensoringProducts(adminToken);
        assert productSummaryList1 != null;
        assert productSummaryList1.size() == 0;

        // disapproved product should not appear in public list
        List<ProductSummary> productList1 = productService.getProductList();
        assert productList1.size() == 0;

        // re-edit product to censor again.
        assert productService.editProduct(token, productId);
        assert productService.editExpectedPrice(token, productId, 23.3);
        assert productService.editFinish(token, productId);

        // re-edited product should appear in censor list
        List<ProductSummary> productSummaryList2 = adminService.getCensoringProducts(adminToken);
        assert productSummaryList2 != null;
        assert productSummaryList2.size() == 1;
        assert adminService.censorProduct(adminToken, productSummaryList0.get(0).getProductId(), true);

        // censored product should appear in public product list
        List<ProductSummary> productList2 = productService.getProductList();
        assert productList2.size() == 1;

        assert productService.deleteProduct(token, productId);

        // no product should appear after delete
        List<ProductSummary> productList3 = productService.getProductList();
        assert productList3.size() == 0;
    }

    @Test
    public void testEditPic() {
        assert userService.register("productSeller", "productSeller", "password");
        assert userService.register("productBuyer", "productBuyer", "password");

        String sellerToken = userService.authorize("productSeller", "password");
        String buyerToken = userService.authorize("productBuyer", "password");
        String adminToken = userService.authorize("initAdmin", "initAdmin");

        assert sellerToken != null;
        assert buyerToken != null;
        assert adminToken != null;

        Long sellerProductId = productService.newProduct(sellerToken);
        assert productService.editExpectedPrice(sellerToken, sellerProductId, 2.33);
        assert productService.editProductName(sellerToken, sellerProductId, "the test order product");
        assert productService.editProductDetail(sellerToken, sellerProductId, "the test order product detail");
        productService.editAddPic(sellerToken, sellerProductId, "pic to keep");
        Long picId = productService.editAddPic(sellerToken, sellerProductId, "pic to drop");
        assert productService.editDeletePic(sellerToken, sellerProductId, picId);
        assert productService.editFinish(sellerToken, sellerProductId);

        adminService.censorProduct(adminToken, sellerProductId, true);

        List<ProductSummary> publicProductList = productService.getProductList();
        Long buyerProductId = publicProductList.get(0).getProductId();
        assert buyerProductId.equals(sellerProductId);

        assert productService.orderProduct(buyerToken, buyerProductId);

        List<ProductSummary> buyerProductList = productService.getBoughtProductList(buyerToken);
        assert buyerProductList != null;
        assert buyerProductList.size() == 1;
        assert buyerProductList.get(0).getSeller().getUsername().equals("productSeller");

        // order and confirm step by step

        ProductDetail productDetail1 = productService.getProductDetail(buyerProductId);
        assert productDetail1 != null;
        assert productDetail1.getProductStatus().equals(ProductService.PRODUCT_ORDERED);

        assert productService.confirmOrder(buyerToken, buyerProductId);

        ProductDetail productDetail2 = productService.getProductDetail(buyerProductId);
        assert productDetail2 != null;
        assert productDetail2.getProductStatus().equals(ProductService.PRODUCT_CONFIRM_BUYER);

        assert productService.confirmOrder(sellerToken, sellerProductId);

        ProductDetail productDetail3 = productService.getProductDetail(buyerProductId);
        assert productDetail3 != null;
        assert productDetail3.getProductStatus().equals(ProductService.PRODUCT_CLINCH);

        List<ProductSummary> sellerProductList = productService.getMyProductList(sellerToken);
        assert sellerProductList != null;
        assert sellerProductList.size() == 1;

        List<ProductSummary> publicProductListAfterClinch = productService.getProductList();
        for (ProductSummary p : publicProductListAfterClinch) {
            log.info(p.toString());
        }
        assert publicProductListAfterClinch.size() == 0;
    }

    @Test
    public void testCancelOrder() {
        userService.register("cancelOrderSeller", "cancelOrderSeller", "cancelOrderSeller");
        userService.register("cancelOrderBuyer", "cancelOrderBuyer", "cancelOrderBuyer");

        String sellerToken = userService.authorize("cancelOrderSeller", "cancelOrderSeller");
        String buyerToken = userService.authorize("cancelOrderBuyer", "cancelOrderBuyer");
        String adminToken = userService.authorize("initAdmin", "initAdmin");

        assert sellerToken != null;
        assert buyerToken != null;
        assert adminToken != null;

        Long productId = productService.newProduct(sellerToken);
        assert productId != null;
        productService.editProductName(sellerToken, productId, "to cancel product");
        productService.editProductDetail(sellerToken, productId, "to cancel product");
        productService.editFinish(sellerToken, productId);

        adminService.censorProduct(adminToken, productId, true);

        assert productService.orderProduct(buyerToken, productId);
        assert productService.cancelOrder(buyerToken, productId);
        ProductDetail product1 = productService.getProductDetail(productId);
        assert product1 != null;
        assert product1.getBuyer() == null;
        assert product1.getProductStatus().equals(ProductService.PRODUCT_SELLING);

        assert productService.orderProduct(buyerToken, productId);
        assert productService.confirmOrder(sellerToken, productId);
        assert productService.cancelOrder(buyerToken, productId);
        ProductDetail product2 = productService.getProductDetail(productId);
        assert product2 != null;
        assert product2.getBuyer() == null;
        assert product2.getProductStatus().equals(ProductService.PRODUCT_SELLING);

        assert productService.orderProduct(buyerToken, productId);
        assert productService.confirmOrder(buyerToken, productId);
        assert productService.confirmOrder(sellerToken, productId);
        ProductDetail product3 = productService.getProductDetail(productId);
        assert product3 != null;
        assert product3.getBuyer().getUsername().equals("cancelOrderBuyer");
        assert product3.getProductStatus().equals(ProductService.PRODUCT_CLINCH);
    }

    @Test
    public void testComment() {
        assert userService.register("productCommentSeller", "productCommentSeller",
                "productCommentSeller");
        assert userService.register("productCommentBuyer", "productCommentBuyer",
                "productCommentBuyer");

        String sellerToken = userService.authorize("productCommentSeller", "productCommentSeller");
        String buyerToken = userService.authorize("productCommentBuyer", "productCommentBuyer");
        String adminToken = userService.authorize("initAdmin", "initAdmin");

        assert sellerToken != null;
        assert buyerToken != null;
        assert adminToken != null;

        Long productId = productService.newProduct(sellerToken);
        assert productId != null;
        assert productService.editProductName(sellerToken, productId, "product to comment");
        assert productService.editProductDetail(sellerToken, productId, "product to comment, detail");
        assert productService.editFinish(sellerToken, productId);

        adminService.censorProduct(adminToken, productId, true);

        assert productService.comment(sellerToken, productId, "first");
        List<ProductComment> commentList1 = productService.getComments(productId);
        assert commentList1 != null;
        assert commentList1.size() == 1;

        assert productService.comment(buyerToken, productId, "second");
        List<ProductComment> commentList2 = productService.getComments(productId);
        assert commentList2 != null;
        assert commentList2.size() == 2;

        for (ProductComment c : commentList2) {
            log.info(c.toString());
        }

        productService.deleteProduct(sellerToken, productId);
    }
}
