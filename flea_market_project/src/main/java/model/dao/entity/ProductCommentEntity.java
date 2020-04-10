package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_comment")
public class ProductCommentEntity {
    @Id
    @Column(name = "c_comment_id")
    Long commentId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_reply_to", nullable = true)
    ProductCommentEntity comment;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_product_id", nullable = false)
    ProductEntity product;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_user_id", nullable = false)
    UserEntity user;

    @Column(name = "c_content", nullable = false)
    String content;

    @Column(name = "c_comment_time", nullable = false)
    Date commentTime;
}
