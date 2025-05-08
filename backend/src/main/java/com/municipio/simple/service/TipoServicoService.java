package com.municipio.simple.service;

import com.municipio.simple.entity.TipoServico;
import com.municipio.simple.dto.TipoServicoRequest;
import com.municipio.simple.entity.CategoriaServico;
import com.municipio.simple.repository.CategoriaServicoRepository;
import com.municipio.simple.repository.TipoServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoServicoService {

    @Autowired
    private TipoServicoRepository tipoServicoRepository;

    @Autowired
    private CategoriaServicoRepository categoriaServicoRepository;

    public List<TipoServico> findAll() {
        return tipoServicoRepository.findAll();
    }

    public Optional<TipoServico> findById(Integer id) {
        return tipoServicoRepository.findById(id);
    }

    public TipoServico save(TipoServico tipoServico) {
        return tipoServicoRepository.save(tipoServico);
    }

    public void deleteById(Integer id) {
        tipoServicoRepository.deleteById(id);
    }

    public TipoServico create(TipoServicoRequest tipoServicoRequest) {
        TipoServico tipoServico = new TipoServico();
        // Busca a CategoriaServico pelo ID fornecido no request
        CategoriaServico categoria = categoriaServicoRepository.findById(tipoServicoRequest.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + tipoServicoRequest.getCategoriaId()));
        tipoServico.setCategoria(categoria);

        tipoServico.setCodigo(tipoServicoRequest.getCodigo());
        tipoServico.setNome(tipoServicoRequest.getNome());
        tipoServico.setDescricao(tipoServicoRequest.getDescricao());
        tipoServico.setPrazoEstimado(tipoServicoRequest.getPrazoEstimado());
        tipoServico.setValorBase(tipoServicoRequest.getValorBase());
        tipoServico.setRequerVistoria(tipoServicoRequest.getRequerVistoria());
        tipoServico.setRequerAnaliseTecnica(tipoServicoRequest.getRequerAnaliseTecnica());
        tipoServico.setRequerAprovacao(tipoServicoRequest.getRequerAprovacao());
        tipoServico.setDisponivelPortal(tipoServicoRequest.getDisponivelPortal());
        tipoServico.setAtivo(tipoServicoRequest.getAtivo());

        return tipoServicoRepository.save(tipoServico);
    }

    public Optional<TipoServico> update(Integer id, TipoServicoRequest tipoServicoRequest) {
        return tipoServicoRepository.findById(id)
                .map(existingTipoServico -> {
                    // Busca a CategoriaServico pelo ID fornecido no request
                    CategoriaServico categoria = categoriaServicoRepository.findById(tipoServicoRequest.getCategoriaId())
                            .orElseThrow(() -> new RuntimeException("Categoria não encontrada com ID: " + tipoServicoRequest.getCategoriaId()));
                    existingTipoServico.setCategoria(categoria);

                    existingTipoServico.setCodigo(tipoServicoRequest.getCodigo());
                    existingTipoServico.setNome(tipoServicoRequest.getNome());
                    existingTipoServico.setDescricao(tipoServicoRequest.getDescricao());
                    existingTipoServico.setPrazoEstimado(tipoServicoRequest.getPrazoEstimado());
                    existingTipoServico.setValorBase(tipoServicoRequest.getValorBase());
                    existingTipoServico.setRequerVistoria(tipoServicoRequest.getRequerVistoria());
                    existingTipoServico.setRequerAnaliseTecnica(tipoServicoRequest.getRequerAnaliseTecnica());
                    existingTipoServico.setRequerAprovacao(tipoServicoRequest.getRequerAprovacao());
                    existingTipoServico.setDisponivelPortal(tipoServicoRequest.getDisponivelPortal());
                    existingTipoServico.setAtivo(tipoServicoRequest.getAtivo());
                    return tipoServicoRepository.save(existingTipoServico);
                });
    }

    // Outros métodos de serviço podem ser adicionados aqui
}