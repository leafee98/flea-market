package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.dao.entity.SocialEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.SocialRepo;
import model.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import service.SystemService;
import service.UserService;

import java.util.Date;

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
        log.debug("new user: userId={} username={} nickname={}", user.getUserId(), username, nickname);
        return true;
    }

    @Nullable
    @Override
    public String authorize(String username, String password) {
        UserEntity user = systemService.findUserByUsername(username);
        if (user == null) {
            log.warn("no such user {}", username);
            return null;
        } else {
            if (user.getPassword().equals(password)) {
                String token = systemService.authorizeToken(user.getUserId());
                log.info("user {} log in.", username);
                return token;
            } else {
                log.warn("user {} identify failed.", username);
                return null;
            }
        }
    }

    @Override
    public Boolean logout(String token) {
        Boolean invoke = systemService.invokeToken(token);
        log.info("user who use token {} logout.", token);
        return invoke;
    }

    @Override
    public Boolean modifyPassword(String token, String password) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token {}", token);
            return false;
        } else {
            user.setPassword(password);
            userRepo.save(user);
            log.info("user {} modified password.", user.getUsername());
            return true;
        }
    }

    @Override
    public Boolean modifyNickname(String token, String nickname) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token {}", token);
            return false;
        } else {
            user.setNickname(nickname);
            userRepo.save(user);
            log.info("user {} modified nickname to {}.", user.getUsername(), nickname);
            return true;
        }
    }

    @Override
    public Boolean modifyAvatar(String token, String avatarUrl) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token {}", token);
            return false;
        } else {
            user.setAvatarUrl(avatarUrl);
            userRepo.save(user);
            log.info("user {} modified avatarUrl to {}.", user.getUsername(), avatarUrl);
            return true;
        }
    }

    @Override
    public Long addSocial(String token, String socialType, String socialUrl) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token {}", token);
            return null;
        } else {
            SocialEntity social = new SocialEntity();
            social.setSocialType(socialType);
            social.setSocialUrl(socialUrl);
            social.setUser(user);
            social = socialRepo.save(social);
            log.debug("new social: socialId={} username={} socialType={} socialUrl={}",
                    social.getSocialId(), social.getUser().getUsername(),
                    social.getSocialType(), social.getSocialUrl());
            return social.getSocialId();
        }
    }

    @Override
    public Boolean removeSocial(String token, Long socialId) {
        socialRepo.deleteById(socialId);
        return true;
    }
}
