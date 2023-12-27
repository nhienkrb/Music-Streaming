package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.Top10ArtistDTO;
import com.rhymthwave.ServiceAdmin.DashboardService;
import com.rhymthwave.ServiceAdmin.StatisticsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/dashboard")
@RequiredArgsConstructor
public class API_Dashboard {

    private final DashboardService dashboardService;
    private final StatisticsService statisticsService;

    @GetMapping("/count")
    public ResponseEntity<?> getCountSongAndEpisode() {

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.getStatisticsOverview()));
    }

    @GetMapping("/visitor")
    public ResponseEntity<?> getCountVisitor(HttpSession session) {

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.incrementCounts(session)));
    }

    @GetMapping("/top10-artist")
    public ResponseEntity<?> getTop10Artist() {

        List<Top10ArtistDTO> top10Artists = dashboardService.top10ArtistByListened();

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", top10Artists));
    }

    @GetMapping("/top10-podcast")
    public ResponseEntity<?> getTop10Podcast() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.top10PodcastByListened()));
    }

    @GetMapping("/top4-genre")
    public ResponseEntity<?> getTop4Genre() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.top4Genre()));
    }

    @GetMapping("/subscription")
    public ResponseEntity<?> countSubscriptionCurrent() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.countSubscriptionCurrent()));
    }

    @GetMapping("/top1-country")
    public ResponseEntity<?> getTop1Country() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.top1Country()));
    }

    @GetMapping("/count-account")
    public ResponseEntity<?> getCountAccount() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.countAllAccount()));
    }

    @GetMapping("/count-account/current")
    public ResponseEntity<?> getCountAccountCreatedCurren() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.countAccountCreatedCurren()));
    }
    
    @GetMapping("/count-account/verify")
    public ResponseEntity<?> getCountAccountIsNotVerified() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.getCountAccountIsNotVerified()));
    }

    @GetMapping("/create-byday")
    public ResponseEntity<?> getCountCreateRecordsAndEpisodeByDay(@RequestParam("date") String date) {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", dashboardService.numberCreateRecordsAndEpisodesByDay(date)));
    }
}
