
package com.fiap.alertachuva.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaIncendio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leitura_id")
    private LeituraSensor leituraGatilho;

    @ManyToOne
    @JoinColumn(name = "equipe_id")
    private EquipeResposta equipeDesignada;

    private String nivelSeveridade;
    private String statusAlerta;
}
