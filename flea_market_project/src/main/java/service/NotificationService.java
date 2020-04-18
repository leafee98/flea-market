package service;

import model.bean.Notification;
import org.springframework.lang.Nullable;

import java.util.List;

public interface NotificationService {

    @Nullable
    List<Notification> getNotifications(String token);

    Boolean addNotification(String username, String notificationContent);

    Boolean readNotification(String token, Long notificationId);
}
