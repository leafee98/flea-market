package model.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "t_social")
@Entity
public class SocialEntity {
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_user_id", nullable = false)
    UserEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_social_id")
    Long socialId;

    @Column(name = "c_social_type", nullable = false)
    String socialType;

    @Column(name = "c_social_url", nullable = false)
    String socialUrl;
}
