package service;

import model.bean.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getNotifications(String token);

    Boolean addNotification(String username, String notificationContent);

    Boolean readNotification(String token, Long notificationId);
}
