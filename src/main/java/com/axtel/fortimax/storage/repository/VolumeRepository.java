package com.axtel.fortimax.storage.repository;

import com.axtel.fortimax.storage.entity.Volume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolumeRepository extends JpaRepository<Volume, String> {

	Optional<Volume> findByDiskUnitAndBasePathAndCapacityAndVolumeType(String diskUnit, String basePath,
			String capacity, String volumeType);
}
