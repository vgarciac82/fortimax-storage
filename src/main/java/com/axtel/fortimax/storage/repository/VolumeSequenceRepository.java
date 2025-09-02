package com.axtel.fortimax.storage.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.axtel.fortimax.storage.entity.VolumeSequence;

@Repository
public interface VolumeSequenceRepository extends JpaRepository<VolumeSequence, String> {
	Optional<VolumeSequence> findTopByOrderByUpdatedAtDesc();

}
