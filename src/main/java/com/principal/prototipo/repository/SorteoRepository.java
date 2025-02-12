package com.principal.prototipo.repository;

import com.principal.prototipo.model.Sorteo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@ApplicationScoped
public class SorteoRepository implements PanacheRepository<Sorteo> {
    @PersistenceContext
    EntityManager em;

    public @Nullable List<Sorteo> allSorteo() {
        return null;
    }
    public List<Sorteo> findAllOrderedByFechaDesc() {
        return em.createQuery("SELECT s FROM Sorteo s ORDER BY s.fecha DESC", Sorteo.class)
                .getResultList();
    }

    public void persist(Sorteo sorteo) {
    }
}
