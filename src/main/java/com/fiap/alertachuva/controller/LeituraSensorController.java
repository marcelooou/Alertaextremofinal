package com.fiap.alertachuva.controller;

import com.fiap.alertachuva.entity.LeituraSensor;
import com.fiap.alertachuva.repository.LeituraSensorRepository;
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
@RequestMapping("/leituras-sensores")
public class LeituraSensorController {

    @Autowired
    private LeituraSensorRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LeituraSensor criar(@RequestBody @Valid LeituraSensor leitura) {
        return repository.save(leitura);
    }

    @GetMapping
    public Page<LeituraSensor> listar(@PageableDefault(size = 10, sort = "timestampLeitura") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeituraSensor> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public LeituraSensor atualizar(@PathVariable Long id, @RequestBody @Valid LeituraSensor leituraAtualizada) {
        return repository.findById(id)
                .map(leituraExistente -> {
                    leituraExistente.setTipoMedicao(leituraAtualizada.getTipoMedicao());
                    leituraExistente.setValorMedicao(leituraAtualizada.getValorMedicao());
                    leituraExistente.setTimestampLeitura(leituraAtualizada.getTimestampLeitura());
                    return repository.save(leituraExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leitura de Sensor não encontrada com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leitura de Sensor não encontrada com ID: " + id));
        repository.deleteById(id);
    }
}