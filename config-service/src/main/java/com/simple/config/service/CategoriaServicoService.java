package com.simple.config.service;

import com.simple.config.domain.entity.CategoriaServico;
import com.simple.config.dto.CategoriaServicoRequest;
import com.simple.config.dto.CategoriaServicoResponse;
import com.simple.config.repository.CategoriaServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServicoService {

    private final CategoriaServicoRepository categoriaServicoRepository;

    @Transactional(readOnly = true)
    public List<CategoriaServicoResponse> findAll(boolean apenasAtivos) {
        List<CategoriaServico> categorias;
        if (apenasAtivos) {
            categorias = categoriaServicoRepository.findByAtivoTrueOrderByOrdem();
        } else {
            categorias = categoriaServicoRepository.findAll(Sort.by(Sort.Order.asc("ordem"), Sort.Order.asc("nome")));
        }
        return categorias.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaServicoResponse findById(Integer id) {
        return categoriaServicoRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de Serviço não encontrada com ID: " + id));
    }

    @Transactional
    public CategoriaServicoResponse create(CategoriaServicoRequest request) {
        categoriaServicoRepository.findByNome(request.getNome()).ifPresent(cs -> {
            throw new IllegalArgumentException("Categoria de Serviço com o nome '" + request.getNome() + "' já existe.");
        });

        CategoriaServico categoriaServico = new CategoriaServico();
        categoriaServico.setNome(request.getNome());
        categoriaServico.setDescricao(request.getDescricao());
        categoriaServico.setIcone(request.getIcone());
        categoriaServico.setCor(request.getCor());
        categoriaServico.setOrdem(request.getOrdem());
        categoriaServico.setAtivo(request.getAtivo());

        CategoriaServico savedCategoria = categoriaServicoRepository.save(categoriaServico);
        return mapToResponse(savedCategoria);
    }

    @Transactional
    public CategoriaServicoResponse update(Integer id, CategoriaServicoRequest request) {
        CategoriaServico categoriaServico = categoriaServicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria de Serviço não encontrada com ID: " + id));

        categoriaServicoRepository.findByNome(request.getNome()).ifPresent(cs -> {
            if (!cs.getId().equals(id)) {
                throw new IllegalArgumentException("Outra Categoria de Serviço com o nome '" + request.getNome() + "' já existe.");
            }
        });

        categoriaServico.setNome(request.getNome());
        categoriaServico.setDescricao(request.getDescricao());
        categoriaServico.setIcone(request.getIcone());
        categoriaServico.setCor(request.getCor());
        categoriaServico.setOrdem(request.getOrdem());
        categoriaServico.setAtivo(request.getAtivo());

        CategoriaServico updatedCategoria = categoriaServicoRepository.save(categoriaServico);
        return mapToResponse(updatedCategoria);
    }

    @Transactional
    public void deleteById(Integer id) {
        if (!categoriaServicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria de Serviço não encontrada com ID: " + id);
        }
        // TODO: Consider implications: what happens to TipoServico referencing this Categoria?
        // Options: Prevent deletion if referenced, set Categoria to null in TipoServico, delete TipoServico.
        // For now, direct delete.
        categoriaServicoRepository.deleteById(id);
    }

    private CategoriaServicoResponse mapToResponse(CategoriaServico categoria) {
        return CategoriaServicoResponse.builder()
                .id(categoria.getId())
                .nome(categoria.getNome())
                .descricao(categoria.getDescricao())
                .icone(categoria.getIcone())
                .cor(categoria.getCor())
                .ordem(categoria.getOrdem())
                .ativo(categoria.isAtivo())
                .criadoEm(categoria.getCriadoEm())
                .atualizadoEm(categoria.getAtualizadoEm())
                .build();
    }
}
