package com.rhymthwave.API_Admin;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rhymthwave.DTO.MessageResponse;
import com.rhymthwave.ServiceAdmin.IAccountServiceAdmin;
import com.rhymthwave.ServiceAdmin.IRole;
import com.rhymthwave.entity.Account;
import com.rhymthwave.entity.Author;
import com.rhymthwave.entity.TypeEnum.EROLE;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/admin/role")
@RequiredArgsConstructor
public class API_Role {
		
	private final IRole iRole;
	
	@GetMapping()
	public ResponseEntity<?> getAllRole() {

		return ResponseEntity.status(HttpStatus.OK).body(new MessageResponse(true, "Successfully", iRole.findAllByRoleNotIn()));
	}
	


}
