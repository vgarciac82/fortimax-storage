package com.axtel.fortimax.storage.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StorageUnitId implements Serializable {

	private static final long serialVersionUID = -1484692784627784327L;
	private String diskUnit;
	private String basePath;
}
