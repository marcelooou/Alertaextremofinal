package com.fiap.alertachuva.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AreaMonitorada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome da área é obrigatório")
    private String nomeArea;

    @NotNull(message = "Latitude é obrigatória")
    @DecimalMin(value = "-90.0")
    @DecimalMax(value = "90.0")
    private Double latitudeCentro;

    @NotNull(message = "Longitude é obrigatória")
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    private Double longitudeCentro;

    @NotNull(message = "Raio em km é obrigatório")
    @DecimalMin(value = "0.1")
    private Double raioKm;
}
