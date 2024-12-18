package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Ordonnance;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;
@Stateless
public class OrdonnanceEJBImpl {

    @PersistenceContext(unitName = "pharmacyPU")
    EntityManager em;

    
    public Ordonnance updateOrdonnance(Ordonnance ordonnance) {
        em.merge(ordonnance);
        return ordonnance;
    }

    
    public void deleteOrdonnance(Ordonnance ordonnance) {
        em.remove(ordonnance);
    }

    
    public Ordonnance findOrdonnanceById(Long id) {
        return em.find(Ordonnance.class, id);
    }

    
    public Ordonnance addOrdonnance(Ordonnance ordonnance) {
        em.persist(ordonnance);
        return ordonnance;
    }

    
    public List<Ordonnance> getOrdonnancesByPatientId(Long id) {
        return em.createQuery("SELECT o FROM Ordonnance o WHERE o.patient.id = :id")
                .setParameter("id", id)
                .getResultList();
    }


}
