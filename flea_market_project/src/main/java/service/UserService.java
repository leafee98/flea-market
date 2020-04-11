package service;

public interface UserService {
    // register a new user.
    // default join time will be now, ban_util will be null,
    // avatar_url will be null, admin is false
    Boolean register(String username, String nickname, String password);

    // return String of token to store in cookie if success, null if fail.
    String authorize(String username, String password);

    // expire this token
    Boolean logout(String token);

    Boolean modifyPassword(String token, String password);

    Boolean modifyNickname(String token, String nickname);

    Boolean modifyAvatar(String token, String avatarUrl);

    Boolean addSocial(String token, String socialType, String socialUrl);

    // remove social information, specify social info by type and url;
    Boolean removeSocial(String token, String socialType, String socialUrl);
}
