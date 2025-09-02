package com.axtel.fortimax.storage.service;

import com.axtel.fortimax.storage.dto.VolumePathInfo;

public interface VolumePathService {

    /**
     * Calcula y devuelve la siguiente ruta disponible para almacenar un archivo.
     *
     * @return Información completa de la ruta y nombre de archivo.
     */
    VolumePathInfo getNextAvailablePath();
}
