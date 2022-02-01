package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Property;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.HashMap;
import java.util.List;

public class PropertyAPIController extends Controller {

  public Result index() {
    List<Property> propertyList = Property.finder.all();
    return ok(Json.toJson(propertyList));
  }

  public Result save(final Http.Request request){
    JsonNode json = request.body().asJson();
    HashMap<String,Object> res = new HashMap<>();
    if(json == null){
      res.put("code","Bad Request");
      res.put("message","We Only Accept Json");
      return badRequest(Json.toJson(res));
    }
    Property property = Json.fromJson(json, Property.class);
    property.save();
    res.put("code","success");
    res.put("message","Property Saved Successfully.");
    return ok(Json.toJson(res));
  }

  public Result update(final Http.Request request, Long id){
    HashMap<String,Object> res = new HashMap<>();
    res.put("code","Bad Request");
    Property property = Property.finder.byId(id);
    if(property == null){
      res.put("message","Property does not exists");
      return badRequest(Json.toJson(res));
    }
    JsonNode json = request.body().asJson();
    if(json == null){
      res.put("message","We Only Accept Json");
      return badRequest(Json.toJson(res));
    }
    Property newProperty = Json.fromJson(json, Property.class);
    property.setNameOfBuilding(newProperty.getNameOfBuilding());
    property.setStreetName(newProperty.getStreetName());
    property.setStreetNumber(newProperty.getStreetNumber());
    property.setPostalCode(newProperty.getPostalCode());
    property.setCity(newProperty.getCity());
    property.setCountry(newProperty.getCountry());
    property.setDescription(newProperty.getDescription());
    property.update();
    res.put("code","success");
    res.put("message","Tag Updated Successfully.");
    return ok(Json.toJson(res));
  }

  public Result destroy(Long id){

    HashMap<String,Object> res = new HashMap<>();

    Property property = Property.finder.byId(id);

    if(property == null){
      res.put("code","Bad Request");
      res.put("message","Property does not exists");
      return badRequest(Json.toJson(res));
    }
    property.delete();

    res.put("code","success");
    res.put("message","Property deleted Successfully.");

    return ok(Json.toJson(res));
  }

}
