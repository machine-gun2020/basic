package com.principal.prototipo.controller;

import com.principal.prototipo.model.Registro;
import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.model.SorteoDTO;
import com.principal.prototipo.repository.SorteoRepository;
import com.principal.prototipo.service.SorteoService;
import jakarta.ws.rs.*;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
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
    @Path("/index")
    public List<SorteoDTO> getAllFechaDesc() {
        List<SorteoDTO> indexados = sorteoService.getAllFechaDesc();
        log.info("Salida, cantidad de sorteos indexados: {}", indexados.size());
        return indexados;
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
    public Response getTop7NumbersByYear(
            @QueryParam("anioInicio") Integer anioInicio,
            @QueryParam("anioTermino") Integer anioTermino) {

        Map<Integer, List<Map<String, Object>>> result;

        // Si no se envían los parámetros, usa el método original sin filtros.
        if (anioInicio == null && anioTermino == null) {
            result = sorteoService.getTop7NumbersByYear();
        } else {
            result = sorteoService.getTop7NumbersByYear(anioInicio, anioTermino);
        }

        return Response.ok(result).build();
    }

    @GET
    @Path("/chica7")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Integer, List<Registro>> Chica7NumbersByYear() {
        return sorteoService.obtenerChica7PorAnio();
    }
}
