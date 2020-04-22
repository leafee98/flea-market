package model.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import model.dao.entity.ChatMsgEntity;

import java.util.Date;

@Data
@NoArgsConstructor
public class ChatMsg {

    private Long chatMsgId;

    private Long chatSessionId;

    private Date messageTime;

    private String content;

    private UserSummary sender;

    public ChatMsg(ChatMsgEntity chatMsg) {
        this.chatMsgId = chatMsg.getChatMsgId();
        this.chatSessionId = chatMsg.getChatSession().getChatSessionId();
        this.messageTime = chatMsg.getMessageTime();
        this.sender = new UserSummary(chatMsg.getSender());
        this.content = chatMsg.getContent();
    }
}
