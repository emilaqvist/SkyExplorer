$(document).ready(function () {
    $('#departure-date, #return-date').datepicker({
        format: 'yyyy-mm-dd',
        autoclose: true,
        todayHighlight: true
    });

    $('#search-flights').on('click', function () {
        let departure = $('#departure-search').val().trim();
        let destination = $('#destination-search').val().trim();
        let departureDate = $('#departure-date').val();
        let returnDate = $('#return-date').val();

        if (!departure || !destination || !departureDate) {
            alert("Please fill in Departure, Destination, and Departure Date.");
            return;
        }

        let queryString = `?departure=${encodeURIComponent(departure)}&destination=${encodeURIComponent(destination)}&departureDate=${encodeURIComponent(departureDate)}&returnDate=${encodeURIComponent(returnDate)}`;
        window.location.href = "flight.html" + queryString;
    });
});
