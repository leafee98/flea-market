package model.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserSelf {

    String username;

    String nickname;

    String avatar_url;

    Date banUntil;

    Date joinTime;

    Boolean admin;

    List<Social> socialList;
}
