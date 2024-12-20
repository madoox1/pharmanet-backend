package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Ordonnance;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

@Stateless
public class OrdonnanceEJBImpl {

    @PersistenceContext(unitName = "pharmacyPU")
    EntityManager em;

    public Ordonnance addOrdonnance(Ordonnance ordonnance) {
        em.persist(ordonnance);
        return ordonnance;
    }

    public Ordonnance findOrdonnanceById(Long id) {
        return em.find(Ordonnance.class, id);
    }

    public List<Ordonnance> getOrdonnancesByPatientId(Long patientId) {
        TypedQuery<Ordonnance> query = em.createQuery("SELECT o FROM Ordonnance o WHERE o.patient.id = :patientId", Ordonnance.class);
        query.setParameter("patientId", patientId);
        return query.getResultList();
    }

    public void deleteOrdonnance(Ordonnance ordonnance) {
        em.remove(em.merge(ordonnance));
    }

    public Ordonnance updateOrdonnance(Ordonnance ordonnance) {
        return em.merge(ordonnance);
    }
}
