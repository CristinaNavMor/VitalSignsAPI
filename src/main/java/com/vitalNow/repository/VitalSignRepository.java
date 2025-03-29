package com.vitalNow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vitalNow.model.VitalSign;

public interface VitalSignRepository extends JpaRepository<VitalSign, String> {

	Optional<VitalSign> findByName(String name);
}
