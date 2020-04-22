package application.test;

import lombok.extern.slf4j.Slf4j;
import model.bean.UserDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.AdminService;
import service.UserService;

import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AdminServiceTest {
    private UserService userService;
    private AdminService adminService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAdminService(AdminService adminService) {
        this.adminService = adminService;
    }

    @Test
    public void grantTest() {
        userService.register("user2grant", "user2grant", "password");
        String adminToken = userService.authorize("initAdmin", "initAdmin");
        assert adminToken != null;

        UserDetail userDetailOld = userService.getUserDetail("user2grant");
        assert userDetailOld != null;
        assert !userDetailOld.getAdmin();

        assert adminService.grantUser(adminToken, "user2grant");

        UserDetail userDetail = userService.getUserDetail("user2grant");
        assert userDetail != null;
        assert userDetail.getAdmin();
    }

    @Test
    public void banTest() {
        userService.register("user2ban", "user2ban", "password");
        String userToken = userService.authorize("user2ban", "password");
        String adminToken = userService.authorize("initAdmin", "initAdmin");
        assert userToken != null;
        assert adminToken != null;

        UserDetail userDetailOld = userService.getUserDetail("user2ban");
        assert userDetailOld != null;
        assert !userDetailOld.getBanUntil().after(new Date());

        assert adminService.banUser(adminToken, "user2ban", 3);

        UserDetail userDetail = userService.getUserDetail("user2ban");
        assert userDetail != null;
        assert userDetail.getBanUntil().after(new Date());

        assert userService.authorize("user2ban", "password") == null;
        assert !userService.modifyAvatar(userToken, "update avatar after banned");
    }

    // todo: test product part

}
