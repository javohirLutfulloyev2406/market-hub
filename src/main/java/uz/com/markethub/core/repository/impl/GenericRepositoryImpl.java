package uz.com.markethub.core.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import uz.com.markethub.core.repository.GenericRepository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GenericRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements GenericRepository<T, ID> {

    private final EntityManager em;
    private final Class<T> domainClass;

    public GenericRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager em) {
        super(entityInformation, em);
        this.em = em;
        this.domainClass = entityInformation.getJavaType();
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + domainClass.getSimpleName() + " e WHERE e.deleted = false", domainClass).getResultList();
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<T> cq = cb.createQuery(domainClass);
        Root<T> root = cq.from(domainClass);

        Predicate deletedFalse = cb.isFalse(root.get("deleted"));
        Predicate specPredicate = (spec == null) ? cb.conjunction() : spec.toPredicate(root, cq, cb);
        cq.where(cb.and(deletedFalse, specPredicate));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            for (Sort.Order order : pageable.getSort()) {
                Path<Object> path = root.get(order.getProperty());
                orders.add(order.isAscending() ? cb.asc(path) : cb.desc(path));
            }
            cq.orderBy(orders);
        }

        List<T> resultList = em.createQuery(cq)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> countRoot = countQuery.from(domainClass);
        Predicate countPredicate = cb.and(
                cb.isFalse(countRoot.get("deleted")),
                (spec == null) ? cb.conjunction() : spec.toPredicate(countRoot, countQuery, cb)
        );

        countQuery.select(cb.count(countRoot)).where(countPredicate);
        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, total);
    }


    @Override
    public Optional<T> findById(ID id) {
        List<T> result = em.createQuery("SELECT e FROM " + domainClass.getSimpleName() + " e WHERE e.deleted = false AND e.id = :id", domainClass).setParameter("id", id).getResultList();
        return result.stream().findFirst();
    }

    @Override
    @Transactional
    public void deleteById(ID id) {
        em.createQuery("UPDATE " + domainClass.getSimpleName() + " e SET e.deleted = true WHERE e.id = :id").setParameter("id", id).executeUpdate();
    }

    @Override
    public boolean existsById(ID id) {
        Long count = em.createQuery("SELECT COUNT(e) FROM " + domainClass.getSimpleName() + " e WHERE e.deleted = false AND e.id = :id", Long.class).setParameter("id", id).getSingleResult();
        return count > 0;
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(e) FROM " + domainClass.getSimpleName() + " e WHERE e.deleted = false", Long.class).getSingleResult();
    }
}
