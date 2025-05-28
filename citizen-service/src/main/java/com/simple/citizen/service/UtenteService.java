package com.simple.citizen.service;

import com.simple.citizen.dto.CidadaoRequest;
import com.simple.citizen.dto.CidadaoResponse;
import com.simple.citizen.domain.entity.Utente;
import com.simple.citizen.repository.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils; // For checking blank strings

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtenteService {

    private final UtenteRepository cidadaoRepository;

    @Transactional(readOnly = true)
    public Page<CidadaoResponse> findAll(Pageable pageable) {
        return cidadaoRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CidadaoResponse findById(UUID id) {
        return cidadaoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Utente não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public CidadaoResponse findByDocumento(String tipoDocumento, String numeroDocumento) {
        return cidadaoRepository.findByTipoDocumentoAndNumeroDocumento(tipoDocumento, numeroDocumento)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Utente não encontrado com documento: " + tipoDocumento + "/" + numeroDocumento));
    }

    @Transactional
    public CidadaoResponse create(CidadaoRequest request) {
        // Check if citizen already exists by document
        Optional<Utente> existingCidadaoByDoc = cidadaoRepository
                .findByTipoDocumentoAndNumeroDocumento(request.getTipoDocumento(), request.getNumeroDocumento());
        if (existingCidadaoByDoc.isPresent()) {
            throw new IllegalArgumentException("Utente já cadastrado com este tipo e número de documento.");
        }

        // Check if citizen already exists by email if email is provided
        if (StringUtils.hasText(request.getEmail())) {
            Optional<Utente> existingCidadaoByEmail = cidadaoRepository.findByEmail(request.getEmail());
            if (existingCidadaoByEmail.isPresent()) {
                throw new IllegalArgumentException("Utente já cadastrado com este email.");
            }
        }

        Utente cidadao = new Utente();
        cidadao.setNome(request.getNome());
        cidadao.setTipoDocumento(request.getTipoDocumento());
        cidadao.setNumeroDocumento(request.getNumeroDocumento());
        cidadao.setEmail(request.getEmail()); // Can be null
        cidadao.setTelefone(request.getTelefone()); // Can be null
        cidadao.setEndereco(request.getEndereco()); // Can be null

        Utente savedCidadao = cidadaoRepository.save(cidadao);
        return mapToResponse(savedCidadao);
    }

    @Transactional
    public CidadaoResponse update(UUID id, CidadaoRequest request) {
        Utente cidadao = cidadaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Utente não encontrado com ID: " + id));

        // Check if the email is being changed and if the new email already exists for another citizen
        if (StringUtils.hasText(request.getEmail()) && !request.getEmail().equals(cidadao.getEmail())) {
            Optional<Utente> existingCidadaoByEmail = cidadaoRepository.findByEmail(request.getEmail());
            if (existingCidadaoByEmail.isPresent()) {
                throw new IllegalArgumentException("Outro utente já cadastrado com este email.");
            }
        }
        
        // Tipo de documento and numero de documento are usually not updatable.
        // If they are, similar checks to the create method would be needed.

        cidadao.setNome(request.getNome());
        cidadao.setEmail(request.getEmail());
        cidadao.setTelefone(request.getTelefone());
        cidadao.setEndereco(request.getEndereco());

        Utente updatedCidadao = cidadaoRepository.save(cidadao);
        return mapToResponse(updatedCidadao);
    }

    public void delete(UUID id) {
        if (!cidadaoRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente não encontrado com ID: " + id);
        }
        cidadaoRepository.deleteById(id);
    }

    private CidadaoResponse mapToResponse(Utente cidadao) {
        return CidadaoResponse.builder()
                .id(cidadao.getId())
                .nome(cidadao.getNome())
                .tipoDocumento(cidadao.getTipoDocumento())
                .numeroDocumento(cidadao.getNumeroDocumento())
                .email(cidadao.getEmail())
                .telefone(cidadao.getTelefone())
                .endereco(cidadao.getEndereco())
                .criadoEm(cidadao.getCriadoEm())
                .atualizadoEm(cidadao.getAtualizadoEm()) // Ensure this is included
                .build();
    }
}
