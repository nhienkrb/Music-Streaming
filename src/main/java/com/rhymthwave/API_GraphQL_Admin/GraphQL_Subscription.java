package com.rhymthwave.API_GraphQL_Admin;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.CRUD;
import com.rhymthwave.entity.Subscription;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GraphQL_Subscription {

	private final CRUD<Subscription, Integer> crud;

	@QueryMapping("totalSubscriptionsUsing")
	public List<Subscription> totalSubscriptionsUsing() {
		return crud.findAll();
	}
}
