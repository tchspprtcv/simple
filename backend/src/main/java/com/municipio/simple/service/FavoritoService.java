package com.municipio.simple.service;

import com.municipio.simple.dto.FavoritoRequest;
import com.municipio.simple.dto.FavoritoResponse;
import com.municipio.simple.entity.Favorito;
import com.municipio.simple.entity.TipoServico;
import com.municipio.simple.entity.Usuario;
import com.municipio.simple.repository.FavoritoRepository;
import com.municipio.simple.repository.TipoServicoRepository;
import com.municipio.simple.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoServicoRepository tipoServicoRepository;

    private Usuario getAuthenticatedUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilizador não encontrado com email: " + email));
    }

    @Transactional(readOnly = true)
    public List<FavoritoResponse> listarFavoritosDoUsuarioLogado() {
        Usuario usuario = getAuthenticatedUser();
        return favoritoRepository.findByUsuarioOrderByOrdem(usuario).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoritoResponse adicionarFavorito(FavoritoRequest favoritoRequest) {
        Usuario usuario = getAuthenticatedUser();
        TipoServico tipoServico = tipoServicoRepository.findById(favoritoRequest.getTipoServicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tipo de Serviço não encontrado com ID: " + favoritoRequest.getTipoServicoId()));

        if (favoritoRepository.findByUsuarioAndTipoServicoId(usuario, tipoServico.getId()).isPresent()) {
            throw new IllegalArgumentException("Este serviço já está nos favoritos.");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setTipoServico(tipoServico);
        if (favoritoRequest.getOrdem() != null) {
            favorito.setOrdem(favoritoRequest.getOrdem());
        } else {
            // Define a ordem como o próximo número disponível
            List<Favorito> favoritosExistentes = favoritoRepository.findByUsuarioOrderByOrdem(usuario);
            favorito.setOrdem(favoritosExistentes.isEmpty() ? 0 : favoritosExistentes.get(favoritosExistentes.size() - 1).getOrdem() + 1);
        }

        Favorito salvo = favoritoRepository.save(favorito);
        return mapToResponse(salvo);
    }

    @Transactional
    public void removerFavorito(Integer tipoServicoId) {
        Usuario usuario = getAuthenticatedUser();
        Favorito favorito = favoritoRepository.findByUsuarioAndTipoServicoId(usuario, tipoServicoId)
                .orElseThrow(() -> new EntityNotFoundException("Favorito não encontrado para este utilizador e tipo de serviço."));
        favoritoRepository.delete(favorito);
    }

    private FavoritoResponse mapToResponse(Favorito favorito) {
        return FavoritoResponse.builder()
                .id(favorito.getId())
                .tipoServicoId(favorito.getTipoServico().getId())
                .tipoServicoNome(favorito.getTipoServico().getNome())
                .tipoServicoCodigo(favorito.getTipoServico().getCodigo())
                .ordem(favorito.getOrdem())
                .criadoEm(favorito.getCriadoEm())
                .build();
    }
}