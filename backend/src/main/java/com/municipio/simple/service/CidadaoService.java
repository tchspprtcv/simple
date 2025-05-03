package com.municipio.simple.service;

import com.municipio.simple.dto.CidadaoRequest;
import com.municipio.simple.dto.CidadaoResponse;
import com.municipio.simple.entity.Cidadao;
import com.municipio.simple.repository.CidadaoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CidadaoService {

    private final CidadaoRepository cidadaoRepository;

    public Page<CidadaoResponse> findAll(Pageable pageable) {
        return cidadaoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public CidadaoResponse findById(UUID id) {
        return cidadaoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cidadão não encontrado"));
    }

    public CidadaoResponse findByDocumento(String tipoDocumento, String numeroDocumento) {
        return cidadaoRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Cidadão não encontrado"));
    }

    public CidadaoResponse create(CidadaoRequest request) {
        Optional<Cidadao> existingCidadao = cidadaoRepository
                .findByTipoDocumentoAndNumeroDocumento(request.getTipoDocumento(), request.getNumeroDocumento());
        
        if (existingCidadao.isPresent()) {
            throw new IllegalArgumentException("Cidadão já cadastrado com este documento");
        }

        Cidadao cidadao = new Cidadao();
        cidadao.setNome(request.getNome());
        cidadao.setTipoDocumento(request.getTipoDocumento());
        cidadao.setNumeroDocumento(request.getNumeroDocumento());
        cidadao.setEmail(request.getEmail());
        cidadao.setTelefone(request.getTelefone());
        cidadao.setEndereco(request.getEndereco());

        cidadaoRepository.save(cidadao);

        return mapToResponse(cidadao);
    }

    public CidadaoResponse update(UUID id, CidadaoRequest request) {
        Cidadao cidadao = cidadaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cidadão não encontrado"));

        cidadao.setNome(request.getNome());
        cidadao.setEmail(request.getEmail());
        cidadao.setTelefone(request.getTelefone());
        cidadao.setEndereco(request.getEndereco());

        cidadaoRepository.save(cidadao);

        return mapToResponse(cidadao);
    }

    private CidadaoResponse mapToResponse(Cidadao cidadao) {
        return CidadaoResponse.builder()
                .id(cidadao.getId())
                .nome(cidadao.getNome())
                .tipoDocumento(cidadao.getTipoDocumento())
                .numeroDocumento(cidadao.getNumeroDocumento())
                .email(cidadao.getEmail())
                .telefone(cidadao.getTelefone())
                .endereco(cidadao.getEndereco())
                .criadoEm(cidadao.getCriadoEm())
                .build();
    }
}
