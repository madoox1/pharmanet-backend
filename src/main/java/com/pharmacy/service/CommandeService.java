package com.pharmacy.service;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.ejb.Implimentation.CommandeEJBImpl;
import com.pharmacy.ejb.Implimentation.OrdonnanceEJBImpl;
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
    private OrdonnanceEJBImpl ordonnanceEJB;
    @Inject
    private NotificationService notificationService;

    public Commande getCommandeStatus(Long patientId, Long commandeId) throws NotFoundException {
        patientService.findPatientById(patientId);
        return commandeEJBImpl.getCommandeStatus(patientId, commandeId);
    }

    public Commande createCommande(Long patientId, Long ordonnanceId) throws NotFoundException {
        // Validate patient and ordonnance
        Patient patient = patientService.findPatientById(patientId);
        Ordonnance ordonnance = ordonnanceEJB.findOrdonnanceById(ordonnanceId);
        if (ordonnance == null || !ordonnance.getPatient().getId().equals(patientId)) {
            throw new NotFoundException("Ordonnance not found for this patient");
        }

        // Check if commande already exists for this ordonnance
        if (commandeExists(ordonnanceId)) {
            throw new NotFoundException("Commande already exists for this ordonnance");
        }

        Commande commande = new Commande();
        commande.setPatient(patient);
        commande.setOrdonnance(ordonnance);

        Commande newCommande = commandeEJBImpl.addCommande(commande);

        // Send notification to patient
        notificationService.sendEmail(patient.getEmail(), "Commande Created", "Your commande has been created.");

        return newCommande;
    }

    private boolean commandeExists(Long ordonnanceId) {
        List<Commande> existingCommandes = commandeEJBImpl.getCommandesByOrdonnanceId(ordonnanceId);
        return !existingCommandes.isEmpty();
    }

    public Commande getCommande(Long patientId, Long commandeId) throws NotFoundException {
        // Validate patient
        patientService.findPatientById(patientId);
        
        // Retrieve commande
        Commande commande = commandeEJBImpl.findCommandeById(commandeId);
        if (commande == null || !commande.getPatient().getId().equals(patientId)) {
            throw new NotFoundException("Commande not found for this patient");
        }
        return commande;
    }

    public List<Commande> getCommandesByPatientId(Long patientId) throws NotFoundException {
        // Validate patient
        patientService.findPatientById(patientId);
        
        // Retrieve commandes
        List<Commande> commandes = commandeEJBImpl.getCommandesByPatientId(patientId);
        if (commandes.isEmpty()) {
            throw new NotFoundException("No commandes found for patient with id " + patientId);
        }
        return commandes;
    }

    public Commande updateCommandeStatus(Long patientId, Long commandeId, CommandeStatut statut) throws NotFoundException {
        // Validate patient
        patientService.findPatientById(patientId);

        // Retrieve commande
        Commande commande = commandeEJBImpl.findCommandeById(commandeId);
        if (commande == null || !commande.getPatient().getId().equals(patientId)) {
            throw new NotFoundException("Commande not found for this patient");
        }

        // Update status
        commande.setStatut(statut);
        Commande updatedCommande = commandeEJBImpl.updateCommande(commande);

        // Send notification to patient
        notificationService.sendEmail(commande.getPatient().getEmail(), "Commande Status Updated", "Your commande status has been updated to " + statut);

        return updatedCommande;
    }
}
