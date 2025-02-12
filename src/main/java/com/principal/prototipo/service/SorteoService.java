package com.principal.prototipo.service;

import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.repository.SorteoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class SorteoService {

    @Inject
    SorteoRepository sorteoRepository;

    public List<Sorteo> getAllSorteo() {
        List<Sorteo> sontodos;
        sontodos = sorteoRepository.listAll();
        return sontodos;
    }

    public Sorteo createSorteo(Sorteo sorteo) {
        sorteoRepository.persist(sorteo);
        return sorteo;
    }
}
