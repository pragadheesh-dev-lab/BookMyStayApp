import java.util.*;
import java.util.concurrent.*;

// Booking Request Class
class BookingRequest {
    private String guestName;
    private int roomsRequested;

    public BookingRequest(String guestName, int roomsRequested) {
        this.guestName = guestName;
        this.roomsRequested = roomsRequested;
    }

    public String getGuestName() {
        return guestName;
    }

    public int getRoomsRequested() {
        return roomsRequested;
    }
}

// Shared Inventory
class RoomInventory {
    private int availableRooms;

    public RoomInventory(int rooms) {
        this.availableRooms = rooms;
    }

    // Critical Section (Thread Safe)
    public synchronized boolean allocateRoom(String guestName, int rooms) {
        if (rooms <= availableRooms) {
            System.out.println(Thread.currentThread().getName() +
                    " allocating " + rooms + " room(s) to " + guestName);
            availableRooms -= rooms;
            System.out.println("Remaining rooms: " + availableRooms);
            return true;
        } else {
            System.out.println(Thread.currentThread().getName() +
                    " FAILED for " + guestName + " (Not enough rooms)");
            return false;
        }
    }
}

// Booking Processor (Consumer Threads)
class BookingProcessor implements Runnable {
    private BlockingQueue<BookingRequest> queue;
    private RoomInventory inventory;

    public BookingProcessor(BlockingQueue<BookingRequest> queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        try {
            while (true) {
                BookingRequest request = queue.poll(2, TimeUnit.SECONDS);
                if (request == null) {
                    break; // Exit if no more requests
                }
                inventory.allocateRoom(request.getGuestName(), request.getRoomsRequested());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        // Shared Queue
        BlockingQueue<BookingRequest> bookingQueue = new LinkedBlockingQueue<>();

        // Shared Inventory
        RoomInventory inventory = new RoomInventory(10);

        // Simulate Multiple Guests (Producers)
        bookingQueue.add(new BookingRequest("Alice", 2));
        bookingQueue.add(new BookingRequest("Bob", 4));
        bookingQueue.add(new BookingRequest("Charlie", 3));
        bookingQueue.add(new BookingRequest("David", 2));
        bookingQueue.add(new BookingRequest("Eve", 1));

        // Multiple Threads (Processors)
        Thread t1 = new Thread(new BookingProcessor(bookingQueue, inventory), "Processor-1");
        Thread t2 = new Thread(new BookingProcessor(bookingQueue, inventory), "Processor-2");
        Thread t3 = new Thread(new BookingProcessor(bookingQueue, inventory), "Processor-3");

        t1.start();
        t2.start();
        t3.start();

        // Wait for completion
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Booking processing completed.");
    }
}