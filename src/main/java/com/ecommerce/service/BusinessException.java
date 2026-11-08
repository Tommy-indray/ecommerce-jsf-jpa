package com.ecommerce.service;

/** Erreur metier (affichee a l'utilisateur). Non verifiee pour provoquer le rollback. */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }
}
