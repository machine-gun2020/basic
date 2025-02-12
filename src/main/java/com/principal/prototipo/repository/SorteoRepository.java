package com.principal.prototipo.repository;

import com.principal.prototipo.model.Sorteo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApplicationScoped
public class SorteoRepository implements PanacheRepository<Sorteo> {


    public @Nullable List<Sorteo> allSorteo() {
        return null;
    }

    public void persist(Sorteo sorteo) {
    }
}
