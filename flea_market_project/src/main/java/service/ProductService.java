package service;

import model.bean.ProductComment;
import model.bean.ProductDetail;
import model.bean.ProductSummary;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ProductService {

    String PRODUCT_CENSORING = "censoring";
    String PRODUCT_EDITING = "editing";
    String PRODUCT_NOT_APPROVED = "not_approved";
    String PRODUCT_SELLING = "selling";
    String PRODUCT_ORDERED = "ordered";
    String PRODUCT_CONFIRM_BUYER = "confirm_buyer";
    String PRODUCT_CONFIRM_SELLER = "confirm_seller";
    String PRODUCT_CLINCH = "clinch";

    // sort by time, from new to old, only selling product
    List<ProductSummary> getProductList();

    @Nullable
    List<ProductSummary> getBoughtProductList(String token);

    @Nullable
    List<ProductSummary> getMyProductList(String token);


    @Nullable
    ProductDetail getProductDetail(Long productId);

    // create product, status is editing. return productId
    @Nullable
    Long newProduct(String token);

    // delete product status if status is selling,censoring or not_approved
    Boolean deleteProduct(String token, Long productId);

    // switch product status to editing from selling,censoring,not_approved
    Boolean editProduct(String token, Long productId);

    // switch product status to censoring
    Boolean editFinish(String token, Long productId);

    Boolean editExpectedPrice(String token, Long productId, Double price);

    Boolean editProductName(String token, Long productId, String productName);

    Boolean editProductDetail(String token, Long productId, String productDetail);

    // add picture to product, return picture id
    @Nullable
    Long editAddPic(String token, Long productId, String picUrl);

    // drop picture from product.
    Boolean editDeletePic(String token, Long productId, Long picId);

    // // check owner ship of product
    // Boolean modifyProduct(String token, ProductDetail product);

    Boolean orderProduct(String token, Long productId);

    Boolean cancelOrder(String token, Long productId);

    // verify owner and buyer in method
    Boolean confirmOrder(String token , Long productId);


    Boolean comment(String token, Long productId, String content);

    @Nullable
    List<ProductComment> getComments(Long productId);
}
