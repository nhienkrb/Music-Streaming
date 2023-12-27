package com.rhymthwave.API.GraphQL;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.AuthorService;
import com.rhymthwave.Service.FollowService;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.Follow;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FollowGraphQL {
	private final FollowService followSer;
	
	private final AuthorService authorSer;
		
	@QueryMapping("myListFollow")
	public List<Follow> findMyListFollow(@Argument("email") String email) {
		Author accountA = authorSer.findAuthor(1, email);
		return followSer.findMyListFollow(accountA);
	}
	
	@QueryMapping("findYourFollow")
	public List<Follow> findListFollower(@Argument("roleId") Integer roleId,@Argument("email") String email) {
		Author accountB = authorSer.findAuthor(roleId, email);
		if(accountB!=null) {
			return followSer.findYourListFollow(accountB);
		}
		return null;
	}
	
	@QueryMapping("findListArtistFanLiked")
	public List<Author> findListArtistFanLiked(@Argument("accountFan") List<Long> accountFan, @Argument("idRole") Integer idRole,@Argument("country") String country){
		return followSer.getListArtistFanLiked(accountFan, idRole, country);
	}
}
