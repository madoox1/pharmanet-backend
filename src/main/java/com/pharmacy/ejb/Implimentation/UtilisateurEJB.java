package com.pharmacy.ejb.Implimentation;

import com.pharmacy.model.Utilisateur;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Stateless
@LocalBean
public class UtilisateurEJB {

    @PersistenceContext(unitName = "pharmacyPU")
    EntityManager em;

    // Recherche utilisateur par email
    public Utilisateur findUserByEmail(String email) {
        try {
            return em.createQuery("SELECT u FROM Utilisateur u WHERE u.email = :email", Utilisateur.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; // Retourne null si aucun utilisateur n'est trouvé
        }
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getAllUsers() {
        return em.createQuery("SELECT u FROM Utilisateur u", Utilisateur.class).getResultList();
    }

    // Ajouter ou sauvegarder un utilisateur
    public Utilisateur save(Utilisateur utilisateur) {
        em.persist(utilisateur);
        return utilisateur;
    }

    // Recherche utilisateur par ID
    public Utilisateur findUserById(Long id) {
        return em.find(Utilisateur.class, id);
    }
}