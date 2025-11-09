package org.example.gschool.Dao;

import jakarta.persistence.EntityManager;
import org.example.gschool.Entity.Module;
import org.example.gschool.Entity.Professeur;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ModuleDaoImpl implements ModuleDao {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void saveModule(Module module) {
        Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate(module);
    }

    @Override
    public List<Module> getModulesByFiliere(int filiereId) {
        Session session = entityManager.unwrap(Session.class);
        Query<Module> query = session.createQuery("from Module where filiere.id = :filiereId", Module.class);
        query.setParameter("filiereId", filiereId);
        return query.getResultList();
    }

    public List<Module> findAll() {
        return entityManager.createQuery("SELECT p FROM Module p", Module.class).getResultList();
    }
}
