package com.ecommerce.util;

import java.io.IOException;
import java.util.Iterator;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

public class ViewExpiredExceptionHandler extends ExceptionHandlerWrapper {
    private final ExceptionHandler wrapped;

    public ViewExpiredExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();
            if (context.getException() instanceof ViewExpiredException) {
                events.remove();
                redirectToHome();
                break;
            }
        }

        getWrapped().handle();
    }

    private void redirectToHome() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_WARN,
                "Votre session a expiré. Veuillez réessayer.",
                null));
        context.getExternalContext().getFlash().setKeepMessages(true);
        try {
            context.getExternalContext().redirect(
                    context.getExternalContext().getRequestContextPath() + "/index.xhtml");
            context.responseComplete();
        } catch (IOException e) {
            throw new FacesException("Impossible de rediriger après l'expiration de la vue.", e);
        }
    }
}
