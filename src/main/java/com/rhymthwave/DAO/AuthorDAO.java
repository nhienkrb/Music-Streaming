package com.rhymthwave.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Author;

@Repository
public interface AuthorDAO extends JpaRepository<Author, Long>{
	
	@Query("SELECT a FROM Author a WHERE a.role.roleId = :roleId AND a.account.email = :email")
	Author findAuthor(@Param("roleId") Integer roleId,@Param("email") String email);
}
