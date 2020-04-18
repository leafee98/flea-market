package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ChatEntity;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class ChatSession {

    private Long chatId;

    public ChatSession(ChatEntity chat) {
        BeanUtils.copyProperties(chat, this);
    }
}
