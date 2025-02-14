package com.principal.prototipo.controller;

import com.principal.prototipo.model.Registro;
import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.repository.SorteoRepository;
import com.principal.prototipo.service.SorteoService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/api/sorteo")
public class SorteoController {

    private static final Logger log = LoggerFactory.getLogger(SorteoController.class);

    @Inject
    SorteoService sorteoService;

    @Inject
    SorteoRepository sorteoRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Sorteo> getSorteo() {
        List<Sorteo> cuales = sorteoService.getAllSorteo();
        log.info("Salida, cantidad de sorteos: {}", cuales.size());
        return cuales;
    }

    @GET
    @Path("/probabilidad")
    public Map<Integer, Double> getProbabilidadSorteo() {
        List<Sorteo> cuales = sorteoService.getAllSorteo();
        log.info("Salida, probabilidad: {}", cuales.size());

        return sorteoService.calcularProbabilidad(cuales,7);
    }

    @GET
    @Path("/top7")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, List<Map<String, Object>>> getTop7NumbersByYear() {
        return sorteoService.getTop7NumbersByYear();
    }

    @GET
    @Path("/chica7")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, List<Registro>> Chica7NumbersByYear() {
        return sorteoService.obtenerChica7PorAnio();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Sorteo createPerson(Sorteo sorteo) {
        return sorteoService.createSorteo(sorteo);
    }
}
