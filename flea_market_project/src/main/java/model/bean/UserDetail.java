package model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class UserDetail {

    String username;

    String nickname;

    String avatarUrl;

    Date banUntil;

    Date JoinTime;
}
