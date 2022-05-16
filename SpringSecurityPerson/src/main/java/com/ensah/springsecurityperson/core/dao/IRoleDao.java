package com.ensah.springsecurityperson.core.dao;

import com.ensah.springsecurityperson.core.bo.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleDao extends JpaRepository<Role, Long> {
}
