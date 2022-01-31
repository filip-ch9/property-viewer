package models;

import com.google.inject.ImplementedBy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAPropertyRepository.class)
public interface PropertyRepository {

    CompletionStage<Property> add(Property property);

    CompletionStage<List<Property>> addAll(List<Property> properties) ;

    CompletionStage<Stream<Property>> list();

    boolean deleteById(Long id);

    CompletableFuture<Optional<Property>> findProperty(Property property);
}
