package com.rhymthwave.Service;

import java.util.List;

import com.rhymthwave.entity.Episode;
import com.rhymthwave.entity.Recording;
import com.rhymthwave.entity.UserType;
import com.rhymthwave.entity.Wishlist;

public interface WishlistService {
	Wishlist checkExtist(UserType usertype, Episode episode,Recording recording);
	
	Wishlist create(Wishlist wishlist, UserType usertype, Episode episode,Recording recording);
	
	List<Wishlist> myWishlist(UserType userType);
}
