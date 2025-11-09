package org.example.gschool.Dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.gschool.Entity.Filiere;
import org.example.gschool.Entity.ImageEntity;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Transactional
public class ImageDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(ImageEntity etudiant) {
        entityManager.persist(etudiant);
    }

    public ImageEntity findByCne(String cne) {
        return entityManager.find(ImageEntity.class, cne);
    }
    public List<ImageEntity> findAll() {
        return entityManager.createQuery("SELECT i FROM ImageEntity i", ImageEntity.class).getResultList();
    }
    public void delete(ImageEntity student) {
        entityManager.remove(student);
    }


    public List<ImageEntity> getEtudiantsP(int page, int size) {
        Session session = entityManager.unwrap(Session.class);
        Query<ImageEntity> query = session.createQuery("FROM ImageEntity", ImageEntity.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public List<ImageEntity> searchStudents(String cne, String nom, String prenom, String filiere, String sexe, String villeDenaissance) {
        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<ImageEntity> cq = cb.createQuery(ImageEntity.class);
        Root<ImageEntity> student = cq.from(ImageEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        if (cne != null && !cne.isEmpty()) {
            predicates.add(cb.equal(student.get("cne"), cne));
        }
        if (nom != null && !nom.isEmpty()) {
            predicates.add(cb.like(student.get("nom"), "%" + nom + "%"));
        }
        if (prenom != null && !prenom.isEmpty()) {
            predicates.add(cb.like(student.get("prenom"), "%" + prenom + "%"));
        }
        if (filiere != null && !filiere.isEmpty()) {
            predicates.add(cb.equal(student.get("filiere"), filiere));
        }
        if (sexe != null && !sexe.isEmpty()) {
            predicates.add(cb.equal(student.get("sexe"), sexe));
        }
        if (villeDenaissance != null && !villeDenaissance.isEmpty()) {
            predicates.add(cb.like(student.get("villeDenaissance"), "%" + villeDenaissance + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        return session.createQuery(cq).getResultList();
    }

    public Map<String, Long> countStudentsByFiliere() {
        Session session = entityManager.unwrap(Session.class);
        String hql = "SELECT f.filiere, COUNT(f) FROM ImageEntity f GROUP BY f.filiere";
        Query<Object[]> query = session.createQuery(hql, Object[].class);
        List<Object[]> results = query.getResultList();

        Map<String, Long> countMap = new HashMap<>();
        for (Object[] result : results) {
            String filiere = (String) result[0];
            Long count = (Long) result[1];
            countMap.put(filiere, count);
        }
        return countMap;
    }


}