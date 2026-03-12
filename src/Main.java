import java.util.*;

/* Reservation class */
class Reservation {

    String reservationId;
    String guestName;
    String roomType;
    String roomId;
    boolean active;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.active = true;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType + " | RoomID: " + roomId + " | Active: " + active;
    }
}


/* Room Inventory */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 2);
        inventory.put("Double", 1);
    }

    public void increaseAvailability(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        inventory.put(roomType, count + 1);
    }

    public void displayInventory() {
        System.out.println("Inventory: " + inventory);
    }
}


/* Booking History */
class BookingHistory {

    Map<String, Reservation> bookings = new HashMap<>();

    public void addBooking(Reservation r) {
        bookings.put(r.reservationId, r);
    }

    public Reservation getReservation(String id) {
        return bookings.get(id);
    }

    public void displayBookings() {
        for (Reservation r : bookings.values()) {
            System.out.println(r);
        }
    }
}


/* Cancellation Service */
class CancellationService {

    private Stack<String> rollbackStack = new Stack<>();

    public void cancelBooking(String reservationId,
                              BookingHistory history,
                              RoomInventory inventory) {

        Reservation r = history.getReservation(reservationId);

        /* Validation */
        if (r == null) {
            System.out.println("Cancellation failed: Reservation does not exist.");
            return;
        }

        if (!r.active) {
            System.out.println("Cancellation failed: Reservation already cancelled.");
            return;
        }

        /* Record rollback info */
        rollbackStack.push(r.roomId);

        /* Restore inventory */
        inventory.increaseAvailability(r.roomType);

        /* Update reservation state */
        r.active = false;

        System.out.println("Booking cancelled successfully. Released RoomID: " + r.roomId);
    }

    public void showRollbackStack() {
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}


/* Main Application */
class HotelBookingApplication {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancelService = new CancellationService();

        /* Simulate confirmed bookings */
        Reservation r1 = new Reservation("R101", "Sachnoor", "Single", "S1");
        Reservation r2 = new Reservation("R102", "Aman", "Double", "D1");

        history.addBooking(r1);
        history.addBooking(r2);

        System.out.println("Before Cancellation:");
        history.displayBookings();
        inventory.displayInventory();

        /* Guest cancels booking */
        System.out.println("\nCancelling Reservation R101...");
        cancelService.cancelBooking("R101", history, inventory);

        System.out.println("\nAfter Cancellation:");
        history.displayBookings();
        inventory.displayInventory();

        cancelService.showRollbackStack();
    }
}