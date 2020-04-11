package model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class UserSelf {

    private String username;

    private String nickname;

    private Boolean admin;

    private String avatar_url;

    private Date banUntil;

    private Date joinTime;
}
