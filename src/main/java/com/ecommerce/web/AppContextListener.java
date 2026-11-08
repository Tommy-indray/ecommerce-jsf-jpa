package com.ecommerce.web;

import com.ecommerce.service.DataInitializer;
import com.ecommerce.util.JPAUtil;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/** Demarre JPA (creation des tables par EclipseLink) et ferme proprement la factory. */
@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            JPAUtil.getEntityManagerFactory();   // genere le schema MySQL
            DataInitializer.init();              // donnees de demonstration
            LOG.info("Base de donnees initialisee.");
        } catch (RuntimeException e) {
            LOG.log(Level.SEVERE, "Initialisation JPA impossible : verifiez MySQL et persistence.xml", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JPAUtil.close();
    }
}
