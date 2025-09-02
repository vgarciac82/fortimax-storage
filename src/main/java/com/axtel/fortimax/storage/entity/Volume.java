package com.axtel.fortimax.storage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "volume")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Volume {

	@Id
	@Column(name = "volume_id", length = 8)
	private String volumeId;

	@Column(name = "disk_unit", length = 50, nullable = false)
	private String diskUnit;

	@Column(name = "base_path", length = 100, nullable = false)
	private String basePath;

	@Column(name = "relative_path", length = 150, nullable = false)
	private String relativePath;

	@Column(name = "capacity", length = 1, nullable = false)
	private String capacity;

	@Column(name = "volume_type", length = 1, nullable = false)
	private String volumeType;
}
