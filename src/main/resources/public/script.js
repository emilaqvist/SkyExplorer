// Initialize datepicker
$(document).ready(() => {
  const departureInput = $("#departure-date");

  departureInput.datepicker({
    format: "yyyy-mm-dd",
    autoclose: true,
    todayHighlight: true,
    startDate: new Date()
  });

  departureInput.on("changeDate", function (e) {

    const dateString = e.format();
    departureInput.val(dateString);
    console.log("changeDate =>", dateString);
  });

  //debug
  $("#search-flights").on("click", function () {
    console.log("Vid klick: ", departureInput.val());
  });

  $("#search-flights").on("click", searchFlightsAndWeather);
});


// Function to search flights och weather
async function searchFlightsAndWeather() {
  const departureCity = $("#departure-search").val().trim()
  const destinationCity = $("#destination-search").val().trim()
  const departureDate = $("#departure-date").val().trim()
  console.log("Selected departure date:", departureDate)

  if (!departureCity || !destinationCity || !departureDate) {
    showAlert("Please fill in all required fields", "danger")
    return
  }

  showLoading(true)

  try {
    const url = `http://localhost:7000/api/v1/flights?from=${encodeURIComponent(departureCity)}&destination=${encodeURIComponent(destinationCity)}&departDate=${encodeURIComponent(departureDate)}`
    console.log("Searching flights & weather with URL:", url)

    const response = await fetch(url)
    const data = await response.json()

    if (!response.ok) {
      throw new Error(data.error || "Something went wrong")
    }

    displayResults(data)
  } catch (error) {
    console.error("Fel vid anrop till backend:", error)
    showAlert("Fel: " + error.message)
  } finally {
    showLoading(false)
  }
}

// Function to display results
function displayResults(data) {
  if ($("#results-container").length === 0) {
    $(".cards").before(`
      <div id="results-container" class="container mt-4 mb-4">
        <h2 class="text-center mb-4">Search Results</h2>
        <div id="results-content"></div>
      </div>
    `)
  }

  let html = ""
  const userDestination = $("#destination-search").val().trim();


  if (data.searchInfo) {
    html += `
      <div class="alert alert-info">
        <strong>Search details:</strong> 
        ${data.searchInfo.departureCity} to ${data.searchInfo.destinationCity} 
        on ${data.searchInfo.departureDate}
      </div>
    `
  }
  // Flights
  html += `<h3 class="mt-4">Flights</h3>`
  if (Array.isArray(data.flights) && data.flights.length > 0) {
    html += `<div class="row">`
    data.flights.forEach((flight) => {
      const departure = flight.departure.replace("T", " Time: ");
      const arrival = flight.arrival.replace("T", " Time: ");

      let stopBadge = ""
      if (flight.stops === 0) {
        stopBadge = '<span class="badge bg-success">Direct</span>'
      } else if (flight.stops === 1) {
        stopBadge = '<span class="badge bg-warning text-dark">1 Stop</span>'
      } else {
        stopBadge = `<span class="badge bg-danger">${flight.stops} Stops</span>`
      }

      let connectionsHtml = ""
      if (flight.stops > 0) {
        connectionsHtml = `
          <div class="connections mt-2 mb-2">
            <p class="mb-1"><strong>Stops:</strong> ${flight.stops} ${flight.stops === 1 ? 'stop' : 'stops'}</p>
          </div>
        `
      }

      html += `
        <div class="col-md-6 mb-3">
          <div class="card flight-card h-100">
            <div class="card-header bg-primary text-white">
              <div class="d-flex justify-content-between align-items-center">
                <h5 class="card-title mb-0">${flight.departureCity} → ${flight.arrivalCity}</h5>
              </div>
            </div>
            <div class="card-body">
              <div class="d-flex justify-content-between mb-2 align-items-center">
                <div><strong>Departure:</strong> ${departure}</div>
                <div><strong>Arrival:</strong> ${arrival}</div>
                <div>${stopBadge}</div>
              </div>
              <div class="row mb-2">
                <div class="col-md-6"><p><strong>From:</strong> ${flight.departureAirport}</p></div>
                <div class="col-md-6"><p><strong>To:</strong> ${flight.arrivalAirport}</p></div>
              </div>
              <p><strong>Airline:</strong> ${flight.airline}</p>
              <p><strong>Duration:</strong> ${Math.floor(flight.duration / 60)}h ${flight.duration % 60}m</p>
              
              ${connectionsHtml}
              ${flight.segments && flight.segments.length > 1 ? `
                 <div class="flight-segments mt-2">
                 <strong>Flight Segments:</strong>
                    <ul class="list-unstyled">
                      ${flight.segments.map(seg => `
                      <li>
                         ${seg.from} → ${seg.to} (${seg.departureTime} - ${seg.arrivalTime}) – ${seg.airline} ${seg.flightNumber}
                         </li>
                  `).join('')}
                     </ul>
                    </div>
                    ` : ''}

<div class="text-end"><span class="badge bg-success fs-6">${flight.formattedPrice}</span></div>
            </div>
          </div>
        </div>
      `
    })
    html += `</div>`
  } else {
    html += `<div class="alert alert-warning">No flights found for this route and date.</div>`
  }

  // Weather
  html += `<h3 class="mt-4">Weather Forecast for ${userDestination}</h3>`

  if (data.weathers?.timelines?.hourly) {
    const dailyWeather = {}

    data.weathers.timelines.hourly.forEach((hour) => {
      const date = new Date(hour.time)
      const dateString = date.toLocaleDateString()

      if (!dailyWeather[dateString]) {
        dailyWeather[dateString] = {
          date,
          minTemp: hour.values.temperature,
          maxTemp: hour.values.temperature,
          weatherCodes: [hour.values.weatherCode]
        }
      } else {
        dailyWeather[dateString].minTemp = Math.min(dailyWeather[dateString].minTemp, hour.values.temperature)
        dailyWeather[dateString].maxTemp = Math.max(dailyWeather[dateString].maxTemp, hour.values.temperature)
        dailyWeather[dateString].weatherCodes.push(hour.values.weatherCode)
      }
    })

    const dailyForecast = Object.values(dailyWeather).slice(0, 5)
    html += `<div class="row">`

    dailyForecast.forEach((day) => {
      const weatherCode = getMostFrequentValue(day.weatherCodes)
      const weatherInfo = getWeatherInfo(weatherCode)
      const dayName = day.date.toLocaleDateString(undefined, { weekday: "short" })
      const dayMonth = day.date.toLocaleDateString(undefined, { day: "numeric", month: "short" })

      html += `
        <div class="col">
          <div class="card weather-card h-100 text-center">
            <div class="card-body">
              <h5>${dayName}</h5>
              <p class="text-muted small">${dayMonth}</p>
              <h4>${Math.round(day.maxTemp)}°C</h4>
              <p class="text-muted small">Min: ${Math.round(day.minTemp)}°C</p>
              <p class="mb-0">${weatherInfo.description}</p>
            </div>
          </div>
        </div>
      `
    })

    html += `</div>`
  } else {
    html += `<div class="alert alert-warning">No weather data available for the destination.</div>`
  }

  $("#results-content").html(html)

  $("html, body").animate(
      { scrollTop: $("#results-container").offset().top - 100 },
      500
  )

  $("#explore-button-container").remove(); // om det finns en gammal, ta bort det frst

  const exploreButton = `
  <div id="explore-button-container" class="text-center mt-4">
    <a href="attractions.html?city=${encodeURIComponent(userDestination)}" class="btn btn-primary">
      <i class="bi bi-map"></i> Utforska ${userDestination}
    </a>
     <a href="cityinfo.html?city=${encodeURIComponent(userDestination)}" class="btn btn-secondary">
        <i class="bi bi-info-circle"></i> Om ${userDestination} 
     </a>
  </div>
`

  $("#results-container").append(exploreButton)
}

// Helper: most frequent weather code
function getMostFrequentValue(arr) {
  return arr.sort((a, b) =>
      arr.filter(v => v === a).length - arr.filter(v => v === b).length
  ).pop()
}

// Helper: map weather code to description
function getWeatherInfo(code) {
  switch (code) {
    case 1000: return { description: "Clear/Sunny" }
    case 1100: return { description: "Mostly Clear" }
    case 1101: return { description: "Partly Cloudy" }
    case 1102: return { description: "Mostly Cloudy" }
    case 1001: return { description: "Cloudy" }
    case 4000:
    case 4200: return { description: "Light Rain" }
    case 4001:
    case 4201: return { description: "Rain" }
    case 5000:
    case 5100: return { description: "Snow" }
    case 5101: return { description: "Heavy Snow" }
    case 8000: return { description: "Thunderstorm" }
    default: return { description: "Unknown" }
  }
}

// Show or hide loading
function showLoading(isLoading) {
  if (isLoading) {
    $("#search-flights").html(
        '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Searching...'
    )
    $("#search-flights").prop("disabled", true)
  } else {
    $("#search-flights").html("Search Flights")
    $("#search-flights").prop("disabled", false)
  }
}

// Show alert
function showAlert(message, type = "danger") {
  $(".alert-message").remove()

  const alertHtml = `
    <div class="alert alert-${type} alert-dismissible fade show alert-message" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  `

  $(".search-field").before(alertHtml)

  setTimeout(() => {
    $(".alert-message").fadeOut("slow", function () {
      $(this).remove()
    })
  }, 5000)
}

// Currency Converter
// Allt om Currency är skriven av Algot
$(document).ready(function () {
  let fromCurrency = null;
  let toCurrency = null;
  const apiKey = CONFIG.CURRENCY_API_KEY;

  $(".from-currency").on("click", function () {
    fromCurrency = $(this).data("currency");
    $(".from-currency").removeClass("active");
    $(this).addClass("active");
    tryConvert();
  });

  $(".to-currency").on("click", function () {
    toCurrency = $(this).data("currency");
    $(".to-currency").removeClass("active");
    $(this).addClass("active");
    tryConvert();
  });

  $("#amount").on("input", function () {
    tryConvert();
  });

  function tryConvert() {
    const amount = $("#amount").val();

    if (!fromCurrency || !toCurrency || !amount || isNaN(amount)) {
      $("#conversion-result").val("");
      return;
    }

    $.ajax({
      url: `https://v6.exchangerate-api.com/v6/${apiKey}/pair/${fromCurrency}/${toCurrency}/${amount}`,
      method: "GET",
      dataType: "json",
    })
        .done(function (data) {
          if (data.result === "success") {
            $("#conversion-result").val(data.conversion_result.toFixed(2));
          } else {
            $("#conversion-result").val("Error");
          }
        })
        .fail(function () {
          $("#conversion-result").val("API error");
        });
  }
});

