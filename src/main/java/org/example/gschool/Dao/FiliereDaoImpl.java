package org.example.gschool.Dao;

import jakarta.persistence.EntityManager;
import org.example.gschool.Entity.Filiere;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FiliereDaoImpl implements FiliereDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Filiere> getFilieres() {
        Session session = entityManager.unwrap(Session.class);
        Query<Filiere> query = session.createQuery("from Filiere", Filiere.class);
        return query.getResultList();
    }

    @Override
    public Filiere getFiliere(int id) {
        Session session = entityManager.unwrap(Session.class);
        return session.get(Filiere.class, id);
    }

    @Override
    public void saveFiliere(Filiere filiere) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(filiere);
    }

    @Override
    public void supprimerFiliere(int id) {
        Session session = entityManager.unwrap(Session.class);
        Filiere filiere = session.get(Filiere.class, id);
        if (filiere != null) {
            session.delete(filiere);
        } else {
            System.out.println("Filière non trouvée avec ID: " + id);
        }
    }



    @Override
    public List<Filiere> getFilieresP(int page, int size) {
        Session session = entityManager.unwrap(Session.class);
        Query<Filiere> query = session.createQuery("from Filiere", Filiere.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public List<Filiere> searchFilieres(String nom, String code, String responsable) {
        Session session = entityManager.unwrap(Session.class);
        StringBuilder hql = new StringBuilder("from Filiere where 1=1");

        if (nom != null && !nom.trim().isEmpty()) {
            hql.append(" and lower(nom) like :nom");
        }
        if (code != null && !code.trim().isEmpty()) {
            hql.append(" and lower(code) like :code");
        }
        if (responsable != null && !responsable.trim().isEmpty()) {
            hql.append(" and lower(responsable) like :responsable");
        }

        Query<Filiere> query = session.createQuery(hql.toString(), Filiere.class);

        if (nom != null && !nom.trim().isEmpty()) {
            query.setParameter("nom", "%" + nom.toLowerCase() + "%");
        }
        if (code != null && !code.trim().isEmpty()) {
            query.setParameter("code", "%" + code.toLowerCase() + "%");
        }
        if (responsable != null && !responsable.trim().isEmpty()) {
            query.setParameter("responsable", "%" + responsable.toLowerCase() + "%");
        }

        return query.getResultList();
    }


}
