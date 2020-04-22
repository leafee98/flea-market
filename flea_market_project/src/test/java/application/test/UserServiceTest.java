package application.test;

import lombok.extern.slf4j.Slf4j;
import model.bean.UserDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.UserService;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testRegisterAndLogin() {
        String username = "testUser";
        String password = "testPassword";
        String nickname = "testNickname";

        String newNickname = "newNickname";
        String newPassword = "newPassword";


        assert userService.register(username, nickname, password);
        assert userService.authorize(username, password + "1234") == null;

        String token = userService.authorize(username, password);
        log.info("token get: {}", token);
        assert token != null;

        UserDetail userDetailInit = userService.getMyDetail(token);
        assert userDetailInit != null;
        assert userDetailInit.getNickname().equals(nickname);
        assert userDetailInit.getUsername().equals(username);

        assert userService.modifyNickname(token, newNickname);
        assert userService.modifyPassword(token, newPassword);

        UserDetail userDetailNew = userService.getMyDetail(token);
        assert userDetailNew != null;
        assert userDetailNew.getNickname().equals(newNickname);

        assert userService.logout(token);
        assert !userService.modifyNickname(token, "anotherNickname");

        String newToken = userService.authorize(username, newPassword);
        log.info("new token get: {}", newToken);

        Long socialId = userService.addSocial(newToken, "testSocialType", "social://testUrl");

        UserDetail myDetail = userService.getMyDetail(newToken);
        UserDetail userDetail = userService.getUserDetail(username);
        assert myDetail != null;
        assert userDetail != null;
        assert myDetail.equals(userDetail);

        assert myDetail.getSocialList().size() == 1;
        assert myDetail.getSocialList().get(0).getSocialType().equals("testSocialType");
        assert myDetail.getSocialList().get(0).getSocialUrl().equals("social://testUrl");

        assert socialId != null;
        assert userService.removeSocial(newToken, socialId);
        assert userService.modifyAvatar(newToken, "new avatarUrl");

        UserDetail myDetailRemoveSocial = userService.getMyDetail(newToken);
        assert myDetailRemoveSocial != null;
        assert myDetailRemoveSocial.getSocialList().size() == 0;
        assert myDetailRemoveSocial.getAvatarUrl().equals("new avatarUrl");
    }

}
