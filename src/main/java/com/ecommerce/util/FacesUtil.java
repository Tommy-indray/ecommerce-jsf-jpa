package com.ecommerce.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/** Petits utilitaires JSF (messages, session). */
public final class FacesUtil {

    private FacesUtil() { }

    public static void info(String message) {
        add(FacesMessage.SEVERITY_INFO, message);
    }

    public static void error(String message) {
        add(FacesMessage.SEVERITY_ERROR, message);
    }

    private static void add(FacesMessage.Severity severity, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, message, null));
    }

    /** Conserve les messages apres une redirection (faces-redirect=true). */
    public static void keepMessages() {
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public static void invalidateSession() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }
}
