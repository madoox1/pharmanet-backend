package com.pharmacy.service;

import com.pharmacy.Exceptions.NotFoundException;
import com.pharmacy.Exceptions.UnauthorizedException;
import com.pharmacy.ejb.Implimentation.UtilisateurEJB;
import com.pharmacy.model.Utilisateur;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.mindrot.jbcrypt.BCrypt; // Hachage du mot de passe

@Stateless
public class LoginService {

    @EJB
    private UtilisateurEJB utilisateurEJB; // Accès direct à la base de données

    // Authentification utilisateur
    public Utilisateur authenticate(String email, String motDePasse) throws UnauthorizedException, NotFoundException {

        // Rechercher l'utilisateur dans la base à partir de son email
        Utilisateur utilisateur = utilisateurEJB.findUserByEmail(email);

        if (utilisateur == null) {
            throw new NotFoundException("Utilisateur introuvable avec l'email fourni");
        }

        // Vérification du mot de passe avec BCrypt
        if (!BCrypt.checkpw(motDePasse, utilisateur.getMotDePasse())) {
            throw new UnauthorizedException("Email ou mot de passe invalide");
        }

        // Si validation réussie, générer un token JWT avec static JwtUtil
        return utilisateur;
    }
}