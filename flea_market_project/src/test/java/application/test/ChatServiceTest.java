package application.test;

import lombok.extern.slf4j.Slf4j;
import model.bean.ChatMsg;
import model.bean.ChatSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import service.ChatService;
import service.UserService;

import java.util.List;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatServiceTest {
    private UserService userService;
    private ChatService chatService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Test
    public void testChat() {
        userService.register("userChat1", "userChat1", "password");
        userService.register("userChat2", "userChat2", "password");
        String token1 = userService.authorize("userChat1", "password");
        String token2 = userService.authorize("userChat2", "password");

        Long chatSessionId = chatService.startChatSession(token1, "userChat2");
        assert chatService.sendMsg(token1, chatSessionId, "PING");

        List<ChatSession> chatSessionList2 = chatService.getChatSessions(token2);

        assert chatSessionList2 != null;
        assert chatSessionList2.size() == 1;

        assert chatService.sendMsg(token2, chatSessionList2.get(0).getChatSessionId(), "PONG");

        List<ChatSession> chatSessionList1 = chatService.getChatSessions(token1);

        assert chatSessionList1 != null;
        assert chatSessionList1.size() == 1;

        List<ChatMsg> chatMsgList1 = chatService.getMsg(token1, chatSessionList1.get(0).getChatSessionId());
        assert chatMsgList1 != null;
        assert chatMsgList1.size() == 2;

        assert chatMsgList1.get(0).getContent().equals("PING");
        assert chatMsgList1.get(1).getContent().equals("PONG");

        List<ChatMsg> chatMsgList2 = chatService.getMsg(token2, chatSessionList2.get(0).getChatSessionId());
        assert chatMsgList2 != null;
        assert chatMsgList2.size() == 2;

        assert chatMsgList2.get(0).getContent().equals("PING");
        assert chatMsgList2.get(1).getContent().equals("PONG");

        ChatMsg msg10 = chatMsgList1.get(0);
        ChatMsg msg11 = chatMsgList1.get(1);

        ChatMsg msg20 = chatMsgList2.get(0);
        ChatMsg msg21 = chatMsgList2.get(1);

        assert msg10.equals(msg20);
        assert msg11.equals(msg21);

        assert msg10.getSender().equals(msg20.getSender());
        assert msg11.getSender().equals(msg21.getSender());
    }
}
