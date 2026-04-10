import java.util.HashMap;
import java.util.Map;

/**
 * Room
 *
 * Domain model representing a room with details like
 * type, price, and amenities.
 *
 * @author YourName
 * @version 4.0
 */
class Room {
    private String type;
    private double price;
    private String amenities;

    public Room(String type, double price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

/**
 * RoomInventory
 *
 * Maintains centralized availability using HashMap.
 * Provides read-only access for search operations.
 *
 * @author YourName
 * @version 4.0
 */
class RoomInventory {
    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single", 10);
        inventory.put("Double", 0);   // intentionally unavailable
        inventory.put("Deluxe", 3);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Expose full inventory (read-only usage expected)
    public Map<String, Integer> getAllInventory() {
        return inventory;
    }
}

/**
 * SearchService
 *
 * Handles room search functionality without modifying system state.
 * Filters only available rooms and displays details.
 *
 * @author YourName
 * @version 4.0
 */
class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomCatalog;

    public SearchService(RoomInventory inventory) {
        this.inventory = inventory;
        this.roomCatalog = new HashMap<>();

        // Room catalog setup (Domain model)
        roomCatalog.put("Single", new Room("Single", 2000, "1 Bed, Free WiFi"));
        roomCatalog.put("Double", new Room("Double", 3500, "2 Beds, AC, Free WiFi"));
        roomCatalog.put("Deluxe", new Room("Deluxe", 5000, "King Bed, AC, Breakfast"));
    }

    /**
     * Displays available rooms without modifying inventory
     */
    public void searchAvailableRooms() {
        System.out.println("\nAvailable Rooms:");

        for (String type : roomCatalog.keySet()) {
            int available = inventory.getAvailability(type);

            // Defensive check: only show available rooms
            if (available > 0) {
                Room room = roomCatalog.get(type);

                System.out.println("------------------------------------");
                System.out.println("Room Type : " + room.getType());
                System.out.println("Price     : ₹" + room.getPrice());
                System.out.println("Amenities : " + room.getAmenities());
                System.out.println("Available : " + available);
            }
        }
    }
}

/**
 * UseCase4RoomSearch
 *
 * Demonstrates room search functionality with read-only access
 * to inventory and proper separation of concerns.
 *
 * @author YourName
 * @version 4.1
 */
public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" Book My Stay App - Room Search");
        System.out.println(" Version: 4.1");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize search service
        SearchService searchService = new SearchService(inventory);

        // Perform search (read-only operation)
        searchService.searchAvailableRooms();

        System.out.println("\nSearch completed. Inventory remains unchanged.");
    }
}