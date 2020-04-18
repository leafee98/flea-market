package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ChatEntity;
import model.dao.entity.ChatMsgEntity;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChatMsg {

    private Long chatMsgId;

    private Long chatSessionId;

    private Date chatMsgTime;

    public ChatMsg(ChatMsgEntity chatMsg) {
        this.chatMsgId = chatMsg.getChatMsgId();
        this.chatSessionId = chatMsg.getChat().getChatId();
        this.chatMsgTime = chatMsg.getMessageTime();
    }
}
