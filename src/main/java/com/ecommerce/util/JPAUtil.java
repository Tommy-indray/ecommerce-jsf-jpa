package com.ecommerce.util;

import java.util.function.Consumer;
import java.util.function.Function;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Pattern Singleton : une seule EntityManagerFactory pour toute l'application
 * (la creation d'une factory est couteuse). Fournit aussi des helpers
 * qui gerent le cycle de vie de l'EntityManager et des transactions.
 */
public final class JPAUtil {

    public static final String PERSISTENCE_UNIT = "ecommercePU";

    private static volatile EntityManagerFactory emf;

    private JPAUtil() { }

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            synchronized (JPAUtil.class) {
                if (emf == null) {
                    emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
                }
            }
        }
        return emf;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    /** Lecture seule : ouvre un EntityManager, execute, puis le ferme. */
    public static <T> T query(Function<EntityManager, T> work) {
        EntityManager em = createEntityManager();
        try {
            return work.apply(em);
        } finally {
            em.close();
        }
    }

    /** Ecriture : begin / commit, rollback automatique en cas d'exception. */
    public static <T> T callInTransaction(Function<EntityManager, T> work) {
        EntityManager em = createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T result = work.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public static void runInTransaction(Consumer<EntityManager> work) {
        callInTransaction(em -> {
            work.accept(em);
            return null;
        });
    }

    public static synchronized void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
        emf = null;
    }
}
