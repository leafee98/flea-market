package service;

import org.springframework.lang.Nullable;

public interface UserService {
    // register a new user.
    // default join time will be now, ban_util will be 1970,
    // avatar_url will be 0 length string, admin is false
    Boolean register(String username, String nickname, String password);

    // return String of token to store in cookie if success, null if fail.
    @Nullable
    String authorize(String username, String password);

    // expire this token
    Boolean logout(String token);

    Boolean modifyPassword(String token, String password);

    Boolean modifyNickname(String token, String nickname);

    Boolean modifyAvatar(String token, String avatarUrl);

    @Nullable
    Long addSocial(String token, String socialType, String socialUrl);

    // remove social information, specify social info by type and url;
    Boolean removeSocial(String token, Long socialId);
}
