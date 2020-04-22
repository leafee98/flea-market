package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.bean.UserDetail;
import model.dao.entity.SocialEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.SocialRepo;
import model.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.SystemService;
import service.UserService;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private SystemService systemService;
    private UserRepo userRepo;
    private SocialRepo socialRepo;

    @Autowired
    public UserServiceImpl(SystemService systemService, UserRepo userRepo, SocialRepo socialRepo) {
        this.systemService = systemService;
        this.userRepo = userRepo;
        this.socialRepo = socialRepo;
    }

    @Override
    public Boolean register(String username, String nickname, String password) {
        UserEntity user = new UserEntity();
        user.setBanUntil(new Date(0));
        user.setAdmin(false);
        user.setAvatarUrl("");
        user.setJoinTime(new Date());
        user.setNickname(nickname);
        user.setUsername(username);
        user.setPassword(password);

        user = userRepo.save(user);
        log.debug("new user: userId='{}' username='{}' nickname='{}'", user.getUserId(), username, nickname);
        return true;
    }

    @Nullable
    @Override
    public String authorize(String username, String password) {
        UserEntity user = systemService.findUserByUsername(username);
        if (user == null) {
            log.warn("no such user '{}'", username);
            return null;
        } else {
            if (user.getPassword().equals(password)) {
                if (user.getBanUntil().after(new Date())) {
                    log.warn("user '{}' has been banned until '{}'", username, user.getBanUntil());
                    return null;
                } else {
                    String token = systemService.authorizeToken(user.getUserId());
                    log.info("user '{}' log in.", username);
                    return token;
                }
            } else {
                log.warn("user '{}' identify failed.", username);
                return null;
            }
        }
    }

    @Override
    public Boolean logout(String token) {
        Boolean invoke = systemService.invokeToken(token);
        log.info("user who use token '{}' logout.", token);
        return invoke;
    }

    @Override
    public Boolean modifyPassword(String token, String password) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            user.setPassword(password);
            userRepo.save(user);
            log.info("user '{}' modified password.", user.getUsername());
            return true;
        }
    }

    @Override
    public Boolean modifyNickname(String token, String nickname) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            user.setNickname(nickname);
            userRepo.save(user);
            log.info("user '{}' modified nickname to '{}'.", user.getUsername(), nickname);
            return true;
        }
    }

    @Override
    public Boolean modifyAvatar(String token, String avatarUrl) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            user.setAvatarUrl(avatarUrl);
            userRepo.save(user);
            log.info("user '{}' modified avatarUrl to '{}'.", user.getUsername(), avatarUrl);
            return true;
        }
    }

    @Override
    @Nullable
    public Long addSocial(String token, String socialType, String socialUrl) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return null;
        } else {
            SocialEntity social = new SocialEntity();
            social.setSocialType(socialType);
            social.setSocialUrl(socialUrl);
            social.setUser(user);
            social = socialRepo.save(social);
            log.info("new social: socialId='{}' username='{}' socialType='{}' socialUrl='{}'",
                    social.getSocialId(), social.getUser().getUsername(),
                    social.getSocialType(), social.getSocialUrl());
            return social.getSocialId();
        }
    }

    @Override
    public Boolean removeSocial(String token, Long socialId) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return false;
        } else {
            Optional<SocialEntity> socialOpt = socialRepo.findById(socialId);
            if (socialOpt.isPresent()) {
                SocialEntity social = socialOpt.get();
                // if (social.getUser().equals(user)) {
                if (social.getUser().getUserId().equals(user.getUserId())) {
                    socialRepo.deleteById(socialId);
                    log.info("user '{}' delete social '{}'", user.getUsername(), socialId);
                    return true;
                } else {
                    log.warn("user '{}' delete social '{}' which not related to him(her)",
                            user.getUsername(), socialId);
                    return false;
                }
            } else {
                log.warn("user '{}' delete social '{}' which not existed",
                        user.getUsername(), socialId);
                return false;
            }
        }
    }

    @Override
    @Nullable
    @Transactional
    public UserDetail getMyDetail(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token '{}'", token);
            return null;
        } else {
            log.info("user '{}' get his(her) detail information", user.getUsername());
            return new UserDetail(user);
        }
    }

    @Override
    @Nullable
    @Transactional
    public UserDetail getUserDetail(String username) {
        UserEntity user = systemService.findUserByUsername(username);
        if (user == null) {
            log.warn("nonexistent username '{}'", username);
            return null;
        } else {
            log.info("user '{}' get his(her) detail information", user.getUsername());
            return new UserDetail(user);
        }
    }
}
