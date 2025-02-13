package com.principal.prototipo.service;

import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.repository.SorteoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.LinkedHashMap;

@ApplicationScoped
public class SorteoService {

    @Inject
    SorteoRepository sorteoRepository;

    public List<Sorteo> getAllSorteo() {
        List<Sorteo> sontodos;
        sontodos = sorteoRepository.findAllOrderedByFechaDesc();
        return sontodos;
    }

    public Sorteo createSorteo(Sorteo sorteo) {
        sorteoRepository.persist(sorteo);
        return sorteo;
    }

    public Map<Integer, Double> calcularProbabilidad(List<Sorteo> sorteos, int topN) {
        if (sorteos.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<Integer, Long> frecuencia = sorteos.stream()
                .flatMap(sorteo -> Stream.of(sorteo.getN1(), sorteo.getN2(), sorteo.getN3(),
                        sorteo.getN4(), sorteo.getN5(), sorteo.getN6(),
                        sorteo.getComodin()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // Calcular la probabilidad
        int totalSorteos = sorteos.size();
        Map<Integer, Double> probabilidades = frecuencia.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> (double) entry.getValue() / (totalSorteos * 7),
                        (e1, e2) -> e1, LinkedHashMap::new
                ));

        // Ordenar por probabilidad descendente y limitar a los `topN` valores más altos
        return probabilidades.entrySet().stream()
                .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                .limit(topN) // ← Filtrar solo los `N` más probables
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new
                ));
    }

}
