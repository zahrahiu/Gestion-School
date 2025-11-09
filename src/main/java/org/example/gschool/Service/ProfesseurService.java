package org.example.gschool.Service;

import org.example.gschool.Entity.Professeur;

import java.util.List;

public interface ProfesseurService {
    void saveProfesseur(Professeur professeur);
    Professeur getProfesseurByCin(String cin);
    List<Professeur> getAllProfesseurs();
    void deleteProfesseurByCin(String cin);
    void updateProfesseur(String cin, Professeur updatedProfesseur);
    List<Professeur> searchProfesseurs(String cin, String nom, String prenom, String login);
}