package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Commande;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class CommandeEJBImpl {

    @PersistenceContext(unitName = "pharmacyPU")
    private EntityManager em;

    public Commande addCommande(Commande commande) {
        em.persist(commande);
        return commande;
    }

    public Commande findCommandeById(Long id) {
        return em.find(Commande.class, id);
    }

    public List<Commande> getCommandesByOrdonnanceId(Long ordonnanceId) {
        TypedQuery<Commande> query = em.createQuery("SELECT c FROM Commande c WHERE c.ordonnance.id = :ordonnanceId", Commande.class);
        query.setParameter("ordonnanceId", ordonnanceId);
        return query.getResultList();
    }

    public List<Commande> getCommandesByPatientId(Long patientId) {
        TypedQuery<Commande> query = em.createQuery("SELECT c FROM Commande c WHERE c.patient.id = :patientId", Commande.class);
        query.setParameter("patientId", patientId);
        return query.getResultList();
    }

    public List<Commande> getCommandesByStatus(String status) {
        return em.createQuery("SELECT c FROM Commande c WHERE c.status = :status", Commande.class)
                .setParameter("status", status)
                .getResultList();
    }

    public Commande getCommandeStatus(Long patientId, Long commandeId) {
        TypedQuery<Commande> query = em.createQuery("SELECT c FROM Commande c WHERE c.id = :commandeId AND c.patient.id = :patientId", Commande.class);
        query.setParameter("commandeId", commandeId);
        query.setParameter("patientId", patientId);
        return query.getSingleResult();
    }

    public Commande updateCommande(Commande commande) {
        return em.merge(commande);
    }

    public void deleteCommande(Long id) {
        Commande existingCommande = em.find(Commande.class, id);
        if (existingCommande != null) {
            em.remove(existingCommande);
        }
    }
}