package service;

import model.bean.ChatMsg;
import model.bean.ChatSession;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ChatService {

    // create chat session if not exist.
    // or get the exist session
    Long startChatSession(String token, String username);

    @Nullable
    List<ChatSession> getChatSessions(String token);

    // check if chat session owned by user.
    Boolean sendMsg(String token, Long chatSessionId, String Content);

    // check if chat session owned by user.
    @Nullable
    List<ChatMsg> getMsg(String token, Long chatSessionId);

}
