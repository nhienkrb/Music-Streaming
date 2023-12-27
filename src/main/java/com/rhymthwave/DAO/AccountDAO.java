package com.rhymthwave.DAO;

import java.util.List;
import java.util.Map;

import com.rhymthwave.DTO.AnalysisDTO;
import com.rhymthwave.Request.DTO.Top10PodcastDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.TypeEnum.EROLE;
import jakarta.transaction.Transactional;

@Transactional
@Repository
public interface AccountDAO extends JpaRepository<Account, String>{

	@Query("SELECT au.account FROM Author au WHERE au.role.role = ?1")
	List<Account> findAllAccountRole( EROLE role);
	
	Account findByEmail(String email);

	Account findByVerificationCode(String verificationCode);	

	@Procedure(name = "SEARCH")
	List<Object> search(String keyword);

	
	@Query(value = "EXEC SP_SEARCH_GR :keyword",nativeQuery = true)
	List<Object> searchGr(String keyword);

	Account findByUsername(String username);

	@Query("SELECT NEW map(YEAR(u.createAt) AS year, COUNT(u) AS count) FROM Account u GROUP BY YEAR(u.createAt)")
	List<Map<Integer, Long>> countAccountsByYear();

	@Query(value = "SELECT TOP 1 COUNTRY,COUNT(*) AS NumberOfAccounts FROM  accounts\n" +
			"GROUP BY COUNTRY ORDER BY NumberOfAccounts DESC", nativeQuery = true)
	Object getTop1Country();

	@Query("select count(a) from Account a")
	int countAll();

	@Query(value = """
			SELECT top 10  a.email, i.url, p.authorname , SUM(e.listened) AS totalListened
       			       FROM episodes e
       			       LEFT JOIN podcast p on p.podcastid = e.podcastid
       			       JOIN accounts a on a.email = p.accountid
   					   LEFT JOIN images i on i.accessid = a.imageid
       			       WHERE e.isDeleted = 0 and e.ispublic = 1
       			 	   GROUP BY a.email, i.url,  p.authorname
       			       ORDER BY totalListened DESC
					""",nativeQuery = true)
	List<Top10PodcastDTO> getTopPodcast();

	@Query(value = "SELECT count(*) FROM accounts WHERE DAY(createat) = DAY(GETDATE())",nativeQuery = true)
	int countAccountCreatedToday();
	
	@Query(value = "select COUNT(*) from accounts where isverify = 'false'",nativeQuery = true)
	int countAccountIsNotVerified();

	@Query(value = "SELECT COUNTRY,COUNT(*) AS quantity FROM accounts GROUP BY COUNTRY",nativeQuery = true)
	List<AnalysisDTO> countAccountByCountry();
}
