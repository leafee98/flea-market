package service.impl;

import lombok.extern.slf4j.Slf4j;
import model.bean.ChatMsg;
import model.bean.ChatSession;
import model.dao.entity.ChatMsgEntity;
import model.dao.entity.ChatSessionEntity;
import model.dao.entity.UserEntity;
import model.dao.repo.ChatMsgRepo;
import model.dao.repo.ChatRepo;
import model.dao.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.ChatService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    private SystemServiceImpl systemService;
    private UserRepo userRepo;
    private ChatRepo chatRepo;
    private ChatMsgRepo chatMsgRepo;

    @Autowired
    public ChatServiceImpl(SystemServiceImpl systemService, UserRepo userRepo,
                           ChatRepo chatRepo, ChatMsgRepo chatMsgRepo) {
        this.systemService = systemService;
        this.userRepo = userRepo;
        this.chatRepo = chatRepo;
        this.chatMsgRepo = chatMsgRepo;
    }

    @Override
    @Nullable
    @Transactional
    public Long startChatSession(String token, String username) {
        UserEntity user = systemService.token2User(token);
        UserEntity targetUser = systemService.findUserByUsername(username);
        if (user == null || targetUser == null) {
            log.warn("invalid token '{}' or not exist username '{}'", token, username);
            return null;
        } else {
            List<ChatSessionEntity> chatList = chatRepo.findByChatMember(user, targetUser);
            if (chatList.size() > 0) {
                Long chatId = chatList.get(0).getChatSessionId();
                log.info("use exist chat '{}' for user '{}' and '{}'", chatId, user.getUsername(), username);
                return chatId;
            } else {
                ChatSessionEntity chat = new ChatSessionEntity();

                user.getChatSessionList().add(chat);
                chat.getUserList().add(user);
                targetUser.getChatSessionList().add(chat);
                chat.getUserList().add(targetUser);

                chat = chatRepo.save(chat);
                userRepo.save(user);
                userRepo.save(targetUser);

                log.info("create new chat '{}' for user '{}' and '{}'",
                        chat.getChatSessionId(), user.getUsername(), username);
                return chat.getChatSessionId();
            }
        }
    }

    @Override
    @Nullable
    public List<ChatSession> getChatSessions(String token) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token: '{}'", token);
            return null;
        } else {
            List<ChatSessionEntity> chatList = chatRepo.findByChatMember(user);
            List<ChatSession> chatSessionList = new ArrayList<>(chatList.size());
            for (ChatSessionEntity chatSessionEntity : chatList) {
                chatSessionList.add(new ChatSession(chatSessionEntity));
            }
            log.info("user '{}' get '{}' chat session", user.getUsername(), chatSessionList.size());
            return chatSessionList;
        }
    }

    @Override
    @Transactional
    public Boolean sendMsg(String token, Long chatSessionId, String content) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token: '{}'", token);
            return null;
        } else {
            Optional<ChatSessionEntity> chatEntityOpt = chatRepo.findById(chatSessionId);
            if (chatEntityOpt.isPresent()) {
                ChatSessionEntity chat = chatEntityOpt.get();
                if (chat.getUserList().contains(user)) {
                    ChatMsgEntity chatMsg = new ChatMsgEntity();
                    chatMsg.setChatSession(chat);
                    chatMsg.setMessageTime(new Date());
                    chatMsg.setSender(user);
                    chatMsg.setContent(content);
                    chatMsg = chatMsgRepo.save(chatMsg);
                    log.info("user '{}' send message '{}' in chat session '{}'",
                            user.getUsername(), chatMsg.getContent(), chatSessionId);
                    return true;
                } else {
                    log.warn("user '{}' want to send message in a not related chat session '{}'",
                            user.getUsername(), chatSessionId);
                    return false;
                }
            } else {
                log.warn("user '{}' want to send message in not exist chat session '{}'",
                        user.getUsername(), chatSessionId);
                return false;
            }
        }
    }

    @Override
    @Nullable
    @Transactional
    public List<ChatMsg> getMsg(String token, Long chatSessionId) {
        UserEntity user = systemService.token2User(token);
        if (user == null) {
            log.warn("invalid or expired token: '{}'", token);
            return null;
        } else {
            Optional<ChatSessionEntity> chatEntityOpt = chatRepo.findById(chatSessionId);
            if (chatEntityOpt.isPresent()) {
                ChatSessionEntity chat = chatEntityOpt.get();
                if (chat.getUserList().contains(user)) {
                    List<ChatMsgEntity> chatMsgEntityList = chatMsgRepo.findByChatSessionEquals(chat);
                    List<ChatMsg> chatMsgList = new ArrayList<>(chatMsgEntityList.size());
                    for (ChatMsgEntity chatMsgEntity : chatMsgEntityList) {
                        chatMsgList.add(new ChatMsg(chatMsgEntity));
                    }
                    log.info("user '{}' get '{}' message(s) from chat session '{}'",
                            user.getUsername(), chatMsgList.size(), chatSessionId);
                    return chatMsgList;
                } else {
                    log.warn("user '{}' want to get message in a not related chat session '{}'",
                            user.getUsername(), chatSessionId);
                    return null;
                }
            } else {
                log.warn("user '{}' want to get message in not exist chat session '{}'",
                        user.getUsername(), chatSessionId);
                return null;
            }
        }
    }
}
