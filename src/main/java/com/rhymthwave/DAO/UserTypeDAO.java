package com.rhymthwave.DAO;

import com.rhymthwave.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTypeDAO extends JpaRepository<UserType, Long>{
	
	@Query("SELECT uty FROM UserType uty WHERE uty.account.email = ?1 AND uty.subscription.subscriptionType = 'MASTER' ")
	UserType findUserTypeEmail(String email);

	@Query("select ut.subscription.subscriptionId from UserType ut where year(ut.startDate) = :year")
	List<Integer> findIdSubscriptionByYearFromUserType( @Param(("year")) int year);

	@Query(value = "select COUNT(*) from userType where userType.startdate =  CONVERT(date, getdate())",nativeQuery = true)
	int countSubscriptionCurrent();

	List<UserType> findUserTypeByNameType(String nameType);
}
