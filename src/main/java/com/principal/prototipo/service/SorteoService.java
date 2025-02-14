package com.principal.prototipo.service;

import com.principal.prototipo.model.Registro;
import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.model.SorteoDTO;
import com.principal.prototipo.repository.SorteoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class SorteoService {

    @Inject
    SorteoRepository sorteoRepository;

    public List<Sorteo> getAllSorteo() {
        List<Sorteo> sontodos;
        sontodos = sorteoRepository.findAllOrderedByFechaDesc();
        return sontodos;
    }

    public List<SorteoDTO> getAllFechaDesc(){
        List<SorteoDTO> sonindex = sorteoRepository.findAllFechaDesc();
        return sonindex;
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

    public Map<Integer, List<Map<String, Object>>> getTop7NumbersByYear() {
        List<Object[]> resultados = sorteoRepository.getTop7NumbersByYear();

        return resultados.stream()
                .map(row -> Map.of(
                        "numero", row[0],
                        "anio", row[1],
                        "frecuencia", row[2]
                ))
                .collect(Collectors.groupingBy(row -> (Integer) row.get("anio")));
    }

    public Map<Integer, List<Registro>> obtenerChica7PorAnio() {
        List<Object[]> resultados = sorteoRepository.getChica7NumbersByYear();

        return resultados.stream()
                .map(row -> {
                    try {
                        // *** CASTING CORRECTO Y MANEJO DE NULOS ***
                        Integer numero = (Integer) row[0]; // Número como Long (o Integer si ya lo es en la BD)
                        Integer anio = (Integer) row[1]; // Año debería ser Integer
                        //Integer frecuencia = row[2]; // Frecuencia como Long (o Integer si ya lo es en la BD)
                        Long frecuenciaLong = (Long) row[2]; // Primero castea a Long
                        Integer frecuencia = frecuenciaLong.intValue(); // Luego convierte a int
                        if (anio == null || numero == null || frecuencia == null) {
                            System.err.println("Error: Valores nulos en la consulta a la base de datos.");
                            return null; // O lanza una excepción
                        }

                        //int numero = numero.intValue(); // Convertir Long a int (manejar overflow si es necesario)
                        //int frecuencia = frecuencia.intValue(); // Convertir Long a int (manejar overflow si es necesario)

                        return new Registro(anio, numero, frecuencia);
                    } catch (ClassCastException | NullPointerException e) {
                        System.err.println("Error de casting o nulo: " + e.getMessage());
                        return null;
                    } catch (ArithmeticException e) {
                        System.err.println("Error: Valor fuera del rango de int: " + e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull) // Eliminar los registros nulos (si los hay)
                .collect(Collectors.groupingBy(Registro::getAnio));
}}
