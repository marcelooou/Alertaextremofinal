package com.fiap.alertachuva.controller;

import com.fiap.alertachuva.entity.AreaMonitorada;
import com.fiap.alertachuva.repository.AreaMonitoradaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/areas")
public class AreaMonitoradaController {

    @Autowired
    private AreaMonitoradaRepository repository;

    @GetMapping
    public Page<AreaMonitorada> listar(@PageableDefault(size = 5, sort = "nomeArea") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PostMapping
    public AreaMonitorada criar(@RequestBody @Valid AreaMonitorada area) {
        return repository.save(area);
    }

    @GetMapping("/{id}")
    public AreaMonitorada buscar(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @PutMapping("/{id}")
    public AreaMonitorada atualizar(@PathVariable Long id, @RequestBody @Valid AreaMonitorada area) {
        area.setId(id);
        return repository.save(area);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
