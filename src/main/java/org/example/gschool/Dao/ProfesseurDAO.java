package org.example.gschool.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.gschool.Entity.Professeur;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class ProfesseurDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(Professeur professeur) {
        entityManager.persist(professeur);
    }

    public Professeur findByCin(String cin) {
        return entityManager.find(Professeur.class, cin);
    }

    public List<Professeur> findAll() {
        return entityManager.createQuery("SELECT p FROM Professeur p", Professeur.class).getResultList();
    }

    public void delete(Professeur professeur) {
        entityManager.remove(professeur);
    }

    public List<Professeur> searchProfesseurs(String cin, String nom, String prenom, String login) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Professeur> cq = cb.createQuery(Professeur.class);
        Root<Professeur> professeur = cq.from(Professeur.class);

        List<Predicate> predicates = new ArrayList<>();

        if (cin != null && !cin.isEmpty()) {
            predicates.add(cb.equal(professeur.get("cin"), cin));
        }
        if (nom != null && !nom.isEmpty()) {
            predicates.add(cb.like(professeur.get("nom"), "%" + nom + "%"));
        }
        if (prenom != null && !prenom.isEmpty()) {
            predicates.add(cb.like(professeur.get("prenom"), "%" + prenom + "%"));
        }
        if (login != null && !login.isEmpty()) {
            predicates.add(cb.like(professeur.get("login"), "%" + login + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cq).getResultList();
    }
}