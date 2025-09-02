package com.axtel.fortimax.storage.service.impl;

import com.axtel.fortimax.storage.dto.VolumePathInfo;
import com.axtel.fortimax.storage.entity.StorageUnit;
import com.axtel.fortimax.storage.entity.Volume;
import com.axtel.fortimax.storage.entity.VolumeSequence;
import com.axtel.fortimax.storage.repository.StorageUnitRepository;
import com.axtel.fortimax.storage.repository.VolumeRepository;
import com.axtel.fortimax.storage.repository.VolumeSequenceRepository;
import com.axtel.fortimax.storage.service.VolumePathService;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VolumePathServiceImpl implements VolumePathService {

	private final VolumeSequenceRepository volumeSequenceRepository;
	private final StorageUnitRepository storageUnitRepository;
	private final VolumeRepository volumeRepository;

	private static final Logger log = LoggerFactory.getLogger(VolumePathServiceImpl.class);

	@Override
	public synchronized VolumePathInfo getNextAvailablePath() {
		StorageUnit unit = storageUnitRepository.findByStatus(1)
				.orElseThrow(() -> new IllegalStateException("No active storage unit configured."));

		Volume currentVolume = volumeRepository
				.findByDiskUnitAndBasePathAndCapacityAndVolumeType(unit.getDiskUnit(), unit.getBasePath(), "1", "0")
				.orElseThrow(() -> new IllegalStateException("No active volume found."));

		String volumePath = unit.getDiskUnit() + unit.getBasePath() + "_volumen/" + currentVolume.getRelativePath();
		File volumeDir = new File(volumePath);

		if (!volumeDir.exists() && !volumeDir.mkdirs()) {
			throw new IllegalStateException("Failed to create directory: " + volumePath);
		}

		int fileCount = Optional.ofNullable(volumeDir.listFiles(File::isFile)).map(arr -> arr.length).orElse(0);
		log.trace("Iniciando cálculo de la siguiente ruta disponible de volumen");

		log.debug("Unidad activa: {}{}", unit.getDiskUnit(), unit.getBasePath());
		log.debug("Volumen activo actual: {}", currentVolume.getVolumeId());
		log.debug("Archivos en volumen {}: {}", currentVolume.getVolumeId(), fileCount);
		if (fileCount >= 1024) {
			log.info("Volumen {} lleno. Generando nuevo volumen...", currentVolume.getVolumeId());

			String lastVolume = volumeSequenceRepository.findTopByOrderByUpdatedAtDesc()
					.orElseThrow(() -> new IllegalStateException("No volume sequence found.")).getLastVolume();

			String nextVolume = getNextVolume(lastVolume);
			String subPath = calculateVolumePath(nextVolume);
			String newVolumeFullPath = unit.getDiskUnit() + unit.getBasePath() + "_volumen/" + subPath;

			File newVolumeDir = new File(newVolumeFullPath);
			if (!newVolumeDir.exists() && !newVolumeDir.mkdirs()) {
				throw new IllegalStateException("Cannot create new volume directory: " + newVolumeFullPath);
			}

			currentVolume.setCapacity("0");
			volumeRepository.save(currentVolume);

			Volume newVolume = new Volume(nextVolume, unit.getDiskUnit(), unit.getBasePath(), subPath, "1", "0");
			volumeRepository.save(newVolume);

			VolumeSequence seq = new VolumeSequence(nextVolume, ZonedDateTime.now());
			volumeSequenceRepository.save(seq);

			currentVolume = newVolume;
			volumePath = newVolumeFullPath;

			log.info("Nuevo volumen registrado: {} en ruta {}", nextVolume, subPath);
		}

		String fileName = UUID.randomUUID().toString().replace("-", "") + ".tif";
		String fullPath = volumePath + fileName;
		log.debug("Archivo generado: {} en volumen {}", fileName, currentVolume.getVolumeId());
		return VolumePathInfo.builder().volume(currentVolume.getVolumeId())
				.relativePath(currentVolume.getRelativePath()).absolutePath(volumePath).fileName(fileName)
				.fullPath(fullPath).diskUnit(unit.getDiskUnit()).basePath(unit.getBasePath()).build();
	}

	private String getNextVolume(String currentVolume) {
		String prefix = currentVolume.substring(0, 2);
		String suffix = currentVolume.substring(2);
		int next = Integer.parseInt(suffix, 36) + 1;

		String nextSuffix = Integer.toString(next, 36).toLowerCase();
		while (nextSuffix.length() < 6) {
			nextSuffix = "0" + nextSuffix;
		}

		return prefix + nextSuffix;
	}

	private String calculateVolumePath(String volumeId) {
		int index = Integer.parseInt(volumeId.substring(2), 36);

		int lvl3 = (index / 1024) % 512;
		int lvl2 = (index / (1024 * 512)) % 512;
		int lvl1 = (index / (1024 * 512 * 512)) % 512;

		String part1 = String.format("%03x", lvl1);
		String part2 = String.format("%03x", lvl2);
		String part3 = String.format("%03x", lvl3);

		return part1 + "/" + part2 + "/" + part3 + "/" + volumeId + "/";
	}
}
