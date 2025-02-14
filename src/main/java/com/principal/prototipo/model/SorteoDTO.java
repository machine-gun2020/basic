package com.principal.prototipo.model;

import java.time.LocalDate;

public class SorteoDTO {
    private String numeros;
    private LocalDate fecha;

    public SorteoDTO(String numeros, LocalDate fecha) {
        this.numeros = numeros;
        this.fecha = fecha;
    }

    public String getNumeros() { return numeros; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        return "SorteoDTO{numeros='" + numeros + "', fecha=" + fecha + "}";
    }
}
