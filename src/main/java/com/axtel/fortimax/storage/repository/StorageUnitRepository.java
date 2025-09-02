package com.axtel.fortimax.storage.repository;

import com.axtel.fortimax.storage.entity.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StorageUnitRepository extends JpaRepository<StorageUnit, String> {

	Optional<StorageUnit> findByStatus(Integer status);
}
