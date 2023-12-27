package com.rhymthwave.DAO;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rhymthwave.entity.Role;
import com.rhymthwave.entity.TypeEnum.EROLE;

public interface RoleDAO extends JpaRepository<Role, Integer>{
	Role findByRole(EROLE role);
	
	@Query(value = "SELECT * FROM ROLE WHERE NAMEROLE NOT IN ('MANAGER', 'STAFF')", nativeQuery = true)
	List<Role> findAllByRoleNotIn();
}
