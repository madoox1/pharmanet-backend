package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Pharmacie;
import com.pharmacy.model.PharmacieStatus;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PharmacieEJBImpl  {
    @PersistenceContext(unitName = "pharmacyPU")
    EntityManager em;



    public Pharmacie addPharmacie(Pharmacie pharmacie) {
        em.persist(pharmacie);
        return pharmacie;
    }


    public void updatePharmacie(Pharmacie pharmacie) {
        em.merge(pharmacie);
    }


    public void deletePharmacie(Pharmacie pharmacie) {
        em.remove(pharmacie);
    }


    public List<Pharmacie> listPharmacies() {
        return em.createQuery("SELECT p FROM Pharmacie p").getResultList();
    }


    public Pharmacie findPharmacieById(Long id) {
        return em.find(Pharmacie.class, id);
    }



    public Pharmacie findPharmacieByNomAndAdresse(String nom, String adresse) {
        try {
            return em.createQuery("SELECT p FROM Pharmacie p WHERE p.nom = :nom AND p.adresse = :adresse", Pharmacie.class)
                    .setParameter("nom", nom)
                    .setParameter("adresse", adresse)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    public Pharmacie changerStatusPharmacie(Long id) {
        Pharmacie pharmacie = findPharmacieById(id);
        pharmacie.setStatus(pharmacie.getStatus() == PharmacieStatus.Active ? PharmacieStatus.Inactive : PharmacieStatus.Active);
        return pharmacie;
    }


    public long countPharmacies() {
        try{
            return (long) em.createQuery("SELECT COUNT(p) FROM Pharmacie p").getSingleResult();
        }
        catch (Exception e){
            return 0;
        }
    }
}
