package com.fiap.alertachuva.repository;

import com.fiap.alertachuva.entity.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

}
