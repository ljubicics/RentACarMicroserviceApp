package com.example.rentacarnotificationservice.repository;

import com.example.rentacarnotificationservice.domain.NotificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationHistoryRepository extends JpaRepository<NotificationHistory, Long> {

    @Query(value = "select * from notification_history where notification_name = 'registration' and " +
            "datediff(day,cast( getdate() as Date ), arrival) <=2 and flag = 0", nativeQuery = true)
    List<NotificationHistory> findNotificationReservationHistory();

    @Query(value = "select * from notification_history where email = ?", nativeQuery = true)
    List<NotificationHistory> findNotificationMessageByEmail(String email);
    List<NotificationHistory> findNotificationHistoryByEmail(String email);
}