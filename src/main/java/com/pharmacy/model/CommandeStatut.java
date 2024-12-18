package com.pharmacy.model;

public enum CommandeStatut {
    EN_ATTENTE,  // Pending
    PAYEE,       // Paid
    EN_COURS,    // In Progress
    LIVREE,      // Delivered
    ANNULEE      // Canceled
}