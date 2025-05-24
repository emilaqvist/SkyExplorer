// Initialize datepickers
$(document).ready(() => {
  $("#departure-date").datepicker({
    format: "yyyy-mm-dd",
    autoclose: true,
    todayHighlight: true,
    startDate: new Date(),
  })

  // default date today
  const today = new Date()
  const formattedDate =
    today.getFullYear() +
    "-" +
    String(today.getMonth() + 1).padStart(2, "0") +
    "-" +
    String(today.getDate()).padStart(2, "0")
  $("#departure-date").datepicker("setDate", today)

  // event listner for search botton
  $("#search-flights").on("click", searchFlightsAndWeather)
})

// Function to search flights and weather
async function searchFlightsAndWeather() {

  const departureCity = $("#departure-search").val().trim()
  const destinationCity = $("#destination-search").val().trim()
  const departureDate = $("#departure-date").val().trim()

  if (!departureCity || !destinationCity || !departureDate) {
    showAlert("Please fill in all required fields", "danger")
    return
  }

  // Show loading state
  showLoading(true)

  try {
    // Call backend API
    const url = `http://localhost:7000/search?from=${encodeURIComponent(departureCity)}&destination=${encodeURIComponent(destinationCity)}&departDate=${encodeURIComponent(departureDate)}`

    console.log("Searching flights & weather with URL: " + url)

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
  // Create results container
  if ($("#results-container").length === 0) {
    $(".cards").before(`
      <div id="results-container" class="container mt-4 mb-4">
        <h2 class="text-center mb-4">Search Results</h2>
        <div id="results-content"></div>
      </div>
    `)
  }

  let html = ""

  // Display flights
  html += `<h3 class="mt-4">Flights</h3>`
  if (Array.isArray(data.flights) && data.flights.length > 0) {
    html += `<div class="row">`
    data.flights.forEach((flight) => {
      // Format departure and arrival times / made a space between time and date for easier read
      const departureTime = flight.departureTime.replace("T", " Time: ")
      const arrivalTime = flight.arrivalTime.replace("T", " Time: ")

      // Create stop badge/ shows how many stops
      let stopBadge = ""
      if (flight.stops === 0) {
        stopBadge = '<span class="badge bg-success">Direct</span>'
      } else if (flight.stops === 1) {
        stopBadge = '<span class="badge bg-warning text-dark">1 Stop</span>'
      } else {
        stopBadge = `<span class="badge bg-danger">${flight.stops} Stops</span>`
      }

      // if there are stops
      let connectionsHtml = ""
      if (flight.stops > 0) {
        // if there are any stops
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
                <div>
                  <strong>Departure:</strong> ${departureTime}
                </div>
                <div>
                  <strong>Arrival:</strong> ${arrivalTime}
                </div>
                <div>
                  ${stopBadge}
                </div>
              </div>
              <div class="row mb-2">
                <div class="col-md-6">
                  <p><strong>From:</strong> ${flight.departureAirport}</p>
                </div>
                <div class="col-md-6">
                  <p><strong>To:</strong> ${flight.arrivalAirport}</p>
                </div>
              </div>
              <p><strong>Airline:</strong> ${flight.airline}</p>
              <p><strong>Duration:</strong> ${Math.floor(flight.duration / 60)}h ${flight.duration % 60}m</p>
              ${connectionsHtml}
              <div class="text-end">
                <span class="badge bg-success fs-6">${flight.formattedPrice}</span>
              </div>
            </div>
          </div>
        </div>
      `
    })
    html += `</div>`
  } else {
    html += `<div class="alert alert-warning">No flights found for this route and date.</div>`
  }

  // Display weather forecast with destination name
  html += `<h3 class="mt-4">Weather Forecast for ${data.flights[0]?.arrivalCity || destinationCity}</h3>`

  if (data.weather && data.weather.timelines && data.weather.timelines.hourly) {
    // Group weather data by day
    const dailyWeather = {}

    // Show daily weather
    data.weather.timelines.hourly.forEach((hour) => {
      const date = new Date(hour.time)
      const dateString = date.toLocaleDateString()

      if (!dailyWeather[dateString]) {
        dailyWeather[dateString] = {
          date: date,
          minTemp: hour.values.temperature,
          maxTemp: hour.values.temperature,
          weatherCodes: [hour.values.weatherCode],

        }
      } else {
        // Show, min max temprature
        dailyWeather[dateString].minTemp = Math.min(dailyWeather[dateString].minTemp, hour.values.temperature)
        dailyWeather[dateString].maxTemp = Math.max(dailyWeather[dateString].maxTemp, hour.values.temperature)
        dailyWeather[dateString].weatherCodes.push(hour.values.weatherCode)
      }
    })

    // Convert to array and take first 5 days
    const dailyForecast = Object.values(dailyWeather).slice(0, 5)
    html += `<div class="row">`

    dailyForecast.forEach((day) => {
      // Get most common weather code for the day
      const weatherCode = getMostFrequentValue(day.weatherCodes)
      const weatherInfo = getWeatherInfo(weatherCode)

      // Format date as day name
      const dayName = day.date.toLocaleDateString(undefined, { weekday: "short" })
      const dayMonth = day.date.toLocaleDateString(undefined, { day: "numeric", month: "short" })

      html += `
        <div class="col">
          <div class="card weather-card h-100 text-center">
            <div class="card-body">
              <h5>${dayName}</h5>
              <p class="text-muted small">${dayMonth}</p>
              <i class="bi ${weatherInfo.icon} ${weatherInfo.class} fs-1"></i>
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

  // Update results content
  $("#results-content").html(html)

  // Scroll to results
  $("html, body").animate(
    {
      scrollTop: $("#results-container").offset().top - 100,
    },
    500,
  )

  const destinationCity = data.flights[0].arrivalCity;

      const exploreButton = `
          <div class="text-center mt-4">
              <a href="attractions.html?city=${encodeURIComponent(destinationCity)}" class="btn btn-primary">
                  <i class="bi bi-map"></i> Utforska ${destinationCity}
              </a>
          </div>
      `;

      // Lägg till knappen efter flygresultaten
      $("#results-container").append(exploreButton);
}

// Helper function to get weather class based on weather code
function getWeatherInfo(weatherCode) {
  switch (weatherCode) {
    // Clear conditions
    case 1000: // Clear, Sunny
      return { description: "Clear/Sunny"}

    // Cloudy conditions
    case 1100: // Mostly Clear
      return { description: "Mostly Clear"}
    case 1101: // Partly Cloudy
      return { description: "Partly Cloudy"}
    case 1102: // Mostly Cloudy
      return { description: "Mostly Cloudy"}
    case 1001: // Cloudy
      return { description: "Cloudy"}

    // Rain conditions
    case 4000: // Drizzle
    case 4200: // Light Rain
      return { description: "Light Rain"}
    case 4001: // Rain
    case 4201: // Heavy Rain
      return { description: "Rain"}

    // Snow conditions
    case 5000: // Snow
    case 5100: // Light Snow
      return { description: "Snow"}
    case 5101: // Heavy Snow
      return { description: "Heavy Snow"}

    // Thunderstorm
    case 8000: // Thunderstorm
      return { description: "Thunderstorm"}

    // Default case
    default:
      return { description: "Unknown"}
  }
}


// Helper function to get most frequent value in array
function getMostFrequentValue(arr) {
  return arr.sort((a, b) => arr.filter((v) => v === a).length - arr.filter((v) => v === b).length).pop()
}

// Function to show/hide loading state/ show loading easy
function showLoading(isLoading) {
  if (isLoading) {
    $("#search-flights").html(
      '<span class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span> Searching...',
    )
    $("#search-flights").prop("disabled", true)
  } else {
    $("#search-flights").html("Search Flights")
    $("#search-flights").prop("disabled", false)
  }
}

// Function to show alerts, if we get an error and nothing happens maybe
function showAlert(message, type) {
  // Remove any existing alerts
  $(".alert-message").remove()

  // Create alert
  const alertHtml = `
    <div class="alert alert-${type} alert-dismissible fade show alert-message" role="alert">
      ${message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
  `

  // Insert alert before search section
  $(".search-field").before(alertHtml)

  // Auto dismiss after 5 seconds
  setTimeout(() => {
    $(".alert-message").fadeOut("slow", function () {
      $(this).remove()
    })
  }, 5000)
}

// Currency Converter
$(document).ready(function () {
  let fromCurrency = null;
  let toCurrency = null;
  const apiKey = "c2669164600c9ccacbd4cbb2";

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

