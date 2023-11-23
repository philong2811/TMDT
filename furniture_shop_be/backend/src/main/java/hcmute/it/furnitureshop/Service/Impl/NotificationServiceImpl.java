package hcmute.it.furnitureshop.Service.Impl;

import hcmute.it.furnitureshop.Entity.Notification;
import hcmute.it.furnitureshop.Entity.User;
import hcmute.it.furnitureshop.Repository.NotificationRepository;
import hcmute.it.furnitureshop.Service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public Iterable<Notification> findByUser(User user) {
        return notificationRepository.findNotificationsByUserOrderByDateDesc(user);
    }

    @Override
    public <S extends Notification> void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    @Override
    public Optional<Notification> findById(Integer notificationId) {
        return notificationRepository.findById(notificationId);
    }
}
