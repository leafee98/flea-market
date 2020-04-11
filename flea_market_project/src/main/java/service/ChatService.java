package service;

import model.bean.ChatMsg;
import model.bean.ChatSession;

import java.util.List;

public interface ChatService {

    // create chat session if not exist.
    // or get the exist session
    Boolean newChatSession(String token, String username);

    List<ChatSession> getChatSessions(String token);

    // check if chat session owned by user.
    Boolean sendMsg(String token, Long chatSessionId, String Content);

    // check if chat session owned by user.
    List<ChatMsg> getMsg(String token, Long chatSessionId);

}
