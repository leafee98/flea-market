package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class UserSummary {

    String nickname;

    String avatarUrl;

    public UserSummary(UserEntity user) {
        BeanUtils.copyProperties(user, this);
    }
}
