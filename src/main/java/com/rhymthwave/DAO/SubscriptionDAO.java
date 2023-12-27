package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Subscription;
import com.rhymthwave.entity.TypeEnum.ESubscription;

@Repository
public interface SubscriptionDAO extends JpaRepository<Subscription, Integer>{
	Subscription findBySubscriptionType(String subscriptionType);

	@Query("select s from Subscription s where  s.subscriptionType = ?1")
	List<Subscription> findListBySubscriptionName(String subscriptionType);
	List<Subscription> findBySubscriptionCategoryAndActive(ESubscription category,Boolean active);
	@Query(value = "select * from subcriptions where MONTH(createdate) = ?1 and YEAR(createdate) = YEAR(GETDATE()) and active = 'true'",nativeQuery = true)
	List<Subscription> getByMonth( int month);

	@Query("select s from Subscription s where year(s.createDate) = ?1")
	List<Subscription> getBySubscriptionAllYear(int year);

	@Query("select distinct year(s.createDate) from Subscription s")
	List<Integer> getAllSubscriptionYear();
}
