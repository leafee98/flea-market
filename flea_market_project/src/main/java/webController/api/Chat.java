package webController.api;

import lombok.extern.slf4j.Slf4j;
import model.bean.ChatMsg;
import model.bean.ChatSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.ChatService;
import webController.Utils;

import java.util.List;

@Slf4j
@RestController
public class Chat {

    private ChatService chatService;

    @Autowired
    public Chat(ChatService chatService) {
        this.chatService = chatService;
    }

    @RequestMapping(path = "/api/chat/startChatSession", method = RequestMethod.POST, produces = {"application/json"})
    String startChatSession(String token, String username) {
        Long sessionId = chatService.startChatSession(token, username);
        return Utils.format(sessionId != null, sessionId);
    }

    @RequestMapping(path = "/api/chat/getChatSessions", method = RequestMethod.POST, produces = {"application/json"})
    String getChatSessions(String token) {
        List<ChatSession> sessions = chatService.getChatSessions(token);
        return Utils.format(sessions != null, sessions);
    }

    @RequestMapping(path = "/api/chat/sendMsg", method = RequestMethod.POST, produces = {"application/json"})
    String sendMsg(String token, Long chatSessionId, String content) {
        Boolean res = chatService.sendMsg(token, chatSessionId, content);
        return Utils.format(res, null);
    }

    @RequestMapping(path = "/api/chat/getMsg", method = RequestMethod.POST, produces = {"application/json"})
    String getMsg(String token, Long chatSessionId) {
        List<ChatMsg> msgs = chatService.getMsg(token, chatSessionId);
        return Utils.format(msgs != null, msgs);
    }
}
