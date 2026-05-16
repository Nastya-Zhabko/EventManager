package dev.nastyazhabko.eventnotificator.service;

import dev.nastyazhabko.eventnotificator.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationCounterService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationCounterService.class);
    private final StringRedisTemplate redisTemplate;
    private final NotificationRepository notificationRepository;

    public NotificationCounterService(StringRedisTemplate redisTemplate, NotificationRepository notificationRepository) {
        this.redisTemplate = redisTemplate;
        this.notificationRepository = notificationRepository;
    }

    public void incrementUnread(Integer userId, long delta) {
        try {
            redisTemplate.opsForValue()
                    .increment(
                            unreadKey(userId),
                            delta
                    );
        } catch (Exception e) {
            logger.error("Got exception while increment cache counter" + e.getMessage());
        }

    }

    public void syncUnreadFromDB(Integer userId) {
        long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(userId);

        try {
            redisTemplate.opsForValue()
                    .set(
                            unreadKey(userId),
                            Long.toString(unreadCount)
                    );
        } catch (Exception e) {
            logger.error("Got exception while set cache counter" + e.getMessage());
        }

    }

    private String unreadKey(Integer userId) {
        return "notif:unread:" + userId;
    }
}
