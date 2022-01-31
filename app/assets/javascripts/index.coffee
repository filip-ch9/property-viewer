$ ->
  $.get "/properties", (properties) ->
    $.each properties, (index, property) ->
      $("#properties").append $("<td>").text property.id
      $("#properties").append $("<td>").text property.nameOfBuilding
      $("#properties").append $("<td>").text property.streetName
      $("#properties").append $("<td>").text property.streetNumber
      $("#properties").append $("<td>").text property.postalCode
      $("#properties").append $("<td>").text property.city
      $("#properties").append $("<td>").text property.country
      $("#properties").append $("<td>").text property.description
      $("#properties").append $("<td>").text property.coordinates