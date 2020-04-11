package model.bean;

import lombok.Data;

import java.util.Date;

@Data
public class ChatMsg {

    private Long chatMsgId;

    private Long chatSessionId;

    private Date chatMsgTime;

}
