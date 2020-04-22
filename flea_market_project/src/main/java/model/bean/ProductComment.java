package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ProductCommentEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class ProductComment {

    private Long commentId;

    private String content;

    private Date commentTime;

    private UserSummary user;

    public ProductComment(ProductCommentEntity p) {
        BeanUtils.copyProperties(p, this);
        this.user = new UserSummary(p.getUser());
    }
}
