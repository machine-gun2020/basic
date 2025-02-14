package com.principal.prototipo.repository;

import com.principal.prototipo.model.Sorteo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    // SQL por year, y multi column
    public List<Object[]> getTop7NumbersByYear() {
        return em.createNativeQuery("""
            WITH Numeros AS (
                SELECT s.n1 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.n2 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.n3 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.n4 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.n5 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.n6 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                UNION ALL
                SELECT s.comodin AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
            ),
            Conteo AS (
                SELECT 
                    num, 
                    anio, 
                    COUNT(*) AS frecuencia,
                    ROW_NUMBER() OVER (PARTITION BY anio ORDER BY COUNT(*) DESC) AS rn
                FROM Numeros
                WHERE num IS NOT NULL
                GROUP BY num, anio
            )
            SELECT num, anio, frecuencia
            FROM Conteo
            WHERE rn <= 7
            ORDER BY anio DESC, frecuencia DESC
        """).getResultList();
    }

    public List<Object[]> getChica7NumbersByYear() {
        return em.createNativeQuery(
                """
                WITH Numeros AS (
                    SELECT s.n1 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.n2 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.n3 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.n4 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.n5 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.n6 AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                    UNION ALL
                    SELECT s.comodin AS num, EXTRACT(YEAR FROM s.fecha) AS anio FROM Sorteo s
                ),
                Conteo AS (
                    SELECT
                        num,
                        anio,
                        COUNT(*) AS frecuencia,
                        ROW_NUMBER() OVER (PARTITION BY anio ORDER BY COUNT(*) ASC, num ASC) AS rn
                    FROM Numeros
                    WHERE num IS NOT NULL
                    GROUP BY num, anio
                )
                SELECT num, anio, frecuencia
                FROM Conteo
                WHERE rn <= 7
                ORDER BY anio DESC, frecuencia ASC;
                """).getResultList();
    }


    public void persist(Sorteo sorteo) {
    }
}
