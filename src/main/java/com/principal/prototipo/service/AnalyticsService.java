package com.principal.prototipo.service;

import com.principal.prototipo.repository.SorteoRepository;
import io.netty.util.internal.ThreadLocalRandom;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.IntPredicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class AnalyticsService {

    @Inject
    SorteoRepository sorteoRepository;

    private Map<Integer, Integer> frequencyCache;

    @PostConstruct
    void initCache() {
        refreshFrequencyCache();
    }

    // Método principal de frecuencia
    public Map<Integer, Integer> calculateFrequency() {
        if (frequencyCache == null || frequencyCache.isEmpty()) {
            refreshFrequencyCache();
        }
        return frequencyCache;
    }

    private void refreshFrequencyCache() {
        frequencyCache = new HashMap<>();
        IntStream.rangeClosed(1, 56).forEach(n -> frequencyCache.put(n, 0));

        sorteoRepository.findAllOrderedByFechaDesc().forEach(sorteo -> {
            incrementFrequency(sorteo.n1);
            incrementFrequency(sorteo.n2);
            incrementFrequency(sorteo.n3);
            incrementFrequency(sorteo.n4);
            incrementFrequency(sorteo.n5);
            incrementFrequency(sorteo.n6);
        });
    }

    private void incrementFrequency(int number) {
        frequencyCache.put(number, frequencyCache.getOrDefault(number, 0) + 1);
    }

    // Generador de combinaciones inteligentes
    public List<Integer> generateOptimalCombo() {
        Map<Integer, Integer> frequency = calculateFrequency();

        List<Integer> hotNumbers = frequency.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .limit(15)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Integer> coldNumbers = frequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .limit(15)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Collections.shuffle(hotNumbers);
        Collections.shuffle(coldNumbers);

        List<Integer> combo = new ArrayList<>();
        combo.addAll(hotNumbers.subList(0, 3)); // 3 calientes
        combo.addAll(coldNumbers.subList(0, 2)); // 2 fríos
        combo.add(ThreadLocalRandom.current().nextInt(25, 36)); // 1 aleatorio en zona media

        Collections.sort(combo);
        return combo;
    }

    // Métricos avanzados
    public Map<Integer, Long> getDelayedNumbers() {
        return IntStream.rangeClosed(1, 56)
                .boxed()
                .collect(Collectors.toMap(
                        n -> n,
                        n -> sorteoRepository.findLastAppearance(n)
                                .map(fecha -> ChronoUnit.DAYS.between(fecha, LocalDate.now()))
                                .orElse(-1L)
                ));
    }

    /**
     * Calcula estadísticas de paridad (pares/impares) sobre todos los números de sorteos históricos.
     * @return String formateado con conteos y porcentajes.
     */
    public String getOddEvenStats() {
        // 1. Usar IntPredicate (mejor solución)
        long totalPares = sorteoRepository.countAllNumbers((IntPredicate) n -> n % 2 == 0);
        long totalNumeros = sorteoRepository.totalNumbersAnalyzed();

        // 2. Cálculo seguro de porcentajes
        double porcentajePares = totalNumeros > 0 ? (totalPares * 100.0) / totalNumeros : 0.0;
        double porcentajeImpares = 100.0 - porcentajePares;

        // 3. Formateo profesional
        return String.format(Locale.US,
                """
                ===== ESTADÍSTICAS DE PARIDAD =====
                • Total analizados: %,d
                • Pares: %,d (%.2f%%)
                • Impares: %,d (%.2f%%)""",
                totalNumeros,
                totalPares, porcentajePares,
                (totalNumeros - totalPares), porcentajeImpares
        );
    }

    // Método auxiliar reusable
    private double calculatePercentage(long partial, long total) {
        return total > 0 ? (partial * 100.0) / total : 0.0;
    }
}
