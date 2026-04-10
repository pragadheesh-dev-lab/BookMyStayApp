import java.util.*;

// Represents an Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return serviceName + " ($" + cost + ")";
    }
}

// Manages Add-On Services for Reservations
class AddOnServiceManager {
    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> reservationServicesMap;

    public AddOnServiceManager() {
        reservationServicesMap = new HashMap<>();
    }

    // Add service to a reservation
    public void addService(String reservationId, AddOnService service) {
        reservationServicesMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);
    }

    // Get services for a reservation
    public List<AddOnService> getServices(String reservationId) {
        return reservationServicesMap.getOrDefault(reservationId, new ArrayList<>());
    }

    // Calculate total cost of services
    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = getServices(reservationId);
        double total = 0.0;

        for (AddOnService service : services) {
            total += service.getCost();
        }

        return total;
    }

    // Display services
    public void displayServices(String reservationId) {
        List<AddOnService> services = getServices(reservationId);

        if (services.isEmpty()) {
            System.out.println("No add-on services selected.");
            return;
        }

        System.out.println("Add-On Services for Reservation " + reservationId + ":");
        for (AddOnService service : services) {
            System.out.println("- " + service);
        }
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AddOnServiceManager manager = new AddOnServiceManager();

        System.out.print("Enter Reservation ID: ");
        String reservationId = scanner.nextLine();

        // Predefined services (can be extended easily)
        AddOnService wifi = new AddOnService("WiFi", 10.0);
        AddOnService breakfast = new AddOnService("Breakfast", 20.0);
        AddOnService parking = new AddOnService("Parking", 15.0);
        AddOnService spa = new AddOnService("Spa Access", 50.0);

        List<AddOnService> availableServices = Arrays.asList(wifi, breakfast, parking, spa);

        while (true) {
            System.out.println("\nAvailable Add-On Services:");
            for (int i = 0; i < availableServices.size(); i++) {
                System.out.println((i + 1) + ". " + availableServices.get(i));
            }
            System.out.println("0. Finish Selection");

            System.out.print("Select a service: ");
            int choice = scanner.nextInt();

            if (choice == 0) {
                break;
            }

            if (choice < 1 || choice > availableServices.size()) {
                System.out.println("Invalid choice. Try again.");
                continue;
            }

            AddOnService selectedService = availableServices.get(choice - 1);
            manager.addService(reservationId, selectedService);

            System.out.println(selectedService.getServiceName() + " added.");
        }

        // Display selected services
        System.out.println("\n--- Summary ---");
        manager.displayServices(reservationId);

        double totalCost = manager.calculateTotalServiceCost(reservationId);
        System.out.println("Total Add-On Cost: $" + totalCost);

        scanner.close();
    }
}
