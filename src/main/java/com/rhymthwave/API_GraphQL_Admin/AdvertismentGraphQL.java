package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.rhymthwave.ServiceAdmin.IAdvertismentServiceAdmin;
import com.rhymthwave.entity.Advertisement;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AdvertismentGraphQL {
	private final IAdvertismentServiceAdmin AdsSer;
	
	@QueryMapping("findAllAdsAudio")
	public List<Advertisement> findAllAdsAudio(){
		return AdsSer.listAdvertismentAudio();
	}
}
