package models;

import play.db.jpa.JPAApi;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
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
    public CompletionStage<Stream<Property>> list() {
        return supplyAsync(() -> wrap(em -> list(em)), executionContext);
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
}
