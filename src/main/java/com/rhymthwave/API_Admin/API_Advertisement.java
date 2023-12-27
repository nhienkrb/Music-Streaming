package com.rhymthwave.API_Admin;

import com.rhymthwave.DAO.AdvertismentDAO;
import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Request.DTO.AdvertisementDTO;
import com.rhymthwave.Service.AdvertisementService;
import com.rhymthwave.ServiceAdmin.IRole;
import com.rhymthwave.entity.Advertisement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/advertisement")
@RequiredArgsConstructor
public class API_Advertisement {

    private final AdvertisementService advertisementService;

    @PostMapping
    public ResponseEntity<?> createAdvertisement(@ModelAttribute AdvertisementDTO advertisementDTO, HttpServletRequest request) {
        Advertisement advertisement = advertisementService.save(advertisementDTO,request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully"));
    }


    @GetMapping("/running-completed")
    public ResponseEntity<?> getAllAdvertisementRunAndComplete() {

        List<Advertisement> list = advertisementService.getAllAdvertisementRunningAndCompleted();

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", list));
    }

    @GetMapping("/pending-reject")
    public ResponseEntity<?> getAllAdvertisementPendingAndReject() {

        List<Advertisement> list = advertisementService.getAllAdvertisementPendingAndReject();

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", list));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable("id") Integer advertisementID, @RequestParam("status") Integer status, HttpServletRequest request) {
        Advertisement advertisement = advertisementService.setStatus(advertisementID, status,request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", advertisement));
    }

}
