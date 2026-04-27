package com.internship.tool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.internship.tool.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
