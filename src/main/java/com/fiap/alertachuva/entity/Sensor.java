
package com.fiap.alertachuva.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private AreaMonitorada area;

    private String tipoSensor;
    private Double latitude;
    private Double longitude;
    private String status;
}
