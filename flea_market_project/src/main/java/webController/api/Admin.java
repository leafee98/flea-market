package webController.api;

import lombok.extern.slf4j.Slf4j;
import model.bean.ProductSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.AdminService;
import webController.Utils;

import java.util.List;

@Slf4j
@RestController
public class Admin {

    private AdminService adminService;

    @Autowired
    public Admin(AdminService adminService) {
        this.adminService = adminService;
    }

    // Boolean grantUser(String token, String username);

    @RequestMapping(path = "/api/admin/grantUser", method = RequestMethod.POST, produces = {"application/json"})
    String grantUser(String token, String username) {
        Boolean res = adminService.grantUser(token, username);
        return Utils.format(res, null);
    }

    // Boolean banUser(String token, String username, Integer day);
    @RequestMapping(path = "/api/admin/banUser", method = RequestMethod.POST, produces = {"application/json"})
    String banUser(String token, String username, Integer day) {
        Boolean res = adminService.banUser(token, username, day);
        return Utils.format(res, null);
    }

    // @Nullable
    // List<ProductSummary> getCensoringProducts(String token);

    // todo: test
    @RequestMapping(path = "/api/admin/getCensoringProducts", method = RequestMethod.POST, produces = {"application/json"})
    String getCensoringProducts(String token) {
        List<ProductSummary> products = adminService.getCensoringProducts(token);
        return Utils.format(products != null, products);
    }

    // Boolean censorProduct(String token, Long productId, Boolean pass);

    @RequestMapping(path = "/api/admin/censorProduct", method = RequestMethod.POST, produces = {"application/json"})
    String censorProduct(String token, Long productId, Boolean pass) {
        Boolean res = adminService.censorProduct(token, productId, pass);
        return Utils.format(res, null);
    }

    // Boolean publishBulletin(String token, String bulletinMsg);

    @RequestMapping(path = "/api/admin/publishBulletin", method = RequestMethod.POST, produces = {"application/json"})
    String publishBulletin(String token, String bulletinMsg) {
        Boolean res = adminService.publishBulletin(token, bulletinMsg);
        return Utils.format(res, null);
    }
}
