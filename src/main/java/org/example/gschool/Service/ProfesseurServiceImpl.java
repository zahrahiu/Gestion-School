package org.example.gschool.Service;

import org.example.gschool.Dao.ProfesseurDAO;
import org.example.gschool.Entity.ImageEntity;
import org.example.gschool.Entity.Professeur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfesseurServiceImpl implements ProfesseurService {

    @Autowired
    private ProfesseurDAO professeurDAO;

    @Override
    public void saveProfesseur(Professeur professeur) {
        professeurDAO.save(professeur);
    }

    @Override
    public Professeur getProfesseurByCin(String cin) {
        return professeurDAO.findByCin(cin);
    }



    @Override
    public List<Professeur> getAllProfesseurs() {
        return professeurDAO.findAll();
    }

    @Override
    @Transactional
    public void deleteProfesseurByCin(String cin) {
        Professeur professeur = professeurDAO.findByCin(cin);
        if (professeur != null) {
            professeurDAO.delete(professeur);
        }
    }

    @Override
    @Transactional
    public void updateProfesseur(String cin, Professeur updatedProfesseur) {
        Professeur professeur = professeurDAO.findByCin(cin);
        if (professeur != null) {
            professeur.setNom(updatedProfesseur.getNom());
            professeur.setPrenom(updatedProfesseur.getPrenom());
            professeur.setAdresse(updatedProfesseur.getAdresse());
            professeur.setTelephone(updatedProfesseur.getTelephone());
            professeur.setEmail(updatedProfesseur.getEmail());
            professeur.setVille(updatedProfesseur.getVille());
            professeur.setImage(updatedProfesseur.getImage()); // Mettre à jour l'image
            professeur.setLogin(updatedProfesseur.getLogin());
            professeur.setPassword(updatedProfesseur.getPassword());
            professeurDAO.save(professeur);
        }
    }

    @Override
    public List<Professeur> searchProfesseurs(String cin, String nom, String prenom, String login) {
        return professeurDAO.searchProfesseurs(cin, nom, prenom, login);
    }
}