package hcmute.it.furnitureshop.Service;

import hcmute.it.furnitureshop.Entity.Notification;
import hcmute.it.furnitureshop.Entity.User;

import java.util.Optional;

public interface NotificationService {
    public Iterable<Notification> findByUser(User user);
    public <S extends Notification> void saveNotification(Notification notification);
    public Optional<Notification> findById(Integer notificationId);
}
