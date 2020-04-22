package model.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "t_chat_session")
public class ChatSessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_chat_session_id")
    Long chatSessionId;

    @JsonIgnore
    @ManyToMany(mappedBy = "chatSessionList")
    Set<UserEntity> userList;

    public ChatSessionEntity() {
        this.userList = new HashSet<>();
    }
}
