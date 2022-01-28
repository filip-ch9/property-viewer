$ ->
  $.get "/properties", (properties) ->
    $.each properties, (index, property) ->
      $("#properties").append $("<li>").text property.nameOfBuilding
      $("#properties").append $("<li>").text property.streetName
      $("#properties").append $("<li>").text property.streetNumber
      $("#properties").append $("<li>").text property.postalCode
      $("#properties").append $("<li>").text property.city
      $("#properties").append $("<li>").text property.country
      $("#properties").append $("<li>").text property.description