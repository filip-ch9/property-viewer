$ ->
  $.get "/properties", (properties) ->
    $.each properties, (index, property) ->
      $("#propertyId").append $("<tr><td></td></tr>").text property.id
      $("#propertyNameOfBuilding").append $("<tr><td></td></tr>").text property.nameOfBuilding
      $("#streetName").append $("<tr><td></td></tr>").text property.streetName
      $("#streetNumber").append $("<tr><td></td></tr>").text property.streetNumber
      $("#postalCode").append $("<tr><td></td></tr>").text property.postalCode
      $("#city").append $("<tr><td></td></tr>").text property.city
      $("#country").append $("<tr><td></td></tr>").text property.country
      $("#description").append $("<tr><td></td></tr>").text property.description
      $("#coordinates").append $("<tr><td></td></tr>").text property.coordinates