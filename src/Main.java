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


/* Shared Inventory */
class RoomInventory {

    private Map<String, Integer> inventory = new HashMap<>();

    public RoomInventory() {
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
    }

    /* Critical Section - synchronized allocation */
    public synchronized boolean allocateRoom(String roomType) {

        int available = inventory.getOrDefault(roomType, 0);

        if (available > 0) {
            inventory.put(roomType, available - 1);
            return true;
        }

        return false;
    }

    public void displayInventory() {
        System.out.println("Current Inventory: " + inventory);
    }
}


/* Shared Booking Queue */
class BookingQueue {

    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void addRequest(Reservation r) {
        queue.add(r);
    }

    public synchronized Reservation getRequest() {
        return queue.poll();
    }
}


/* Concurrent Booking Processor */
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    public void run() {

        while (true) {

            Reservation request = queue.getRequest();

            if (request == null)
                break;

            boolean success = inventory.allocateRoom(request.roomType);

            if (success) {
                System.out.println(Thread.currentThread().getName()
                        + " confirmed booking for "
                        + request.guestName
                        + " (" + request.roomType + ")");
            } else {
                System.out.println(Thread.currentThread().getName()
                        + " failed booking for "
                        + request.guestName
                        + " - No rooms available");
            }
        }
    }
}


/* Main Application */
class HotelBookingApplication {

    public static void main(String[] args) {

        System.out.println("Concurrent Booking Simulation\n");

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        /* Multiple guests submitting requests */
        queue.addRequest(new Reservation("Sachnoor", "Single Room"));
        queue.addRequest(new Reservation("Aman", "Single Room"));
        queue.addRequest(new Reservation("Riya", "Single Room"));
        queue.addRequest(new Reservation("Karan", "Double Room"));

        /* Multiple threads processing requests */
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Processor-1");
        t2.setName("Processor-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nFinal Inventory State:");
        inventory.displayInventory();
    }
}