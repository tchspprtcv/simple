package com.municipio.simple.controller;

import com.municipio.simple.dto.TipoServicoRequest;
import com.municipio.simple.dto.TipoServicoResponse;
import com.municipio.simple.entity.TipoServico;
import com.municipio.simple.service.TipoServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tipos-servicos") // Define o path base para este controller
public class TipoServicoController {

    @Autowired
    private TipoServicoService tipoServicoService;

    // Mapeamento de Entidade para DTO
    private TipoServicoResponse convertToDto(TipoServico tipoServico) {
        TipoServicoResponse dto = new TipoServicoResponse();
        dto.setId(tipoServico.getId());
        if (tipoServico.getCategoria() != null) {
            dto.setCategoriaId(tipoServico.getCategoria().getId());
            dto.setCategoriaNome(tipoServico.getCategoria().getNome()); // Assumindo que CategoriaServico tem um método getNome()
        }
        dto.setCodigo(tipoServico.getCodigo());
        dto.setNome(tipoServico.getNome());
        dto.setDescricao(tipoServico.getDescricao());
        dto.setPrazoEstimado(tipoServico.getPrazoEstimado());
        dto.setValorBase(tipoServico.getValorBase());
        dto.setRequerVistoria(tipoServico.isRequerVistoria());
        dto.setRequerAnaliseTecnica(tipoServico.isRequerAnaliseTecnica());
        dto.setRequerAprovacao(tipoServico.isRequerAprovacao());
        dto.setDisponivelPortal(tipoServico.isDisponivelPortal());
        dto.setAtivo(tipoServico.isAtivo());
        dto.setCriadoEm(tipoServico.getCriadoEm());
        dto.setAtualizadoEm(tipoServico.getAtualizadoEm());
        return dto;
    }

    // Endpoint GET para listar todos os tipos de serviço
    @GetMapping
    public ResponseEntity<List<TipoServicoResponse>> getAllTiposServicos() {
        List<TipoServico> tiposServicos = tipoServicoService.findAll();
        List<TipoServicoResponse> dtos = tiposServicos.stream()
                                                .map(this::convertToDto)
                                                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // Endpoint GET para buscar um tipo de serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<TipoServicoResponse> getTipoServicoById(@PathVariable Integer id) {
        return tipoServicoService.findById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint POST para criar um novo tipo de serviço
    @PostMapping    
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<TipoServicoResponse> createTipoServico(@Valid @RequestBody TipoServicoRequest tipoServicoRequest) {
        try {
            TipoServico novoTipoServico = tipoServicoService.create(tipoServicoRequest);
            return new ResponseEntity<>(convertToDto(novoTipoServico), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Por exemplo, se a categoria não for encontrada
            return ResponseEntity.badRequest().build(); // Ou um erro mais específico
        }
    }

    // Endpoint PUT para atualizar um tipo de serviço existente
    @PutMapping("/{id}")    
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<TipoServicoResponse> update(@PathVariable Integer id, @Valid @RequestBody TipoServicoRequest tipoServicoRequest) {
        return tipoServicoService.update(id, tipoServicoRequest)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint DELETE para remover um tipo de serviço
    @DeleteMapping("/{id}")    
    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (tipoServicoService.findById(id).isPresent()) {
            tipoServicoService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}