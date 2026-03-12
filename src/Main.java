import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation
 *
 * Represents a guest's request to book a specific room type.
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}


/**
 * BookingRequestQueue
 *
 * Manages incoming booking requests using a Queue.
 * Ensures FIFO order of processing.
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        requestQueue.add(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View next request (without removing)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Display all queued requests
    public void displayQueue() {

        System.out.println("\nCurrent Booking Queue:");

        for (Reservation r : requestQueue) {
            r.displayReservation();
        }
    }
}


/**
 * HotelBookingApplication
 *
 * Demonstrates intake of booking requests using a FIFO queue.
 */
class HotelBookingApplication {

    public static void main(String[] args) {

        System.out.println("Hotel Booking System - Booking Request Queue\n");

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests submit booking requests
        Reservation r1 = new Reservation("Sachnoor", "Single Room");
        Reservation r2 = new Reservation("Aman", "Double Room");
        Reservation r3 = new Reservation("Riya", "Suite Room");

        // Add requests to queue
        queue.addRequest(r1);
        queue.addRequest(r2);
        queue.addRequest(r3);

        // Display queued requests
        queue.displayQueue();

        System.out.println("\nRequests stored in FIFO order. Awaiting allocation.");
    }
}