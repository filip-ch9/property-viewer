package models;

import exceptions.CustomException;
import play.db.jpa.JPAApi;
import services.LocatePropertyService;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 * Provide JPA operations running inside a thread pool sized to the connection pool
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
    public CompletionStage<Property> update(Property property) {
        return supplyAsync(() -> wrap(entityManager -> updateProperty(entityManager, property)), executionContext);
    }

    @Override
    public Property getPropertyById(Long id) {
        return getProperty(id);
    }

    @Override
    public List<Property> getAll() {
        return showAll();
    }

    @Override
    public boolean deleteById(Long id) {
        return wrap(entityManager -> deleteProperty(entityManager, id));
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private Property insert(EntityManager em, Property property) {
        LocatePropertyService.checkPropertyLocation(property);
        em.persist(property);
        return property;
    }

    private Property getProperty(Long id) {
        return jpaApi.withTransaction(
            (Function<EntityManager, Property>) entityManager -> entityManager.createQuery("select p from Property p where p.id = " + id, Property.class).getSingleResult());
    }

    private Property updateProperty(EntityManager entityManager, Property property){
        property = entityManager.find(Property.class, property.getId());
        if (property != null) {
            Property updateProperty = new Property();
            updateProperty.setNameOfBuilding(property.getNameOfBuilding());
            updateProperty.setStreetName(property.getStreetName());
            updateProperty.setStreetNumber(property.getStreetNumber());
            updateProperty.setPostalCode(property.getPostalCode());
            updateProperty.setCity(property.getCity());
            updateProperty.setCountry(property.getCountry());
            updateProperty.setDescription(property.getDescription());
            LocatePropertyService.checkPropertyLocation(property);
            entityManager.persist(property);
            return updateProperty;
        } else {
            throw new CustomException("Property does not exist!");
        }
    }

    private Stream<Property> list(EntityManager em) {
        List<Property> properties = em.createQuery("select p from Property p", Property.class).getResultList();
        return properties.stream();
    }

    private List<Property> showAll() {
        return jpaApi.withTransaction(
            (Function<EntityManager, List<Property>>) entityManager -> entityManager.createQuery("select p from Property p", Property.class).getResultList());
    }

    private List<Property> saveAll(EntityManager em, List<Property> properties) {
        properties.forEach(LocatePropertyService::checkPropertyLocation);
        properties.forEach(em::persist);
        return properties;
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

    private Stream<Property> listDescending(EntityManager entityManager) {
        List<Property> properties = entityManager.createQuery("select p from Property p order by id desc ", Property.class).getResultList();
        return properties.stream();
    }//TODO have one function that builds all the sorting functionality

    private Stream<Property> listByCity(EntityManager entityManager) {
        List<Property> properties = entityManager.createQuery("select p from Property p order by city", Property.class).getResultList();
        return properties.stream();
    }

    private Stream<Property> listByCountry(EntityManager entityManager) {
        List<Property> properties = entityManager.createQuery("select p from Property p order by country ", Property.class).getResultList();
        return properties.stream();
    }
}
