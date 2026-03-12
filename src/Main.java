import java.util.*;

/* Represents an optional service */
class AddOnService {

    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void displayService() {
        System.out.println(serviceName + " (₹" + cost + ")");
    }
}


/* Manages services linked to reservations */
class AddOnServiceManager {

    // Map<ReservationID, List of Services>
    private Map<String, List<AddOnService>> reservationServices = new HashMap<>();


    // Attach a service to a reservation
    public void addService(String reservationId, AddOnService service) {

        reservationServices
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println(service.getServiceName() +
                " added to reservation " + reservationId);
    }


    // Display services for a reservation
    public void displayServices(String reservationId) {

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId);

        for (AddOnService service : services) {
            service.displayService();
        }
    }


    // Calculate additional cost
    public double calculateTotalCost(String reservationId) {

        double total = 0;

        List<AddOnService> services = reservationServices.get(reservationId);

        if (services != null) {
            for (AddOnService service : services) {
                total += service.getCost();
            }
        }

        return total;
    }
}


/* Simple Reservation class */
class Reservation {

    private String reservationId;
    private String guestName;

    public Reservation(String reservationId, String guestName) {
        this.reservationId = reservationId;
        this.guestName = guestName;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void displayReservation() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest: " + guestName);
    }
}


/* Application Entry */
class HotelBookingApplication {

    public static void main(String[] args) {

        System.out.println("Hotel Booking System - Add-On Services\n");

        // Existing reservation
        Reservation reservation = new Reservation("R101", "Sachnoor");
        reservation.displayReservation();

        // Add-on service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Available services
        AddOnService breakfast = new AddOnService("Breakfast", 300);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 800);
        AddOnService spa = new AddOnService("Spa Access", 1500);

        // Guest selects services
        manager.addService(reservation.getReservationId(), breakfast);
        manager.addService(reservation.getReservationId(), spa);

        // Display selected services
        manager.displayServices(reservation.getReservationId());

        // Calculate additional cost
        double extraCost = manager.calculateTotalCost(reservation.getReservationId());

        System.out.println("\nTotal Add-On Cost: ₹" + extraCost);
    }
}