import java.io.*;
import java.util.*;

/* Reservation class */
class Reservation implements Serializable {

    String reservationId;
    String guestName;
    String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String toString() {
        return reservationId + " | " + guestName + " | " + roomType;
    }
}


/* Room Inventory */
class RoomInventory implements Serializable {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single", 5);
        inventory.put("Double", 3);
        inventory.put("Suite", 2);
    }

    public Map<String, Integer> getInventory() {
        return inventory;
    }

    public void setInventory(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}


/* Booking History */
class BookingHistory implements Serializable {

    private List<Reservation> bookings = new ArrayList<>();

    public void addBooking(Reservation r) {
        bookings.add(r);
    }

    public List<Reservation> getBookings() {
        return bookings;
    }

    public void displayBookings() {
        for (Reservation r : bookings) {
            System.out.println(r);
        }
    }
}


/* Persistence Service */
class PersistenceService {

    private static final String FILE_NAME = "hotel_state.dat";

    /* Save system state */
    public static void saveState(RoomInventory inventory, BookingHistory history) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(inventory);
            out.writeObject(history);

            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    /* Load system state */
    public static Object[] loadState() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();

            System.out.println("System state restored successfully.");

            return new Object[]{inventory, history};

        } catch (FileNotFoundException e) {

            System.out.println("No previous state found. Starting fresh.");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("State file corrupted. Starting with clean state.");

        }

        return null;
    }
}


/* Main Application */
class HotelBookingApplication {

    public static void main(String[] args) {

        RoomInventory inventory;
        BookingHistory history;

        /* Try loading persisted state */
        Object[] state = PersistenceService.loadState();

        if (state != null) {
            inventory = (RoomInventory) state[0];
            history = (BookingHistory) state[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        /* Simulate a booking */
        Reservation r1 = new Reservation("R101", "Sachnoor", "Single");
        history.addBooking(r1);

        System.out.println("\nBooking History:");
        history.displayBookings();

        System.out.println("\nInventory:");
        inventory.displayInventory();

        /* Save state before shutdown */
        PersistenceService.saveState(inventory, history);
    }
}