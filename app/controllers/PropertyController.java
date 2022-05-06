package controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.CustomException;
import models.Property;
import models.PropertyRepository;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.create;
import views.html.show;
import views.html.update;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

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

    public Result show() {
        return ok(show.render(propertyRepository.getAll()));
    }

    public Result deleteProperty(Long id) {
        propertyRepository.deleteById(id);
        return redirect(routes.PropertyController.show());
    }

    public Result create(final Http.Request request) {
        return ok(create.apply(request));
    }

    public Result update(Long id) {
        Property property = propertyRepository.getPropertyById(id);
        return ok(update.render(property));
    }

    public CompletionStage<Result> addProperty(final Http.Request request) {
        Property property = formFactory.form(Property.class).bindFromRequest(request).get();
        return propertyRepository
            .add(property)
            .thenApplyAsync(p -> redirect(routes.PropertyController.show()), ec.current());
    }

    public CompletionStage<Result> updateProperty(final Http.Request request) {
        Property property = formFactory.form(Property.class).bindFromRequest(request).get();
        return propertyRepository
            .update(property)
            .thenApplyAsync(p -> redirect(routes.PropertyController.show()), ec.current());
    }

    public CompletionStage<Result> addListOfProperties(final Http.Request request) {
        try {
            String json = request.body().asJson().toString();
            ObjectMapper objectMapper = new ObjectMapper();
            List<Property> properties = objectMapper.readValue(json, new TypeReference<List<Property>>() {});
            return propertyRepository
                .addAll(properties)
                .thenApplyAsync(p -> redirect(routes.PropertyController.index()), ec.current());
        } catch (Exception e){
            throw new CustomException("Unable to handle request body!", e);
        }
    }

    public CompletionStage<Result> getProperty() {
        return propertyRepository
            .list()
            .thenApplyAsync(propertyStream -> ok(toJson(propertyStream.collect(Collectors.toList()))), ec.current());
    }

}
