package controller;

/*import io.javalin.Javalin;
import service.FlightService;

public class FlightController {

    //private static FlightService flightService = new FlightService();
    public static void registerRoutes(Javalin app) {
        app.get("/flights",context -> {
            String departureDate = context.queryParam("departureDate");
            String returnDate = context.queryParam("returnDate");
            String from = context.queryParam("from");
            String destination = context.queryParam("destination");

            if (departureDate == null || returnDate == null || from == null || destination == null
                    || departureDate.isEmpty() || returnDate.isEmpty()
                    || from.isEmpty() || destination.isEmpty()){
                context.status(400).result("{\"error\":\"Saknar obligatoriska parametrar\"}");
                return;
            }

            // String flightsJson = flightService.searchFlights(from,destination,departureDate,returnDate);
            // context.result(flightsJson);
        });

    }
}
*/

//används inte längre