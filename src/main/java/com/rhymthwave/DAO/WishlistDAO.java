package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

@Repository
public interface WishlistDAO extends JpaRepository<Wishlist, Long>{

	@Query("SELECT COUNT(w.usertype.userTypeId) AS w FROM Wishlist w where w.usertype.account.email = ?1")
	int countWishlistByAccount(String idAccount);

	Wishlist findByUsertypeAndEpisodeAndRecording(UserType usertype, Episode episode, Recording recording);
	
	List<Wishlist> findAllByUsertype(UserType usertype);
}
