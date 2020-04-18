package service;

import model.bean.ProductSummary;
import org.springframework.lang.Nullable;

import java.util.List;

public interface AdminService {
    // User operate

    Boolean grantUser(String token, String username);

    Boolean banUser(String token, String username, Integer day);

    // Product operate

    @Nullable
    List<ProductSummary> getCensoringProducts(String token);

    Boolean CensorProduct(String token, Long productId, Boolean pass);

    // Bulletin operate

    Boolean publishBulletin(String token, String bulletinMsg);
}
