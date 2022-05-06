package services;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import exceptions.CustomException;
import models.Property;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

public class LocatePropertyService {

  private static final Config config = ConfigFactory.load();

  private LocatePropertyService() {}

  public static void checkPropertyLocation(Property property) {
      try {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://api.geoapify.com/v1/geocode/search")).newBuilder()
            .addQueryParameter("text", property.getStreetNumber() + " "
                + property.getStreetName() + " "
                + property.getCity() + " "
                + property.getPostalCode() + " "
                + property.getCountry() + " ")
            .addQueryParameter("apiKey", config.getString("geoApifyKey.key"))
            .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
        setCoordinatesFromJson(property, response, json);
      } catch (Exception e) {
        throw new CustomException("Something went wrong while parsing data from url!", e);
      }
  }

  private static void setCoordinatesFromJson(Property property, Response response, JSONObject json) {
    if (response.code() == 200) {
      JSONArray results = json.getJSONArray("features");
      JSONObject firstResult = results.getJSONObject(0);
      JSONObject firstResultProperties = firstResult.getJSONObject("properties");
      String type = firstResultProperties.getString("result_type");
      if (type.equals("building")){
        double longitude = firstResultProperties.getDouble("lon");
        double latitude = firstResultProperties.getDouble("lat");
        String coordinates = "longitude: " + longitude + " latitude: " + latitude;
        property.setCoordinates(coordinates);
      }else {
        property.setCoordinates("Unable to find coordinates!");
      }
    }
  }
}
