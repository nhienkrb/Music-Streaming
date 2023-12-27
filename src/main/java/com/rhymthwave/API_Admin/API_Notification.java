package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.Service.NotificationService;
import com.rhymthwave.entity.Notification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/notification")
@RequiredArgsConstructor
public class API_Notification {

    private final NotificationService notificationService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getAllById(@PathVariable("id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success",notificationService.findOne(id)));
    }

    @GetMapping()
    public ResponseEntity<?> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success",notificationService.findAll()));
    }

    @PostMapping()
    public ResponseEntity<?> createNotification(@ModelAttribute Notification notificationDTO, @RequestParam("file") MultipartFile file, HttpServletRequest request){
        Notification notification = notificationService.save(notificationDTO,file,request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "success",notification));
    }

    @PutMapping()
    public ResponseEntity<?> updateNotification(@RequestBody Notification notificationDTO, HttpServletRequest request){
      notificationService.update(notificationDTO,request);
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "success"));
    }

}
