package service;

import model.dao.entity.UserEntity;
import org.springframework.lang.Nullable;

public interface SystemService {

    @Nullable
    UserEntity token2User(String token);

    @Nullable
    String authorizeToken(Long userId);

    Boolean invokeToken(String token);

    @Nullable
    UserEntity findUserByUsername(String username);
}
