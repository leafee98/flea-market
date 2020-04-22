package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ChatSessionEntity;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class ChatSession {

    private Long chatSessionId;

    public ChatSession(ChatSessionEntity chat) {
        BeanUtils.copyProperties(chat, this);
    }
}
