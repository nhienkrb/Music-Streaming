package com.rhymthwave.API_Admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/counter")
@RequiredArgsConstructor
public class API_CountVisitor {
    private Set<String> uniqueVisitors = new HashSet<>();
    public int incrementCounts(HttpSession session) {
        String sessionId = session.getId();
        uniqueVisitors.add(sessionId);
        return uniqueVisitors.size();
    }


    @GetMapping("/increment")
    public ResponseEntity<Integer> incrementVisitorCount(HttpSession session) {
        int count = incrementCounts( session);
        return ResponseEntity.ok(count);
    }



}
