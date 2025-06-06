package com.fiap.alertachuva.controller;

import com.fiap.alertachuva.entity.Sensor;
import com.fiap.alertachuva.repository.SensorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/sensores")
public class SensorController {

    @Autowired
    private SensorRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sensor criar(@RequestBody @Valid Sensor sensor) {
        return repository.save(sensor);
    }

    @GetMapping
    public Page<Sensor> listar(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sensor> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Sensor atualizar(@PathVariable Long id, @RequestBody @Valid Sensor sensorAtualizado) {
        return repository.findById(id)
                .map(sensorExistente -> {
                    sensorExistente.setTipoSensor(sensorAtualizado.getTipoSensor());
                    sensorExistente.setLatitude(sensorAtualizado.getLatitude());
                    sensorExistente.setLongitude(sensorAtualizado.getLongitude());
                    sensorExistente.setStatus(sensorAtualizado.getStatus());
                    return repository.save(sensorExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor não encontrado com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sensor não encontrado com ID: " + id));
        repository.deleteById(id);
    }
}