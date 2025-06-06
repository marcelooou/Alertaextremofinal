package com.fiap.alertachuva.controller;

import com.fiap.alertachuva.entity.EquipeResposta;
import com.fiap.alertachuva.repository.EquipeRespostaRepository;
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
@RequestMapping("/equipes-resposta")
public class EquipeRespostaController {

    @Autowired
    private EquipeRespostaRepository repository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EquipeResposta criar(@RequestBody @Valid EquipeResposta equipe) {
        return repository.save(equipe);
    }

    @GetMapping
    public Page<EquipeResposta> listar(@PageableDefault(size = 10, sort = "nomeEquipe") Pageable pageable) {
        return repository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipeResposta> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public EquipeResposta atualizar(@PathVariable Long id, @RequestBody @Valid EquipeResposta equipeAtualizada) {
        return repository.findById(id)
                .map(equipeExistente -> {
                    equipeExistente.setNomeEquipe(equipeAtualizada.getNomeEquipe());
                    equipeExistente.setBaseOperacao(equipeAtualizada.getBaseOperacao());
                    return repository.save(equipeExistente);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipe de Resposta não encontrada com ID: " + id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Equipe de Resposta não encontrada com ID: " + id));
        repository.deleteById(id);
    }
}