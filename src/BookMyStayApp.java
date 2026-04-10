import java.util.*;

/**
 * Reservation
 *
 * Represents a booking request made by a guest.
 *
 * @author YourName
 * @version 6.0
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
}

/**
 * RoomInventory
 *
 * Maintains room availability and updates inventory
 * after successful allocation.
 *
 * @author YourName
 * @version 6.0
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 1);
        inventory.put("Deluxe", 1);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void decrementRoom(String roomType) {
        int current = getAvailability(roomType);
        if (current > 0) {
            inventory.put(roomType, current - 1);
        }
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

/**
 * BookingRequestQueue
 *
 * FIFO queue storing booking requests.
 *
 * @author YourName
 * @version 6.0
 */
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public Reservation getNextRequest() {
        return queue.poll(); // removes from queue
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * BookingService
 *
 * Processes booking requests, assigns unique room IDs,
 * prevents double-booking, and updates inventory.
 *
 * @author YourName
 * @version 6.0
 */
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs globally
    private Set<String> allocatedRoomIds;

    // Map room type -> allocated room IDs
    private Map<String, Set<String>> roomAllocations;

    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
        this.allocatedRoomIds = new HashSet<>();
        this.roomAllocations = new HashMap<>();
    }

    /**
     * Process booking queue
     */
    public void processBookings(BookingRequestQueue queue) {

        System.out.println("\nProcessing Booking Requests...\n");

        while (!queue.isEmpty()) {

            Reservation request = queue.getNextRequest();
            String roomType = request.getRoomType();

            System.out.println("Processing request for " + request.getGuestName()
                    + " (" + roomType + ")");

            // Check availability
            if (inventory.getAvailability(roomType) > 0) {

                // Generate unique room ID
                String roomId = generateRoomId(roomType);

                // Ensure uniqueness using Set
                if (!allocatedRoomIds.contains(roomId)) {

                    allocatedRoomIds.add(roomId);

                    // Map room type -> room IDs
                    roomAllocations
                            .computeIfAbsent(roomType, k -> new HashSet<>())
                            .add(roomId);

                    // Update inventory immediately
                    inventory.decrementRoom(roomType);

                    System.out.println("Booking CONFIRMED | Room ID: " + roomId);
                }

            } else {
                System.out.println("Booking FAILED | No rooms available");
            }

            System.out.println("------------------------------------");
        }
    }

    /**
     * Generate unique room ID
     */
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 1).toUpperCase() + idCounter++;
    }

    /**
     * Display all allocations
     */
    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " Rooms -> " + roomAllocations.get(type));
        }
    }
}

/**
 * UseCase6RoomAllocationService
 *
 * Demonstrates booking confirmation with safe allocation,
 * uniqueness enforcement, and inventory synchronization.
 *
 * @author YourName
 * @version 6.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay App - Room Allocation");
        System.out.println(" Version: 6.1");
        System.out.println("====================================");

        // Initialize components
        RoomInventory inventory = new RoomInventory();
        BookingRequestQueue queue = new BookingRequestQueue();
        BookingService bookingService = new BookingService(inventory);

        // Add booking requests (FIFO)
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single")); // should fail
        queue.addRequest(new Reservation("Diana", "Deluxe"));
        queue.addRequest(new Reservation("Ethan", "Double"));

        // Process bookings
        bookingService.processBookings(queue);

        // Show final state
        bookingService.displayAllocations();
        inventory.displayInventory();
    }
}