package com.fiap.alertachuva.controller;

import com.fiap.alertachuva.entity.AlertaIncendio;
import com.fiap.alertachuva.repository.AlertaIncendioRepository;
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
@RequestMapping("/alertas-incendio")
public class AlertaIncendioController {

    @Autowired
    private AlertaIncendioRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlertaIncendio criar(@RequestBody @Valid AlertaIncendio alerta) {
        return repository.save(alerta);
    }

    @GetMapping
    public Page<AlertaIncendio> listar(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaIncendio> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public AlertaIncendio atualizar(@PathVariable Long id, @RequestBody @Valid AlertaIncendio alertaAtualizado) {
        return repository.findById(id)
                .map(alertaExistente -> {
                    alertaExistente.setNivelSeveridade(alertaAtualizado.getNivelSeveridade());
                    alertaExistente.setStatusAlerta(alertaAtualizado.getStatusAlerta());
                    return repository.save(alertaExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta de Incêndio não encontrado com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Alerta de Incêndio não encontrado com ID: " + id));
        repository.deleteById(id);
    }
}