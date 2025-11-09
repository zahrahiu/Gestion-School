package org.example.gschool.Service;

import org.example.gschool.Entity.Filiere;
import java.util.List;

public interface FiliereService {

    List<Filiere> getAllFilieres();
    Filiere getFiliereById(int id);
    void saveFiliere(Filiere filiere);
    void supprimerFiliere(int id) ;



    List<Filiere> getFilieresPaginated(int page, int size);
    List<Filiere> searchFilieres(String nom, String code, String responsable);


}
