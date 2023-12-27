package com.rhymthwave.API_Admin;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.ITagsService;
import com.rhymthwave.Utilities.ExcelExportService;
import com.rhymthwave.entity.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/category/tag")
@RequiredArgsConstructor
public class API_Tags {

    private final ITagsService tagsService;

    private final ExcelExportService excelExportService;

    @GetMapping
    public ResponseEntity<?> getAllMood() {


        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", tagsService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findOneTag(@PathVariable("id") Long id) {

        Tag tag = tagsService.findOne(id);

        if (tag == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", tag));
    }

    @PostMapping()
    public ResponseEntity<?> createMood(@RequestBody Tag tagRequest, final HttpServletRequest request) {

        Tag tag = tagsService.save(tagRequest,request);
        if (tag == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Mood exists", tag));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse(true, "Successfully", tag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMood(@PathVariable("id") Integer id, @RequestBody Tag tagRequest, final HttpServletRequest request) {

        Tag tag = tagsService.save(tagRequest,request);
        if (tag == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", tag));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMood(@PathVariable("id") Long id) {

        boolean tag = tagsService.delete(id);

        if (!tag) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse(false, "Mood does exists", tag));
        }

        return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", tag));
    }


    @GetMapping("/export-excel")
    public ResponseEntity<?> exportToExcel(HttpServletResponse response) {
        try {
            List<Tag> tag = tagsService.findAll();
            excelExportService.exportToExcel(tag, response);
            return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Export excel successfully", ""));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(false, "Export excel Error", ""));
        }
    }

}
