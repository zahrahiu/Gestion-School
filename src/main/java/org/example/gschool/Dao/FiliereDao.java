package org.example.gschool.Dao;

import org.example.gschool.Entity.Filiere;
import java.util.List;

public interface FiliereDao {
    List<Filiere> getFilieres();
    Filiere getFiliere(int id);
    void saveFiliere(Filiere filiere);
    void supprimerFiliere(int id);

    List<Filiere> getFilieresP(int page, int size);
    List<Filiere> searchFilieres(String nom, String code, String responsable);

}
