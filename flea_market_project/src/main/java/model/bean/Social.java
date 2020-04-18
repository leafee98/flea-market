package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.SocialEntity;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class Social {

    private Long socialId;

    private String socialType;

    private String socialUrl;

    public Social(SocialEntity socialEntity) {
        BeanUtils.copyProperties(socialEntity, this);
    }
}
