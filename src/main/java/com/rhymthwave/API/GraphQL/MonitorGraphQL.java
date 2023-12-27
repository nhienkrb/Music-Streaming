package com.rhymthwave.API.GraphQL;

import java.util.List;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.rhymthwave.Service.MonitorService;
import com.rhymthwave.entity.Monitor;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MonitorGraphQL {
	
	private final MonitorService monitorSer;
	
	@QueryMapping("getNewListened")
	public List<Monitor> getNewListened(@Argument("recordingId") Long recordingId, @Argument("duration") Integer duration){
		return monitorSer.getNewListener(recordingId, duration);
	}
}
