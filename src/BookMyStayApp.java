import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory
 *
 * This class is responsible for managing room availability
 * using a centralized HashMap. It ensures consistency and
 * scalability in inventory operations.
 *
 * @author YourName
 * @version 3.0
 */
class RoomInventory {

    // Centralized inventory storage
    private Map<String, Integer> inventory;

    /**
     * Constructor to initialize room inventory
     */
    public RoomInventory() {
        inventory = new HashMap<>();

        // Initial room setup
        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Deluxe", 3);
    }

    /**
     * Get availability of a specific room type
     *
     * @param roomType Type of room
     * @return available count
     */
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    /**
     * Update availability of a specific room type
     *
     * @param roomType Type of room
     * @param count New available count
     */
    public void updateAvailability(String roomType, int count) {
        inventory.put(roomType, count);
    }

    /**
     * Display all room inventory
     */
    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

/**
 * UseCase3InventorySetup
 *
 * This class demonstrates the centralized inventory system
 * using RoomInventory and validates operations.
 *
 * @author YourName
 * @version 3.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay App - Inventory Setup");
        System.out.println(" Version: 3.1");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial inventory
        inventory.displayInventory();

        // Check availability
        System.out.println("\nChecking availability for Double room:");
        System.out.println("Available: " + inventory.getAvailability("Double"));

        // Update inventory
        System.out.println("\nUpdating Double room availability to 4...");
        inventory.updateAvailability("Double", 4);

        // Display updated inventory
        System.out.println("\nUpdated Inventory:");
        inventory.displayInventory();
    }
}