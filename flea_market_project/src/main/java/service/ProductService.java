package service;

import model.bean.ProductComment;
import model.bean.ProductDetail;
import model.bean.ProductSummary;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;

public interface ProductService {

    String PRODUCT_CENSORING = "censoring";
    String PRODUCT_NOT_APPROVED = "not_approved";
    String PRODUCT_SELLING = "selling";
    String PRODUCT_ORDERED = "ordered";
    String PRODUCT_CONFIRM_BUYER = "confirm_buyer";
    String PRODUCT_CONFIRM_SELLER = "confirm_seller";
    String PRODUCT_CLINCH = "clinch";

    // sort by time, from new to old, only selling product.
    List<ProductSummary> getProductList(Long rangeBegin);

    List<ProductSummary> getBoughtProductList(String token);

    List<ProductSummary> getSellingProductList(String token);


    ProductDetail getProductDetail(Long productId);


    Long releaseProduct(String token, ProductDetail product);

    Long orderProduct(String token, Long productId);

    // verify owner and buyer in method
    Boolean confirmOrder(String token , Long productId);


    Boolean comment(String token, Long productId, String content,
                    @Nullable Long replyTo);

    // 30 pre request
    List<ProductComment> getCommentsNoReply(Long ProductId, Long rageBegin);

    List<ProductComment> getCommentsReplayTo(Long CommentId);
}
