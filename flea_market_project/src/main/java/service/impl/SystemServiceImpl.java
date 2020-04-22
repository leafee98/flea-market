package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.dao.entity.TokenEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.TokenRepo;
import model.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import service.SystemService;

import java.util.*;

@Slf4j
@Service
public class SystemServiceImpl implements SystemService {
    private TokenRepo tokenRepo;
    private UserRepo userRepo;

    @Autowired
    public SystemServiceImpl(TokenRepo tokenRepo, UserRepo userRepo) {
        this.tokenRepo = tokenRepo;
        this.userRepo = userRepo;
    }

    @Nullable
    @Override
    public UserEntity token2User(String token) {
        List<TokenEntity> tokenList =  tokenRepo.findByTokenEquals(token);
        if (! tokenList.isEmpty()) {
            if (tokenList.get(0).getExpireTime().after(new Date())) {
                UserEntity user = tokenList.get(0).getUser();
                if (user.getBanUntil().after(new Date())) {
                    log.warn("user '{}' has been banned until '{}'", user.getUsername(), user.getBanUntil());
                    return null;
                } else {
                    log.info("token2User: get user '{}' from token '{}'", user.getUsername(), token);
                    return tokenList.get(0).getUser();
                }
            } else {
                log.warn("token2User: such token is expired: '{}'", token);
                return null;
            }
        } else {
            log.warn("token2User: no such token: '{}'", token);
            return null;
        }
    }

    // create token for user
    @Nullable
    @Override
    public String authorizeToken(Long userId) {
        Optional<UserEntity> user = userRepo.findById(userId);
        if (user.isPresent()) {
            TokenEntity token = new TokenEntity();
            token.setToken(UUID.randomUUID().toString());

            token.setUser(user.get());

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 7);
            token.setExpireTime(calendar.getTime());

            token = tokenRepo.save(token);

            log.debug("authorized token: id='{}' username='{}' expire_time='{}' token='{}'",
                    token.getTokenId(), token.getUser().getUsername(), token.getExpireTime(), token.getToken());
            log.info("authorized token for user '{}'", user.get().getUsername());

            return token.getToken();
        } else {
            log.error("authorized token failed: userId not found: '{}'", userId);
            return null;
        }
    }

    @Override
    public Boolean invokeToken(String token) {
        List<TokenEntity> tokenList = tokenRepo.findByTokenEquals(token);
        if (! tokenList.isEmpty()) {
            TokenEntity tokenEntity = tokenList.get(0);
            tokenRepo.delete(tokenEntity);

            log.debug("invoke token: tokenId='{}' token='{}' expireTime='{}'",
                    tokenEntity.getTokenId(), tokenEntity.getToken(), tokenEntity.getExpireTime());

            return true;
        } else {
            log.warn("invoke token failed: token not found: '{}'", token);
            return false;
        }
    }

    // used for be the object to be operated.
    @Nullable
    @Override
    public UserEntity findUserByUsername(String username) {
        List<UserEntity> userList = userRepo.findByUsernameEquals(username);
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }
    }
}
