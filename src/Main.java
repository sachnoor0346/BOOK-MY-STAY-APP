import java.util.*;

/* Reservation represents a booking request */
class Reservation {

    String guestName;
    String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}


/* Inventory Service */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 3);
        inventory.put("Double Room", 2);
        inventory.put("Suite Room", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        inventory.put(roomType, inventory.get(roomType) - 1);
    }
}


/* Booking Service - Handles allocation */
class BookingService {

    private Queue<Reservation> bookingQueue;
    private RoomInventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Track room assignments by type
    private Map<String, Set<String>> roomAllocations = new HashMap<>();


    public BookingService(Queue<Reservation> bookingQueue, RoomInventory inventory) {
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }


    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.poll();

            String roomType = request.roomType;

            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Prevent duplicate IDs
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    roomAllocations
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Decrement inventory
                    inventory.decrementRoom(roomType);

                    System.out.println("Reservation Confirmed");
                    System.out.println("Guest: " + request.guestName);
                    System.out.println("Room Type: " + roomType);
                    System.out.println("Assigned Room ID: " + roomId);
                    System.out.println("------------------------");

                }

            } else {
                System.out.println("Reservation Failed for " + request.guestName +
                        " (No " + roomType + " available)");
            }
        }
    }


    private String generateRoomId(String roomType) {

        String prefix = roomType.replace(" ", "").substring(0, 2).toUpperCase();
        int number = allocatedRoomIds.size() + 1;

        return prefix + number;
    }
}


/* Main Application */
class HotelBookingApplication {

    public static void main(String[] args) {

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Booking request queue (FIFO)
        Queue<Reservation> bookingQueue = new LinkedList<>();

        bookingQueue.add(new Reservation("Sachnoor", "Single Room"));
        bookingQueue.add(new Reservation("Aman", "Double Room"));
        bookingQueue.add(new Reservation("Riya", "Single Room"));
        bookingQueue.add(new Reservation("Karan", "Suite Room"));

        // Booking service
        BookingService service = new BookingService(bookingQueue, inventory);

        // Process bookings
        service.processBookings();
    }
}