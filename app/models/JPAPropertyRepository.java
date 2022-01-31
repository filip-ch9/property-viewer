package models;

import play.db.jpa.JPAApi;
import services.LocatePropertyService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside of a thread pool sized to the connection pool
 */
public class JPAPropertyRepository implements PropertyRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAPropertyRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Property> add(Property property) {
        return supplyAsync(() -> wrap(em -> insert(em, property)), executionContext);
    }

    @Override
    public CompletionStage<List<Property>> addAll(List<Property> properties) {
        return supplyAsync(() -> wrap(em -> saveAll(em, properties)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Property>> list() {
        return supplyAsync(() -> wrap(this::list), executionContext);
    }

    @Override
    public boolean deleteById(Long id) {
        return wrap(entityManager -> deleteProperty(entityManager, id));
    }

    @Override
    public CompletableFuture<Optional<Property>> findProperty(Property property) {
        return supplyAsync(() -> wrap(entityManager -> findProperty(entityManager, property)), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Property insert(EntityManager em, Property property) {
        em.persist(property);
        return property;
    }

    private Stream<Property> list(EntityManager em) {
        List<Property> properties = em.createQuery("select p from Property p", Property.class).getResultList();
        return properties.stream();
    }

    private List<Property> saveAll(EntityManager em, List<Property> properties) {
        properties.forEach(LocatePropertyService::checkPropertyLocation);
        properties.forEach(em::persist);
        return properties;
    }

    private Optional<Property> findProperty(EntityManager entityManager, Property property) {
        if (entityManager.contains(property)) {
            return Optional.ofNullable(property);
        }
        return Optional.empty();
    }

    private boolean deleteProperty(EntityManager entityManager, Long id) {
        Property property = entityManager.find(Property.class, id);
        if (property != null) {
            entityManager.remove(property);
            return true;
        }else {
            return false;
        }
    }
}
