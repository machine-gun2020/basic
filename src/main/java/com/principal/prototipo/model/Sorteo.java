package com.principal.prototipo.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sorteo")
public class Sorteo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer n1;
    private Integer n2;
    private Integer n3;
    private Integer n4;
    private Integer n5;
    private Integer n6;
    private Integer comodin;
    private LocalDate fecha;
    private Long monto;

    // Getters and Setters
    public Integer getId() {return id; }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getN1() {
        return n1;
    }
    public void setN1(Integer n1) {
        this.n1 = n1;
    }
    public Integer getN2() {
        return n2;
    }
    public void setN2(Integer n2) {
        this.n2 = n2;
    }
    public Integer getN3() {
        return n3;
    }
    public void setN3(Integer n3) {
        this.n3 = n3;
    }
    public Integer getN4() {
        return n4;
    }
    public void setN4(Integer n4) {
        this.n4 = n4;
    }
    public Integer getN5() {
        return n5;
    }
    public void setN5(Integer n5) {
        this.n5 = n5;
    }
    public Integer getN6() {
        return n6;
    }
    public void setN6(Integer n6) {
        this.n6 = n6;
    }
    public Integer getComodin() {
        return comodin;
    }
    public void setComodin(Integer comodin) {
        this.comodin = comodin;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Long getMonto() {
        return monto;
    }
    public void setMonto(Long monto) {
        this.monto = monto;
    }

    // toString() para imprimir los registros
    @Override
    public String toString() {
        return "Sorteo{" +
                "id=" + id +
                "|" + n1 +
                "|" + n2 +
                "|" + n3 +
                "|" + n4 +
                "|" + n5 +
                "|" + n6 +
                ", comodin=" + comodin +
                ", fecha=" + fecha +
                ", monto=" + monto +
                '}';
    }
}
