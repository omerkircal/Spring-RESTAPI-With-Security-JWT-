package com.works.repositories;

import com.works.entities.Admin;
import com.works.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmailEqualsIgnoreCase(String email);

    Optional<Admin> findByVerificationCodeEquals(String verificationCode);


}