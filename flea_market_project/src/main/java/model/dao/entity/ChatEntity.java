package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "t_chat")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_chat_id")
    Long chatId;

    @ManyToMany(mappedBy = "chatList")
    Set<UserEntity> userList;
}
