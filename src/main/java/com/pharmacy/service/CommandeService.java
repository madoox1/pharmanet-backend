package com.pharmacy.service;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.CommandeEJBImpl;
import com.pharmacy.model.Commande;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class CommandeService {
    @EJB
    private CommandeEJBImpl commandeEJBImpl;
    private PatientService patientService;
    public Commande getCommandeStatus(Long patientId, Long commandeId) throws NotFoundException {
        patientService.findPatientById(patientId);
        return commandeEJBImpl.getCommandeStatus(patientId, commandeId);
    }

    public Object getCommandesByPatientId(Long patientId) throws NotFoundException {
        patientService.findPatientById(patientId);
        return commandeEJBImpl.getCommandesByPatientId(patientId);
    }
}
