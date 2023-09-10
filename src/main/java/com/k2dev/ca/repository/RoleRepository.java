package com.k2dev.ca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.k2dev.ca.model.Role;

public interface RoleRepository extends JpaRepository<Role,Integer> {
	@Query("select r from Role r where r.rolename=:rolename")
	Role findByRolename(@Param("rolename") String rolename);
}
