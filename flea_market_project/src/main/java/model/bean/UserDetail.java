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

    private List<Social> socialList;

    public UserDetail(UserEntity userEntity) {
        BeanUtils.copyProperties(userEntity, this);
        List<SocialEntity> sle = userEntity.getSocialList();
        List<Social> sl = new ArrayList<Social>(sle.size());
        for (int i = 0; i < sle.size(); ++i) {
            sl.set(i, new Social(sle.get(i)));
        }
        this.setSocialList(sl);
    }
}
