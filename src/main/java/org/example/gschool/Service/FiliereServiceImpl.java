package org.example.gschool.Service;

import org.example.gschool.Dao.FiliereDao;
import org.example.gschool.Entity.Filiere;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FiliereServiceImpl implements FiliereService {

    @Autowired
    private FiliereDao filiereDao;

    @Override
    @Transactional
    public List<Filiere> getAllFilieres() {
        return filiereDao.getFilieres();
    }

    @Override
    @Transactional
    public Filiere getFiliereById(int id) {
        return filiereDao.getFiliere(id);
    }

    @Override
    @Transactional
    public void saveFiliere(Filiere filiere) {

        filiereDao.saveFiliere(filiere);
    }

    @Override
    @Transactional
    public void supprimerFiliere(int id) {
        filiereDao.supprimerFiliere(id);
    }



    @Override
    public List<Filiere> getFilieresPaginated(int page, int size) {
        return filiereDao.getFilieresP(page, size);
    }

    @Override
    @Transactional
    public List<Filiere> searchFilieres(String nom, String code, String responsable) {
        if ((nom == null || nom.isEmpty()) && (code == null || code.isEmpty()) && (responsable == null || responsable.isEmpty())) {
            return getAllFilieres(); // Si aucun filtre, retourner tout.
        }
        return filiereDao.searchFilieres(nom, code, responsable);
    }


}
