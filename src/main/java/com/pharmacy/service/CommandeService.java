package com.pharmacy.service;

import com.pharmacy.Exceptions.BadRequestException;
import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.CommandeEJBImpl;
import com.pharmacy.model.Commande;
import com.pharmacy.model.Ordonnance;
import com.pharmacy.model.Patient;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class CommandeService {
    @EJB
    private CommandeEJBImpl commandeEJBImpl;
    @Inject
    private PatientService patientService;
    @Inject
    private OrdonnanceService ordonnanceService;

    public Commande createCommande(Long patientId, Long ordonnanceId) throws NotFoundException, BadRequestException {
        // Valider patient et ordonnance
        Patient patient = patientService.findPatientById(patientId);
        Ordonnance ordonnance = ordonnanceService.findOrdonnanceById(ordonnanceId);
        
        // Vérifier si la commande existe déjà pour cette ordonnance
        if (commandeExists(ordonnanceId)) {
            throw new BadRequestException("Une commande existe déjà pour cette ordonnance");
        }

        // Créer la nouvelle commande
        Commande commande = new Commande();
        commande.setPatient(patient);
        commande.setOrdonnance(ordonnance);

        return commandeEJBImpl.addCommande(commande);
    }

    private boolean commandeExists(Long ordonnanceId) {
        return !commandeEJBImpl.findCommandeByOrdonnanceId(ordonnanceId).isEmpty();
    }

    public Commande getCommandeStatus(Long patientId, Long commandeId) throws NotFoundException {
        patientService.findPatientById(patientId);
        return commandeEJBImpl.getCommandeStatus(patientId, commandeId);
    }

    public List<Commande> getCommandesByPatientId(Long patientId) throws NotFoundException {
        patientService.findPatientById(patientId); // Valider que le patient existe
        List<Commande> commandes = commandeEJBImpl.getCommandesByPatientId(patientId);
        if (commandes.isEmpty()) {
            throw new NotFoundException("Aucune commande trouvée pour ce patient");
        }
        return commandes;
    }
}
