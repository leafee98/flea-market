package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_notification")
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_notification_id")
    Long notificationId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_user_id", nullable = false)
    UserEntity user;

    // have read
    @Column(name = "c_read")
    Boolean read;

    @Column(name = "c_notification_time", nullable = false)
    Date notificationTime;

    @Column(name = "c_notification_content")
    String notificationContent;
}
