package com.principal.prototipo.model;
import jakarta.persistence.*;




public class Registro {
    private int anio;
    private int numero; // O Long si necesitas Longs
    private int frecuencia; // O Long si necesitas Longs

    // Constructor (debe existir para que puedas crear objetos Registro)
    public Registro(int anio, int numero, int frecuencia) {
        this.anio = anio;
        this.numero = numero;
        this.frecuencia = frecuencia;
    }

    public int getAnio() {
        return anio;
    }

    public int getNumero() {
        return numero;
    }

    public int getFrecuencia() {
        return frecuencia;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setFrecuencia(int frecuencia) {
        this.frecuencia = frecuencia;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "anio=" + anio +
                ", numero=" + numero +
                ", frecuencia=" + frecuencia +
                '}';
    }
// ... otros métodos si los tienes
}
