import java.util.*;

// Represents a Reservation
class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private int nights;
    private double pricePerNight;

    public Reservation(String reservationId, String guestName, String roomType, int nights, double pricePerNight) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
        this.pricePerNight = pricePerNight;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public double getTotalCost() {
        return nights * pricePerNight;
    }

    @Override
    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest: " + guestName +
                ", Room: " + roomType +
                ", Nights: " + nights +
                ", Total Cost: $" + getTotalCost();
    }
}

// Maintains booking history
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservations); // defensive copy
    }
}

// Generates reports
class BookingReportService {

    public void displayAllBookings(List<Reservation> reservations) {
        if (reservations.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    public void generateSummary(List<Reservation> reservations) {
        int totalBookings = reservations.size();
        double totalRevenue = 0.0;

        for (Reservation r : reservations) {
            totalRevenue += r.getTotalCost();
        }

        System.out.println("\n--- Booking Summary Report ---");
        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Revenue: $" + totalRevenue);
    }
}

// Main Class
public class BookMyStayApp {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        while (true) {
            System.out.println("\n1. Add Confirmed Booking");
            System.out.println("2. View Booking History");
            System.out.println("3. Generate Report");
            System.out.println("0. Exit");

            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Reservation ID: ");
                    String id = scanner.nextLine();

                    System.out.print("Enter Guest Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Room Type: ");
                    String room = scanner.nextLine();

                    System.out.print("Enter Nights: ");
                    int nights = scanner.nextInt();

                    System.out.print("Enter Price per Night: ");
                    double price = scanner.nextDouble();
                    scanner.nextLine(); // consume newline

                    Reservation reservation = new Reservation(id, name, room, nights, price);
                    history.addReservation(reservation);

                    System.out.println("Booking confirmed and added to history.");
                    break;

                case 2:
                    reportService.displayAllBookings(history.getAllReservations());
                    break;

                case 3:
                    reportService.generateSummary(history.getAllReservations());
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