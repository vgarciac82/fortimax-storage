package com.axtel.fortimax.storage.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "storage_unit")
@IdClass(StorageUnitId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageUnit {

	@Id
	@Column(name = "disk_unit", length = 50)
	private String diskUnit;

	@Id
	@Column(name = "base_path", length = 100)
	private String basePath;

	@Column(name = "status", nullable = false)
	private Integer status;

	@Column(name = "device_type", length = 5)
	private String deviceType;
}
