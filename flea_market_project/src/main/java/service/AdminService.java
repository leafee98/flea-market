package service;

import model.bean.ProductSummary;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AdminService {
    // User operate

    Boolean grantUser(String token, String username);

    Boolean banUser(String token, String username);

    // Product operate

    List<ProductSummary> getCensoringProducts(String token);

    Boolean CensorProduct(String token, Long productId);

    // Bulletin operate

    Boolean publishBulletin(String token, String bulletinMsg);
}
