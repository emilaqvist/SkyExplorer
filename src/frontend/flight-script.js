$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    let departure = urlParams.get("departure");
    let destination = urlParams.get("destination");
    let departureDate = urlParams.get("departureDate");
    let returnDate = urlParams.get("returnDate");

    if (!departure) {
        $('#flights-results').html('<p>Error: Departure location is missing.</p>');
        return;
    }

    $('#destination-title').text(`Flights to ${destination}`);

    $.getJSON("/src/frontend/flights.json", function (flightsData) {
        let filteredFlights = flightsData.filter(flight =>
            flight.departure.toLowerCase().includes(departure.toLowerCase()) &&
            flight.destination.toLowerCase().includes(destination.toLowerCase()) &&
            (!departureDate || flight.departureDate === departureDate) &&
            (!returnDate || flight.returnDate === returnDate || flight.returnDate === "")
        );

        displayFlights(filteredFlights);
    });

    fetchWeather(destination);

    fetchAttractions(destination);
});

function displayFlights(flights) {
    let flightsContainer = $('#flights-results');
    flightsContainer.empty();

    if (flights.length === 0) {
        flightsContainer.append('<p>No flights found.</p>');
        return;
    }

    flights.forEach(flight => {
        flightsContainer.append(`
            <div class="card shadow-sm p-3 mb-3">
                <div class="card-body">
                    <h5 class="card-title">${flight.departure} → ${flight.destination}</h5>
                    <p><strong>Departure:</strong> ${flight.departureDate}</p>
                    <p><strong>Return:</strong> ${flight.returnDate || 'One-way'}</p>
                    <p><strong>Price:</strong> ${flight.price} ${flight.currency}</p>
                </div>
            </div>
        `);
    });
}

function fetchWeather(city) {
    let weatherAPI = `https://your-weather-api.com/search?city=${city}`;

    $.getJSON(weatherAPI, function (data) {
        $('#weather-info').html(`
            <h3>Weather in ${city}</h3>
            <p>Temperature: ${data.main.temp}°C</p>
            <p>Condition: ${data.weather[0].description}</p>
        `);
    }).fail(function () {
        $('#weather-info').html('<p>Weather data not available.</p>');
    });
}

function fetchAttractions(city) {
    let attractionsAPI = `https://your-attractions-api.com/search?city=${city}`;

    $.getJSON(attractionsAPI, function (data) {
        let attractionsContainer = $('#attractions-info');
        attractionsContainer.empty();

        if (data.length === 0) {
            attractionsContainer.append('<p>No attractions found.</p>');
            return;
        }

        attractionsContainer.append('<h3>Top Attractions</h3>');
        data.forEach(attraction => {
            attractionsContainer.append(`<p>${attraction.name} - ${attraction.description}</p>`);
        });
    }).fail(function () {
        $('#attractions-info').html('<p>Attractions data not available.</p>');
    });
}