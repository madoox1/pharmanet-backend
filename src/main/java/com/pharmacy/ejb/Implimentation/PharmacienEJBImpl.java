package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Pharmacien;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
public class PharmacienEJBImpl {

    @PersistenceContext(unitName = "pharmacyPU")
    private EntityManager em;

    // Ajouter un pharmacien
    public Pharmacien addPharmacien(Pharmacien pharmacien) {
        em.persist(pharmacien);
        return pharmacien;
    }

    // Trouver un pharmacien par ID
    public Pharmacien findPharmacienById(Long id) {
        return em.find(Pharmacien.class, id);
    }

    // Récupérer tous les pharmaciens
    public List<Pharmacien> getAllPharmaciens() {
        return em.createQuery("SELECT p FROM Pharmacien p", Pharmacien.class)
                .getResultList();
    }

    // Rechercher des pharmaciens par nom (similaire à la recherche des patients par nom)
    public List<Pharmacien> getPharmaciensByName(String name) {
        try{
            return em.createQuery("SELECT p FROM Pharmacien p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :name, '%'))", Pharmacien.class)
                    .setParameter("name", name)
                    .getResultList();
        }
        catch (Exception e){
            return null;
        }
    }

    // Mettre à jour un pharmacien
    public Pharmacien updatePharmacien(Pharmacien pharmacien) {
        return em.merge(pharmacien);
    }

    // Supprimer un pharmacien par ID
    public void deletePharmacien(Long id) {
        Pharmacien existingPharmacien = em.find(Pharmacien.class, id);
        if (existingPharmacien != null) {
            em.remove(existingPharmacien);
        }
    }

    // Trouver un pharmacien par email
    public Pharmacien findPharmacienByEmail(String email) {
        List<Pharmacien> pharmaciens = em.createQuery("SELECT p FROM Pharmacien p WHERE p.email = :email", Pharmacien.class)
                .setParameter("email", email)
                .getResultList();

        return pharmaciens.isEmpty() ? null : pharmaciens.get(0);
    }

    public Long countPharmaciens() {
        try {
            return em.createQuery("SELECT COUNT(p) FROM Pharmacien p", Long.class)
                    .getSingleResult();
        } catch (Exception e) {
            return 0L;
        }
    }
}