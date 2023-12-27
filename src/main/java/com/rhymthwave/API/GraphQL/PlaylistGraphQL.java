package com.rhymthwave.API.GraphQL;

import java.util.List;
import java.util.Optional;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.CRUD;
import com.rhymthwave.Service.PlaylistService;
import com.rhymthwave.Service.UserTypeService;
import com.rhymthwave.Service.WishlistService;
import com.rhymthwave.Utilities.GetHostByRequest;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Playlist;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PlaylistGraphQL {
	private final CRUD<Playlist, Long> crudPlaylist;
	
	private final CRUD<Account, String> crudAccount;
	
	private final WishlistService wishlistSer;
	
	private final PlaylistService playlistSer;
	
	private final CRUD<UserType,Long> crudUserType;
		
	@QueryMapping("playlistById")
	public Playlist findPlaylistById(@Argument("playlistId") Long id) {
		return crudPlaylist.findOne(id);
	}
	
	@QueryMapping("myWishlist")
	public List<Wishlist> findMyWishlist(@Argument("email") String email){
		Account account = crudAccount.findOne(email);
		UserType basic = account.getUserType().get(0);
		List<Wishlist> list = wishlistSer.myWishlist(basic);
		if(account.getUserType().toArray().length > 2) {
			UserType premium = account.getUserType().get(1);
			list.addAll( wishlistSer.myWishlist(premium));
		}		
		if(!list.isEmpty()) {
			return list;
		}
		return null;
	}
	
	@QueryMapping("findPublicPlaylist")
	public List<Playlist> findPublicPlaylist(@Argument("userTypeId") Long userTypeId,@Argument("isPublic") Boolean isPublic){
		return playlistSer.findPublicPlaylist(crudUserType.findOne(userTypeId), isPublic);
	}
	
	@QueryMapping("findPlaylistFeaturingByArtist")
	public List<Playlist> findPlaylistFeaturingByArtist(@Argument("artistId") Long artistId,@Argument("roleId") List<Integer> roleId){
		return playlistSer.findPlaylistFeaturingArtist(artistId,roleId);
	}
	
	@QueryMapping("findPlaylistDiscoverByArtist")
	public List<Playlist> findPlaylistDiscoverByArtist(@Argument("artistId") Long artistId,@Argument("roleId") List<Integer> roleId){
		return playlistSer.findDiscoverArtist(artistId, roleId);
	}
	
	@QueryMapping("findTopNewPlaylist")
	public List<Playlist> findTopNewPlaylist(){
		return playlistSer.top50PlaylistLatest(true);
	}
	
	@QueryMapping("findTopRecentPlaylist")
	public List<Playlist> top50PlaylistRecentListen(@Argument("genre") Optional<List<String>> genre,
			@Argument("culture") Optional<String> culture, @Argument("instrument") Optional<String> instrument,
			@Argument("mood") Optional<String> mood, @Argument("songstyle") Optional<String> songstyle, 
			@Argument("versions") Optional<String> versions){
		return playlistSer.top50PlaylistRecentListen(true, genre, culture, instrument, mood, songstyle, versions);
	}
}
