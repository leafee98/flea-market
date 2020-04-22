package model.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "t_chat_msg")
public class ChatMsgEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_chat_msg_id")
    Long chatMsgId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_chat_id", nullable = false)
    ChatSessionEntity chatSession;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "c_sender_id", nullable = false)
    UserEntity sender;

    @Column(name = "c_chat_msg_time", nullable = false)
    Date messageTime;

    @Column(name = "c_chat_msg_content", nullable = false)
    String content;
}
