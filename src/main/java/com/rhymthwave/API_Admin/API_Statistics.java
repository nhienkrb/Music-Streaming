package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/statistics")
@RequiredArgsConstructor
public class API_Statistics {

    private  final StatisticsService statisticsService;
    @GetMapping()
    public ResponseEntity<?> getStatisticsOverview() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.getStatisticsOverview()));
    }

    @GetMapping("/account")
    public ResponseEntity<?> getStatisticsAccountByYear() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.getAllAccountByYear()));
    }
    @GetMapping("/account-role")
    public ResponseEntity<?> getStatisticsAccountByRole() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.getAllAccountByRole()));
    }

    @GetMapping("/sumprice-subscription")
    public ResponseEntity<?> getTotalSumPriceSubscriptionUsingByCategory() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.totalSumPriceSubscriptionUsingByCategory()));
    }

    @GetMapping("/sumprice-year-subscription")
    public ResponseEntity<?> getTotalSumPriceSubscriptionByYear() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.totalSumPriceSubscriptionAllYear()));
    }

    // Lấy ra tỉ lệ gói nâng cấp (example: 43% Basic, 57% Premium)
    @GetMapping("/upgrade-package")
    public ResponseEntity<?> upgradePackageRate() {
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", statisticsService.upgradePackageRate()));
    }
}
