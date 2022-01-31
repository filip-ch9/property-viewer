package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Property;
import models.PropertyRepository;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.io.DataInput;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * The controller keeps all database operations behind the repository, and uses
 * {@link play.libs.concurrent.HttpExecutionContext} to provide access to the
 * {@link play.mvc.Http.Context} methods like {@code request()} and {@code flash()}.
 */
public class PropertyController extends Controller {

    private final FormFactory formFactory;
    private final PropertyRepository propertyRepository;
    private final HttpExecutionContext ec;

    @Inject
    public PropertyController(FormFactory formFactory, PropertyRepository propertyRepository, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.propertyRepository = propertyRepository;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request));
    }

    public CompletionStage<Result> addProperty(final Http.Request request) {
        Property property = formFactory.form(Property.class).bindFromRequest(request).get();
        return propertyRepository
                .add(property)
                .thenApplyAsync(p -> redirect(routes.PropertyController.index()), ec.current());
    }

    public CompletionStage<Result> addListOfProperties(final Http.RequestBody request) {
        List<Property> properties = new ArrayList<>();
        return propertyRepository
            .addAll(properties)
            .thenApplyAsync(p -> redirect(routes.PropertyController.index()), ec.current());
    }

    public CompletionStage<Result> getProperty() {
        return propertyRepository
                .list()
                .thenApplyAsync(propertyStream -> ok(toJson(propertyStream.collect(Collectors.toList()))), ec.current());
    }

    public Result deleteProperty(Long id) {
        return propertyRepository.deleteById(id) ? ok() : notFound();
    }

}
