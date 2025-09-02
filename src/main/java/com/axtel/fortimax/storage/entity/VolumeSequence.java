package com.axtel.fortimax.storage.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "volume_sequence")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolumeSequence {

    @Id
    @Column(name = "last_volume", length = 8, nullable = false)
    private String lastVolume;

    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt;
}
