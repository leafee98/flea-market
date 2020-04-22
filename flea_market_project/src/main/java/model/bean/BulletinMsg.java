package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.BulletinMsgEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class BulletinMsg {

    Long bulletinId;

    UserSummary user;

    Date publishTime;

    String content;

    public BulletinMsg(BulletinMsgEntity msg) {
        BeanUtils.copyProperties(msg, this);
        this.user = new UserSummary(msg.getUser());
    }
}
