package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Follow;
import java.util.List;


@Repository
public interface FollowDAO extends JpaRepository<Follow, Long>{
	
	@Query("select o from Follow o where o.authorsAccountA = :accountA and o.authorsAccountB = :accountB")
	Follow findFollowByAccount(@Param("accountA") Author accountA, @Param("accountB") Author accountB);
	
	List<Follow> findByAuthorsAccountA(Author authorsAccountA);
	
	@Query("SELECT o FROM Follow o WHERE o.authorsAccountB.authorId = :authorId")
	List<Follow> findByAuthorsAccountB(@Param("authorId") Long authorId);

	@Query(value="select count(*) from follower where account_b = :authorId and followdate > GETDATE() - :days",nativeQuery = true)
	Integer getQuantityFollowByDate(@Param("authorId") Long authorId, @Param("days") Integer days);
	
	@Query(value="select distinct f.account_b from follower f "
			+ "join author a on f.account_a = a.authorid "
			+ "join author b on f.account_b = b.authorid "
			+ "join accounts acc on acc.email = b.email "
			+ "where f.account_a in :accountFans and f.account_b in (select authorid from author where idrole= :idRole) "
			+ "and acc.country = :country",nativeQuery = true)
	List<Long> getListFansLiked(@Param("accountFans") List<Long> accountFans,@Param("idRole") Integer idRole,@Param("country") String country);
	
	@Query(value="SELECT followdate, COUNT(*) AS soLuongFollow "
			+ "FROM follower "
			+ "WHERE account_b = :authId AND followdate BETWEEN DATEADD(DAY, -:date, GETDATE()) AND GETDATE() "
			+ "GROUP BY account_b, followdate "
			+ "ORDER BY followdate;",nativeQuery = true)
	List<Object[]> monitorFollower(@Param("authId") Long authId,@Param("date") Integer date);
}
