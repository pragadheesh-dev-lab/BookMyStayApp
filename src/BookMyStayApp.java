import java.util.LinkedList;
import java.util.Queue;

/**
 * Reservation
 *
 * Represents a guest's booking request.
 * Contains guest name and requested room type.
 *
 * @author YourName
 * @version 5.0
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

    @Override
    public String toString() {
        return "Guest: " + guestName + ", Room Type: " + roomType;
    }
}

/**
 * BookingRequestQueue
 *
 * Manages incoming booking requests using FIFO principle.
 * Ensures fair ordering of requests.
 *
 * No inventory updates happen at this stage.
 *
 * @author YourName
 * @version 5.0
 */
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    /**
     * Add a booking request to the queue
     */
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Request added: " + reservation);
    }

    /**
     * View all queued requests (without removing)
     */
    public void viewRequests() {
        System.out.println("\nCurrent Booking Requests (FIFO Order):");

        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }

        for (Reservation r : requestQueue) {
            System.out.println(r);
        }
    }

    /**
     * Get next request (for future processing)
     */
    public Reservation getNextRequest() {
        return requestQueue.peek(); // does not remove
    }
}

/**
 * UseCase5BookingRequestQueue
 *
 * Demonstrates how booking requests are collected
 * and ordered using a FIFO queue.
 *
 * @author YourName
 * @version 5.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay App - Booking Queue");
        System.out.println(" Version: 5.1");
        System.out.println("====================================");

        // Initialize booking queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Simulate incoming booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Deluxe"));
        queue.addRequest(new Reservation("Charlie", "Single"));
        queue.addRequest(new Reservation("Diana", "Double"));

        // View all requests (FIFO order preserved)
        queue.viewRequests();

        // Peek next request
        System.out.println("\nNext request to process:");
        System.out.println(queue.getNextRequest());

        System.out.println("\nAll requests are queued. No allocation done yet.");
    }
}