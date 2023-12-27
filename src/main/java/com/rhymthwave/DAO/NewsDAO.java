package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.News;
import com.rhymthwave.entity.TypeEnum.EROLE;

@Repository
public interface NewsDAO extends JpaRepository<News, Integer> {

	@Query(value = "select distinct year(CREATEDATE) as years from NEWS", nativeQuery = true)
	List<Integer> getAllYearInDB();

	@Query("SELECT n FROM News n " + "WHERE (YEAR(n.createDate) = :year OR :year IS NULL) "
			+ "AND (MONTH(n.createDate) = :month OR :month IS NULL) " + "ORDER BY n.createDate")
	List<News> findNewsByYearAndMonth(@Param("year") Integer year, @Param("month") Integer month);

	@Query("SELECT n FROM News n WHERE n.createFor = ?1 ORDER BY n.createDate desc")
	List<News> findAllNewsCreateFor(EROLE role);
}