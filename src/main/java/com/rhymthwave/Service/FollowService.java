package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Follow;

public interface FollowService {
	Follow snapFollow(Follow follow,Author accountA, Author accountB);
	
	Follow findFollowByAccount(Author accountA, Author accountB);
	
	List<Follow> findMyListFollow(Author accountA);
		
	List<Follow> findYourListFollow(Author accountB);
	
	Integer getQuantityFollowByDate(Long authorId,Integer days);
	
	List<Author> getListArtistFanLiked(List<Long> accountFans,Integer idRole, String country);
	
	List<Object[]> monitorFollower(String email,Integer role, Integer date);
}
