package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_login_token")
public class TokenEntity {
    @Id
    @Column(name = "c_token_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long tokenId;

    @Column(name = "c_token", length = 36, unique = true, nullable = false)
    String token;

    Date expireTime;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_user_id")
    UserEntity user;
}
