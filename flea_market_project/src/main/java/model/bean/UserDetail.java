package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.SocialEntity;
import model.dao.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDetail {

    String username;

    String nickname;

    String avatarUrl;

    Date banUntil;

    Date JoinTime;

    Boolean admin;

    private List<Social> socialList;

    public UserDetail(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity, this);

        List<SocialEntity> sle = userEntity.getSocialList();
        List<Social> sl = new ArrayList<>(sle.size());
        for (SocialEntity socialEntity : sle) {
            sl.add(new Social(socialEntity));
        }
        this.setSocialList(sl);
    }

}
