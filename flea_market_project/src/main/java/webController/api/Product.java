package webController.api;

import model.bean.ProductComment;
import model.bean.ProductDetail;
import model.bean.ProductSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.ProductService;
import webController.Utils;

import java.util.List;

@RestController
public class Product {

    private ProductService productService;

    @Autowired
    public Product(ProductService productService) {
        this.productService = productService;
    }

    // List<ProductSummary> getProductList();
    @RequestMapping(path = "/api/product/getProductList", produces = {"application/json"})
    String getProductList() {
        List<ProductSummary> productSummaryList = productService.getProductList();
        return Utils.format(true, productSummaryList);
    }

    // @Nullable
    // List<ProductSummary> getBoughtProductList(String token);
    @RequestMapping(path = "/api/product/getBoughtProductList", method = RequestMethod.POST, produces = {"application/json"})
    String getBoughtProductList(String token) {
        List<ProductSummary> productSummaryList = productService.getBoughtProductList(token);
        return Utils.format(productSummaryList != null, productSummaryList);
    }

    // @Nullable
    // List<ProductSummary> getMyProductList(String token);
    @RequestMapping(path = "/api/product/getMyProductList", method = RequestMethod.POST, produces = {"application/json"})
    String getMyProductList(String token) {
        List<ProductSummary> productSummaryList = productService.getMyProductList(token);
        return Utils.format(productSummaryList != null, productSummaryList);
    }

    // @Nullable
    // ProductDetail getProductDetail(Long productId);
    @RequestMapping(path = "/api/product/getProductDetail", produces = {"application/json"})
    String getProductDetail(Long productId) {
        ProductDetail detail = productService.getProductDetail(productId);
        return Utils.format(detail != null, detail);
    }

    // // create product, status is editing. return productId
    // @Nullable
    // Long newProduct(String token);
    @RequestMapping(path = "/api/product/newProduct", method = RequestMethod.POST, produces = {"application/json"})
    String newProduct(String token) {
        Long productId = productService.newProduct(token);
        return Utils.format(productId != null, productId);
    }

    // // delete product status if status is selling,censoring or not_approved
    // Boolean deleteProduct(String token, Long productId);
    @RequestMapping(path = "/api/product/deleteProduct", method = RequestMethod.POST, produces = {"application/json"})
    String deleteProduct(String token, Long productId) {
        Boolean res = productService.deleteProduct(token, productId);
        return Utils.format(res != null, null);
    }

    // // switch product status to editing from selling,censoring,not_approved
    // Boolean editProduct(String token, Long productId);
    @RequestMapping(path = "/api/product/editProduct", method = RequestMethod.POST, produces = {"application/json"})
    String editProduct(String token, Long productId) {
        Boolean res = productService.editProduct(token, productId);
        return Utils.format(res != null, null);
    }

    // // switch product status to censoring
    // Boolean editFinish(String token, Long productId);
    @RequestMapping(path = "/api/product/editFinish", method = RequestMethod.POST, produces = {"application/json"})
    String editFinish(String token, Long productId) {
        Boolean res = productService.editFinish(token, productId);
        return Utils.format(res != null, null);
    }

    // Boolean editExpectedPrice(String token, Long productId, Double price);
    @RequestMapping(path = "/api/product/editExpectedPrice", method = RequestMethod.POST, produces = {"application/json"})
    String editExpectedPrice(String token, Long productId, Double price) {
        Boolean res = productService.editExpectedPrice(token, productId, price);
        return Utils.format(res != null, null);
    }

    // Boolean editProductName(String token, Long productId, String productName);
    @RequestMapping(path = "/api/product/editProductName", method = RequestMethod.POST, produces = {"application/json"})
    String editProductName(String token, Long productId, String productName) {
        Boolean res = productService.editProductName(token, productId, productName);
        return Utils.format(res != null, null);
    }

    // Boolean editProductDetail(String token, Long productId, String productDetail);
    @RequestMapping(path = "/api/product/editProductDetail", method = RequestMethod.POST, produces = {"application/json"})
    String editProductDetail(String token, Long productId, String productDetail) {
        Boolean res = productService.editProductDetail(token, productId, productDetail);
        return Utils.format(res != null, null);
    }

    // // add picture to product, return picture id
    // @Nullable
    // Long editAddPic(String token, Long productId, String picUrl);
    @RequestMapping(path = "/api/product/editAddPic", method = RequestMethod.POST, produces = {"application/json"})
    String editAddPic(String token, Long productId, String picUrl) {
        Long picId = productService.editAddPic(token, productId, picUrl);
        return Utils.format(picId != null, picId);
    }

    // // drop picture from product.
    // Boolean editDeletePic(String token, Long productId, Long picId);
    @RequestMapping(path = "/api/product/editDeletePic", method = RequestMethod.POST, produces = {"application/json"})
    String editDeletePic(String token, Long productId, Long picId) {
        Boolean res = productService.editDeletePic(token, productId, picId);
        return Utils.format(res != null, null);
    }

    // Boolean orderProduct(String token, Long productId);
    @RequestMapping(path = "/api/product/orderProduct", method = RequestMethod.POST, produces = {"application/json"})
    String orderProduct(String token, Long productId) {
        Boolean res = productService.orderProduct(token, productId);
        return Utils.format(res != null, null);
    }

    // Boolean cancelOrder(String token, Long productId);
    @RequestMapping(path = "/api/product/cancelOrder", method = RequestMethod.POST, produces = {"application/json"})
    String cancelOrder(String token, Long productId) {
        Boolean res = productService.cancelOrder(token, productId);
        return Utils.format(res != null, null);
    }

    // // verify owner and buyer in method
    // Boolean confirmOrder(String token , Long productId);
    @RequestMapping(path = "/api/product/confirmOrder", method = RequestMethod.POST, produces = {"application/json"})
    String confirmOrder(String token , Long productId) {
        Boolean res = productService.confirmOrder(token, productId);
        return Utils.format(res != null, null);
    }



    // Boolean comment(String token, Long productId, String content);
    @RequestMapping(path = "/api/product/comment", method = RequestMethod.POST, produces = {"application/json"})
    String comment(String token, Long productId, String content) {
        Boolean res = productService.comment(token, productId, content);
        return Utils.format(res != null, null);
    }

    // @Nullable
    // List<ProductComment> getComments(Long productId);
    @RequestMapping(path = "/api/product/getComments", produces = {"application/json"})
    String getComments(Long productId) {
        List<ProductComment> commentList = productService.getComments(productId);
        return Utils.format(commentList != null, commentList);
    }
}
