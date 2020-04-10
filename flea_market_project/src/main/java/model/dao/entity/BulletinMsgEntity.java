package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_bulletin_msg")
public class BulletinMsgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_bulletin_msg_id")
    Long bulletinId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_user_id", nullable = false)
    UserEntity user;

    @Column(name = "c_publish_time", nullable = false)
    Date publishTime;

    @Lob
    @Column(name = "c_content", nullable = false)
    String content;
}
