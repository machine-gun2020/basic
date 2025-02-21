package com.principal.prototipo.repository;

import com.principal.prototipo.model.Sorteo;
import com.principal.prototipo.model.SorteoDTO;
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
                .setHint("org.hibernate.readOnly", true)      // No modifica datos
                .setHint("org.hibernate.fetchSize", 50)       // Recupera en lotes de 50
                .setHint("org.hibernate.cacheable", true)     // Usa caché de segundo nivel
                .setHint("jakarta.persistence.query.retrieveMode", "USE")
                .setHint("jakarta.persistence.query.storeMode", "USE")
                .getResultList();
    }

    public List<SorteoDTO> findAllFechaDesc(){
        return em.createQuery("SELECT CONCAT(s.n1, '-', s.n2, '-', s.n3, '-', s.n4, '-', s.n5, '-', s.n6, '-', s.comodin), s.fecha " +
                        "FROM Sorteo s ORDER BY s.fecha DESC",
                SorteoDTO.class
        )       .setHint("org.hibernate.readOnly", true)
                .setHint("org.hibernate.fetchSize", 50)
                .setHint("org.hibernate.cacheable", true)
                .setHint("jakarta.persistence.query.retrieveMode", "USE")
                .setHint("jakarta.persistence.query.storeMode", "USE")
                .getResultList();
   }
    // SQL por year, y multi column
    public List<Object[]> getTop7NumbersByYear() {
        return getTop7NumbersByYear(null, null);
    }

    public List<Object[]> getTop7NumbersByYear(Integer anioInicio, Integer anioTermino) {
        String query = """
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
        """;

        if (anioInicio != null) {
            query += " AND anio >= :anioInicio";
        }
        if (anioTermino != null) {
            query += " AND anio <= :anioTermino";
        }

        query += " ORDER BY anio DESC, frecuencia DESC";

        var nativeQuery = em.createNativeQuery(query)
                .setHint("org.hibernate.readOnly", true)
                .setHint("org.hibernate.fetchSize", 50)
                .setHint("org.hibernate.cacheable", true)
                .setHint("jakarta.persistence.query.retrieveMode", "USE")
                .setHint("jakarta.persistence.query.storeMode", "USE");

        if (anioInicio != null) {
            nativeQuery.setParameter("anioInicio", anioInicio);
        }
        if (anioTermino != null) {
            nativeQuery.setParameter("anioTermino", anioTermino);
        }

        return nativeQuery.getResultList();
    }

    public List getChica7NumbersByYear() {
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
                """)
                .setHint("org.hibernate.readOnly", true)
                .setHint("org.hibernate.fetchSize", 50)
                .setHint("org.hibernate.cacheable", true)
                .setHint("jakarta.persistence.query.retrieveMode", "USE")
                .setHint("jakarta.persistence.query.storeMode", "USE")
                .getResultList();
    }


    public void persist(Sorteo sorteo) {
    }
}
