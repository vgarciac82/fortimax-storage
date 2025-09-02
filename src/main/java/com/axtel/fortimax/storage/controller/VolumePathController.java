package com.axtel.fortimax.storage.controller;

import com.axtel.fortimax.storage.dto.VolumePathInfo;
import com.axtel.fortimax.storage.service.VolumePathService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/storage")
@RequiredArgsConstructor
public class VolumePathController {

	private final VolumePathService volumePathService;

	@GetMapping("/next-path")
	public VolumePathInfo getNextAvailablePath() {
		return volumePathService.getNextAvailablePath();
	}
}
