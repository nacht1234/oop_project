import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class CineReserveSys {

    private static final String USER_FILE = "users.txt";
    private static final String RESERVATION_FILE = "reservations.txt";
    private static final int NUM_SEATS = 20;
    private static Map<String, boolean[][]> seat_resv = new HashMap<>();

    public static void main(String[] args) {
        initializeSeatReservations();

        while (true) {
            String[] action_choices = {"Sign Up", "Login", "Exit"};
            int action_choice = JOptionPane.showOptionDialog(null, "Welcome to the Cinema Reservation System!", "Cinema Reservation System", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, action_choices, action_choices[0]);

            if (action_choice == JOptionPane.CLOSED_OPTION) {
                break;
            }

            switch (action_choice) {
                case 0:
                    signUp();
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    System.exit(0);
            }
        }
    }

    private static void initializeSeatReservations() {
        seat_resv.put("50 Shades of a Rainbow - 1 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("50 Shades of a Rainbow - 1 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("50 Shades of a Rainbow - 15 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("50 Shades of a Rainbow - 15 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("50 Shades of a Rainbow - 20 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("50 Shades of a Rainbow - 20 - 3:00 PM", new boolean[4][5]);

        seat_resv.put("Star Wars: War of the Stars - 5 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Star Wars: War of the Stars - 5 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("Star Wars: War of the Stars - 10 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Star Wars: War of the Stars - 10 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("Star Wars: War of the Stars - 16 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Star Wars: War of the Stars - 16 - 3:00 PM", new boolean[4][5]);

        seat_resv.put("Moana 2: How far I'll go - 3 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Moana 2: How far I'll go - 3 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("Moana 2: How far I'll go - 9 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Moana 2: How far I'll go - 9 - 3:00 PM", new boolean[4][5]);
        seat_resv.put("Moana 2: How far I'll go - 13 - 10:00 AM", new boolean[4][5]);
        seat_resv.put("Moana 2: How far I'll go - 13 - 3:00 PM", new boolean[4][5]);
    }

    private static void signUp() {
        String signup_username = JOptionPane.showInputDialog(null, "Enter username:", "Sign Up", JOptionPane.PLAIN_MESSAGE);
        if (signup_username == null || signup_username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String signup_pass = JOptionPane.showInputDialog(null, "Enter password:", "Sign Up", JOptionPane.PLAIN_MESSAGE);
        if (signup_pass == null || signup_pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PrintWriter signup_file_writer = new PrintWriter(new FileWriter(USER_FILE, true))) {
            signup_file_writer.println(signup_username + "," + signup_pass);
            JOptionPane.showMessageDialog(null, "Sign up successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error during sign up.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void login() {
        String login_username = JOptionPane.showInputDialog(null, "Enter username:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (login_username == null || login_username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String login_pass = JOptionPane.showInputDialog(null, "Enter password:", "Login", JOptionPane.PLAIN_MESSAGE);
        if (login_pass == null || login_pass.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (authenticate(login_username, login_pass)) {
            JOptionPane.showMessageDialog(null, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            reservationMenu(login_username);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean authenticate(String username, String password) {
        try (BufferedReader auth_user_file_reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = auth_user_file_reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error during login.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private static void reservationMenu(String username) {
        while (true) {
            String[] menu_choices = {"Reserve Seat", "View Reservations", "Cancel Reservation", "Logout"};
            int menu_choice = JOptionPane.showOptionDialog(null, null, "Main Menu", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, menu_choices, menu_choices[0]);

            if (menu_choice == JOptionPane.CLOSED_OPTION) {
                break;
            }

            switch (menu_choice) {
                case 0:
                    movieSelection(username);
                    break;
                case 1:
                    viewReservations();
                    break;
                case 2:
                    cancelReservation(username);
                    break;
                case 3:
                    return;
            }
        }
    }

    private static void movieSelection(String username) {
        String[] avail_movies = {"50 Shades of a Rainbow", "Star Wars: War of the Stars", "Moana 2: How far I'll go"};
        int movie_choice = JOptionPane.showOptionDialog(null, "Select a movie:", "Movie Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_movies, avail_movies[0]);

        if (movie_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        switch (movie_choice) {
            case 0:
                selectDateAndTime(username, "50 Shades of a Rainbow", new int[]{1, 15, 20});
                break;
            case 1:
                selectDateAndTime(username, "Star Wars: War of the Stars", new int[]{5, 10, 16});
                break;
            case 2:
                selectDateAndTime(username, "Moana 2: How far I'll go", new int[]{3, 9, 13});
                break;
        }
    }

    private static void selectDateAndTime(String username, String movieName, int[] availableDates) {
        String[] avail_dates = new String[availableDates.length];
        for (int i = 0; i < availableDates.length; i++) {
            avail_dates[i] = String.valueOf(availableDates[i]);
        }

        int date_choice = JOptionPane.showOptionDialog(null, "Available dates for " + movieName + " in December:", "Date Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_dates, avail_dates[0]);

        if (date_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        String[] avail_times = {"10:00 AM", "3:00 PM"};
        int time_choice = JOptionPane.showOptionDialog(null, "Available times:", "Time Selection", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_times, avail_times[0]);

        if (time_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        reserveSeat(username, movieName, availableDates[date_choice], avail_times[time_choice]);
    }

    private static void reserveSeat(String username, String movieName, int date, String time) {
        String seat_no_str = JOptionPane.showInputDialog(null, "Enter seat number (1-" + NUM_SEATS + "):", "Reserve Seat", JOptionPane.PLAIN_MESSAGE);

        if (seat_no_str == null || seat_no_str.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seat number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int seat_no = Integer.parseInt(seat_no_str);
            if (seat_no < 1 || seat_no > NUM_SEATS) {
                JOptionPane.showMessageDialog(null, "Invalid seat number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sched_key = movieName + " - " + date + " - " + time;
            boolean[][] seats = seat_resv.get(sched_key);

            int row = (seat_no - 1) / 5;
            int colm = (seat_no - 1) % 5;

            if (seats[row][colm]) {
                JOptionPane.showMessageDialog(null, "Seat already reserved.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (paymentWindow()) {
                seats[row][colm] = true;
                saveReservation(username, seat_no, movieName, date, time);
                JOptionPane.showMessageDialog(null, "Seat reserved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Payment failed. Seat reservation canceled.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid seat number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void viewReservations() {
        String[] avail_movies = {"50 Shades of a Rainbow", "Star Wars: War of the Stars", "Moana 2: How far I'll go"};
        int movie_choice = JOptionPane.showOptionDialog(null, "Select a movie:", "View Reservations", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_movies, avail_movies[0]);
    
        if (movie_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String movie_name = avail_movies[movie_choice];
        String[] avail_dates;
    
        switch (movie_name) {
            case "50 Shades of a Rainbow":
                avail_dates = new String[]{"1", "15", "20"};
                break;
            case "Star Wars: War of the Stars":
                avail_dates = new String[]{"5", "10", "16"};
                break;
            case "Moana 2: How far I'll go":
                avail_dates = new String[]{"3", "9", "13"};
                break;
            default:
                return;
        }
    
        int date_choice = JOptionPane.showOptionDialog(null, "Select date:", "View Reservations", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_dates, avail_dates[0]);
    
        if (date_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String date_str = avail_dates[date_choice];
    
        String[] avail_times = {"10:00 AM", "3:00 PM"};
        int time_choice = JOptionPane.showOptionDialog(null, "Select time:", "View Reservations", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_times, avail_times[0]);
    
        if (time_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String time = avail_times[time_choice];

        String sched_key = movie_name + " - " + date_str + " - " + time;
        boolean[][] seats = seat_resv.get(sched_key);

        if (seats == null) {
            JOptionPane.showMessageDialog(null, "No reservations found for this schedule.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder grid = new StringBuilder("  1 2 3 4 5\n");
        for (int i = 0; i < 4; i++) {
            grid.append((char) ('A' + i)).append(" ");
            for (int j = 0; j < 5; j++) {
                grid.append(seats[i][j] ? "X " : "O ");
            }
            grid.append("\n");
        }

        JOptionPane.showMessageDialog(null, grid.toString(), "Reservations for " + sched_key, JOptionPane.PLAIN_MESSAGE);
    }

    private static void cancelReservation(String username) {
        String[] avail_movies = {"50 Shades of a Rainbow", "Star Wars: War of the Stars", "Moana 2: How far I'll go"};
        int movie_choice = JOptionPane.showOptionDialog(null, "Select a movie:", "Cancel Reservation", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_movies, avail_movies[0]);
    
        if (movie_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String movie_name = avail_movies[movie_choice];
        String[] dates;
    
        switch (movie_name) {
            case "50 Shades of a Rainbow":
                dates = new String[]{"1", "15", "20"};
                break;
            case "Star Wars: War of the Stars":
                dates = new String[]{"5", "10", "16"};
                break;
            case "Moana 2: How far I'll go":
                dates = new String[]{"3", "9", "13"};
                break;
            default:
                return;
        }
    
        int date_choice = JOptionPane.showOptionDialog(null, "Select date:", "Cancel Reservation", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, dates, dates[0]);
    
        if (date_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String date_str = dates[date_choice];
    
        String[] avail_times = {"10:00 AM", "3:00 PM"};
        int time_choice = JOptionPane.showOptionDialog(null, "Select time:", "Cancel Reservation", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, avail_times, avail_times[0]);
    
        if (time_choice == JOptionPane.CLOSED_OPTION) {
            return;
        }
    
        String time = avail_times[time_choice];

        String seat_no_str = JOptionPane.showInputDialog(null, "Enter seat number to cancel:", "Cancel Reservation", JOptionPane.PLAIN_MESSAGE);

        if (seat_no_str == null || seat_no_str.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Seat number cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int seat_no = Integer.parseInt(seat_no_str);
            if (seat_no < 1 || seat_no > NUM_SEATS) {
                JOptionPane.showMessageDialog(null, "Invalid seat number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sched_key = movie_name + " - " + date_str + " - " + time;
            boolean[][] seats = seat_resv.get(sched_key);

            if (seats == null) {
                JOptionPane.showMessageDialog(null, "No reservations found for this schedule.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int row = (seat_no - 1) / 5;
            int colm = (seat_no - 1) % 5;

            if (!seats[row][colm]) {
                JOptionPane.showMessageDialog(null, "Seat not reserved.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            seats[row][colm] = false;
            removeReservation(username, seat_no, movie_name, date_str, time);
            JOptionPane.showMessageDialog(null, "Reservation canceled successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid seat number.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void saveReservation(String username, int seatNumber, String movieName, int date, String time) {
        try (PrintWriter resv_file_writer = new PrintWriter(new FileWriter(RESERVATION_FILE, true))) {
            resv_file_writer.println(username + "," + seatNumber + "," + movieName + "," + date + "," + time);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving reservation.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void removeReservation(String username, int seatNumber, String movieName, String dateStr, String time) {
        List<String> reservations = new ArrayList<>();
        try (BufferedReader resv_file_reader = new BufferedReader(new FileReader(RESERVATION_FILE))) {
            String line;
            while ((line = resv_file_reader.readLine()) != null) {
                String[] data = line.split(",");
                if (!(data[0].equals(username) && Integer.parseInt(data[1]) == seatNumber &&
                        data[2].equals(movieName) && data[3].equals(dateStr) && data[4].equals(time))) {
                    reservations.add(line);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading reservations.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (PrintWriter resv_file_writer = new PrintWriter(new FileWriter(RESERVATION_FILE))) {
            for (String reservation : reservations) {
                resv_file_writer.println(reservation);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error updating reservations.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static boolean paymentWindow() {
        PaymentMethod[] paymentMethods = {
            new GCashPayment(),
            new PaymayaPayment(),
            new CreditCardPayment(),
            new DebitCardPayment(),
            new BankTransferPayment()
        };

        String[] payMethodNames = new String[paymentMethods.length];
        for (int i = 0; i < paymentMethods.length; i++) {
            payMethodNames[i] = paymentMethods[i].methodName;
        }

        int choice = JOptionPane.showOptionDialog(null, "Choose payment method:", "Payment", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, payMethodNames, payMethodNames[0]);

        if (choice == JOptionPane.CLOSED_OPTION) {
            return false;
        }

        String amountStr = JOptionPane.showInputDialog(null, "Enter amount to pay (250 pesos):", "Payment", JOptionPane.PLAIN_MESSAGE);
        if (amountStr == null || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            return paymentMethods[choice].processPayment(amount);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}

abstract class PaymentMethod {
    protected String methodName;

    public PaymentMethod(String methodName) {
        this.methodName = methodName;
    }

    public abstract boolean processPayment(double amount);
}

class GCashPayment extends PaymentMethod {
    public GCashPayment() {
        super("GCash");
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount < 250) {
            JOptionPane.showMessageDialog(null, "Insufficient amount. Minimum payment is 250.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double change = amount - 250;
        JOptionPane.showMessageDialog(null, "Payment successful with GCash!\nAmount: " + amount + "\nChange: " + change, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}

class PaymayaPayment extends PaymentMethod {
    public PaymayaPayment() {
        super("Paymaya");
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount < 250) {
            JOptionPane.showMessageDialog(null, "Insufficient amount. Minimum payment is 250.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        double change = amount - 250;
        JOptionPane.showMessageDialog(null, "Payment successful with Paymaya!\nAmount: " + amount + "\nChange: " + change, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}

class CreditCardPayment extends PaymentMethod {
    public CreditCardPayment() {
        super("Credit Card");
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount < 250) {
            JOptionPane.showMessageDialog(null, "Insufficient amount. Minimum payment is 250.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(null, "Payment successful with Credit Card!\nAmount: " + amount, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}

class DebitCardPayment extends PaymentMethod {
    public DebitCardPayment() {
        super("Debit Card");
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount < 250) {
            JOptionPane.showMessageDialog(null, "Insufficient amount. Minimum payment is 250.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(null, "Payment successful with Debit Card!\nAmount: " + amount, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}

class BankTransferPayment extends PaymentMethod {
    public BankTransferPayment() {
        super("Bank Transfer");
    }

    @Override
    public boolean processPayment(double amount) {
        if (amount < 250) {
            JOptionPane.showMessageDialog(null, "Insufficient amount. Minimum payment is 250.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        JOptionPane.showMessageDialog(null, "Payment successful with Bank Transfer!\nAmount: " + amount, "Payment Success", JOptionPane.INFORMATION_MESSAGE);
        return true;
    }
}