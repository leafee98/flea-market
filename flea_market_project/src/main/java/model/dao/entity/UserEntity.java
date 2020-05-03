package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "t_user")
public class UserEntity {
    @Column(name = "c_avatar_url")
    String avatarUrl;

    @Column(name = "c_ban_until")
    Date banUntil;

    @Column(name = "c_join_time", nullable = false)
    Date joinTime;

    @Column(name = "c_admin", nullable = false)
    Boolean admin;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_user_id")
    Long userId;

    @Column(name = "c_username", length = 32, nullable = false, unique = true)
    String username;

    @Column(name = "c_nickname", length = 64, nullable = false)
    String nickname;

    // todo: encrypt password
    @Column(name = "c_password")
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<SocialEntity> socialList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<NotificationEntity> notificationList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<BulletinMsgEntity> bulletinMsgList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<ProductCommentEntity> commentList;

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    List<ChatMsgEntity> chatMsgList;

    @ManyToMany
    @JoinTable(name = "t_relation_user_chat",
    joinColumns = @JoinColumn(name = "c_user_id"),
    inverseJoinColumns = @JoinColumn(name = "c_chat_id"))
    Set<ChatSessionEntity> chatSessionList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<TokenEntity> tokenList;
}
