package models;

import com.google.inject.ImplementedBy;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

/**
 * This interface provides a non-blocking API for possibly blocking operations.
 */
@ImplementedBy(JPAPropertyRepository.class)
public interface PropertyRepository {

    CompletionStage<Property> add(Property property);

    CompletionStage<Stream<Property>> list();
}
