package service;

import model.dao.entity.UserEntity;

public interface SystemService {

    UserEntity token2User(String token);

    String authorizeToken(Long userId);

    Boolean invokeToken(String token);
}
