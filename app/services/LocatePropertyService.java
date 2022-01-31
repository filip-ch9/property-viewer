package services;

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

  private LocatePropertyService() {}

  public static String checkPropertyLocation(Property property) {
      OkHttpClient client = new OkHttpClient();
    String coordinates = null;
      try {
        HttpUrl url = Objects.requireNonNull(HttpUrl.parse("https://api.geoapify.com/v1/geocode/search")).newBuilder()
            .addQueryParameter("text", property.getStreetNumber() + " "
                + property.getStreetName() + " "
                + property.getCity() + " "
                + property.getPostalCode() + " "
                + property.getCountry() + " "
                + property.getNameOfBuilding() + " ")
            .addQueryParameter("apiKey", "ba81d0fc2ac141af97a44a41a8d7912f")
            .build();
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();

        if (response.code() == 200) {
          JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());

          JSONArray results = json.getJSONArray("features");
          JSONObject firstResult = results.getJSONObject(0);
          JSONObject firstResultProperties = firstResult.getJSONObject("properties");
          String type = firstResultProperties.getString("result_type");
          if (type.equals("building")){
            double longitude = firstResultProperties.getDouble("lon");
            double latitude = firstResultProperties.getDouble("lat");
            coordinates = "longitude: " + longitude + " latitude: " + latitude;
            property.setCoordinates(coordinates);
            firstResultProperties.getString("formatted"); // prints something like "10117 Berlin, Germany"
            return coordinates;
          }else {
            return null;
          }
        } else {
          return "Request error " + response.code() + Objects.requireNonNull(response.body()).string();
        }
      } catch (Exception e) {
        throw new CustomException("Something went wrong while parsing data from url!", e);
      }
  }
}
