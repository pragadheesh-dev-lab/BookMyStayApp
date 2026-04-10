import java.util.*;

// Reservation Model
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private boolean isCancelled;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.isCancelled = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomId() {
        return roomId;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Status: " + (isCancelled ? "Cancelled" : "Confirmed");
    }
}

// Inventory Manager
class InventoryManager {
    private Map<String, Integer> inventory;

    public InventoryManager() {
        inventory = new HashMap<>();
        inventory.put("Single", 2);
        inventory.put("Double", 2);
    }

    public boolean allocateRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
            return true;
        }
        return false;
    }

    public void releaseRoom(String roomType) {
        inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);
    }

    public void displayInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + " Rooms Available: " + inventory.get(type));
        }
    }
}

// Booking History
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public Reservation findReservation(String id) {
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(id)) {
                return r;
            }
        }
        return null;
    }

    public void displayAll() {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }
}

// Cancellation Service with Stack Rollback
class CancellationService {
    private Stack<String> rollbackStack = new Stack<>();

    public void cancelReservation(String reservationId,
                                  BookingHistory history,
                                  InventoryManager inventory) {

        Reservation reservation = history.findReservation(reservationId);

        // Validation
        if (reservation == null) {
            System.out.println("Reservation not found.");
            return;
        }

        if (reservation.isCancelled()) {
            System.out.println("Reservation already cancelled.");
            return;
        }

        // Step 1: Push room ID to stack
        rollbackStack.push(reservation.getRoomId());

        // Step 2: Restore inventory
        inventory.releaseRoom(reservation.getRoomType());

        // Step 3: Mark reservation cancelled
        reservation.cancel();

        System.out.println("Cancellation successful for Reservation ID: " + reservationId);

        // Debug: Show rollback stack
        System.out.println("Rollback Stack: " + rollbackStack);
    }
}

// Main Application
public class BookMyStayApp {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        InventoryManager inventory = new InventoryManager();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        // Pre-created bookings (for demo)
        Reservation r1 = new Reservation("R101", "Alice", "Single", "S1");
        Reservation r2 = new Reservation("R102", "Bob", "Double", "D1");

        history.addReservation(r1);
        history.addReservation(r2);

        inventory.allocateRoom("Single");
        inventory.allocateRoom("Double");

        while (true) {
            System.out.println("\n1. View Bookings");
            System.out.println("2. Cancel Booking");
            System.out.println("3. View Inventory");
            System.out.println("0. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case 1:
                    history.displayAll();
                    break;

                case 2:
                    System.out.print("Enter Reservation ID to cancel: ");
                    String id = scanner.nextLine();
                    cancellationService.cancelReservation(id, history, inventory);
                    break;

                case 3:
                    inventory.displayInventory();
                    break;

                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}