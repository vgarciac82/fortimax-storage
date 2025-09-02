package com.axtel.fortimax.storage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VolumePathInfo {

    private String volume; // Volume ID (e.g., 0a00000f)
    private String relativePath; // Path like 000/000/001/0a00000f/
    private String absolutePath; // Full path to the volume folder
    private String fileName; // Generated UUID filename, e.g., a1b2c3d4e5f6.tif
    private String fullPath; // Full file path = absolutePath + fileName

    private String diskUnit; // e.g., D:\
    private String basePath; // e.g., fortimax_2024\
}
