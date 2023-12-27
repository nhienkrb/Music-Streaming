package com.rhymthwave.API_GraphQL_Admin;

import com.rhymthwave.DAO.AdvertismentDAO;
import com.rhymthwave.DTO.CountStatusADS;
import com.rhymthwave.Service.AdvertisementService;
import com.rhymthwave.ServiceAdmin.IArtistService;
import com.rhymthwave.entity.Advertisement;
import com.rhymthwave.entity.Artist;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class API_GraphQL_Advertisement {

	private  final AdvertismentDAO advertismentDAO;
	@QueryMapping("findAllAdvertisementPendingAndRejectByStatus")
	public Object findAllAdvertisementPendingAndRejectByStatus() {
		List<CountStatusADS> list  =  advertismentDAO.findAllAdvertisementPendingAndRejectByStatus();
		return list;
	}


}
