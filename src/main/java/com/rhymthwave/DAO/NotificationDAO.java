package com.rhymthwave.DAO;

import com.rhymthwave.entity.Mood;
import com.rhymthwave.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationDAO extends JpaRepository<Notification, Integer>{
	
	@Query(value="select top 1 * from notification where active = 1 order by createdate desc",nativeQuery = true)
	Notification findNotificationLatest();
}
