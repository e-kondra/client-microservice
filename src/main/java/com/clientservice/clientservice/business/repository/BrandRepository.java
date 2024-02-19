package com.clientservice.clientservice.business.repository;

import com.clientservice.clientservice.business.repository.model.BrandDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<BrandDAO, Long> {
}
