package webController.api;

import lombok.extern.slf4j.Slf4j;
import model.bean.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;
import webController.Utils;

@Slf4j
@RestController
public class User {
    private UserService userService;

    @Autowired
    public User(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/api/user/register", method = RequestMethod.POST, produces = {"application/json"})
    String register(String username, String nickname, String password) {
        Boolean res = userService.register(username, nickname, password);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/authorize", method = RequestMethod.POST, produces = {"application/json"})
    String authorize(@RequestParam(name = "username") String username,
                     @RequestParam(name = "password") String password
    ) {
        String token = userService.authorize(username, password);
        return Utils.format(token != null, token);
    }

    @RequestMapping(value = "/api/user/logout", method = RequestMethod.POST, produces = {"application/json"})
    String logout(String token) {
        Boolean res = userService.logout(token);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/modifyPassword", method = RequestMethod.POST, produces = {"application/json"})
    String modifyPassword(String token, String password) {
        Boolean res = userService.modifyPassword(token, password);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/modifyNickname", method = RequestMethod.POST, produces = {"application/json"})
    String modifyNickname(String token, String nickname) {
        Boolean res = userService.modifyNickname(token, nickname);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/modifyAvatar", method = RequestMethod.POST, produces = {"application/json"})
    String modifyAvatar(String token, String avatarUrl) {
        Boolean res = userService.modifyAvatar(token, avatarUrl);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/addSocial", method = RequestMethod.POST, produces = {"application/json"})
    String addSocial(String token, String socialType, String socialUrl) {
        Long id = userService.addSocial(token, socialType, socialUrl);
        return Utils.format(id != null, String.valueOf(id));
    }

    @RequestMapping(value = "/api/user/removeSocial", method = RequestMethod.POST, produces = {"application/json"})
    String removeSocial(String token, Long socialId) {
        log.info("debug: token='{}'", token);
        Boolean res = userService.removeSocial(token, socialId);
        return Utils.format(res, null);
    }

    @RequestMapping(value = "/api/user/getMyDetail", method = RequestMethod.POST, produces = {"application/json"})
    String getMyDetail(String token) {
        UserDetail detail = userService.getMyDetail(token);

        return Utils.format(detail != null, detail);
    }

    @RequestMapping(value = "/api/user/getUserDetail", produces = {"application/json"})
    String getUserDetail(String username) {
        UserDetail detail = userService.getUserDetail(username);

        return Utils.format(detail != null, detail);
    }
}
