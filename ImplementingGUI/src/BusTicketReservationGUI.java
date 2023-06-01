
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BusTicketReservationGUI {
    private JFrame frame;
    private JComboBox<String> busComboBox;
    private JTextField passengerNameField;
    private JComboBox<Integer> seatNumberComboBox;
    private JTextField paymentMethodField;

    private List<Bus> buses;

    public BusTicketReservationGUI() {
        buses = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Bus Ticket Reservation");
        frame.setBounds(100, 100, 400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblBus = new JLabel("Bus:");
        lblBus.setBounds(20, 20, 60, 20);
        frame.getContentPane().add(lblBus);

        busComboBox = new JComboBox<>();
        busComboBox.setBounds(100, 20, 200, 20);
        frame.getContentPane().add(busComboBox);

        JLabel lblPassengerName = new JLabel("Passenger Name:");
        lblPassengerName.setBounds(20, 60, 120, 20);
        frame.getContentPane().add(lblPassengerName);

        passengerNameField = new JTextField();
        passengerNameField.setBounds(150, 60, 150, 20);
        frame.getContentPane().add(passengerNameField);

        JLabel lblSeatNumber = new JLabel("Seat Number:");
        lblSeatNumber.setBounds(20, 100, 100, 20);
        frame.getContentPane().add(lblSeatNumber);

        seatNumberComboBox = new JComboBox<>();
        seatNumberComboBox.setBounds(130, 100, 50, 20);
        frame.getContentPane().add(seatNumberComboBox);

        JLabel lblPaymentMethod = new JLabel("Payment Method:");
        lblPaymentMethod.setBounds(20, 140, 120, 20);
        frame.getContentPane().add(lblPaymentMethod);

        paymentMethodField = new JTextField();
        paymentMethodField.setBounds(150, 140, 150, 20);
        frame.getContentPane().add(paymentMethodField);

        JButton btnBookTicket = new JButton("Book Ticket");
        btnBookTicket.setBounds(150, 180, 100, 30);
        frame.getContentPane().add(btnBookTicket);
        btnBookTicket.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bookTicket();
            }
        });

        frame.setVisible(true);
    }

    public void addBus(Bus bus) {
        buses.add(bus);
        busComboBox.addItem(bus.getName());
    }

    private void bookTicket() {
        int busIndex = busComboBox.getSelectedIndex();
        String passengerName = passengerNameField.getText();
        int seatNumber = (int) seatNumberComboBox.getSelectedItem();
        String paymentMethod = paymentMethodField.getText();

        if (busIndex >= 0 && busIndex < buses.size()) {
            Bus selectedBus = buses.get(busIndex);
            if (selectedBus.hasAvailableSeat(seatNumber)) {
                Ticket ticket = new Ticket(selectedBus, passengerName, seatNumber);
                selectedBus.bookSeat(seatNumber);
                JOptionPane.showMessageDialog(frame, "Ticket booked successfully!\n" +
                        "Bus: " + selectedBus.getName() + "\n" +
                        "Passenger Name: " + passengerName + "\n" +
                        "Seat Number: " + seatNumber + "\n" +
                        "Payment Method: " + paymentMethod);
            } else {
                JOptionPane.showMessageDialog(frame, "Seat number " + seatNumber + " is not available for the selected bus.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid bus selection.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                BusTicketReservationGUI ticketGUI = new BusTicketReservationGUI();

                // Adding sample buses
                Bus bus1 = new Bus("Bus 1", 10);
                Bus bus2 = new Bus("Bus 2", 15);
                ticketGUI.addBus(bus1);
                ticketGUI.addBus(bus2);

                // Adding seat numbers to combo box
                for (int i = 1; i <= bus1.getSeatCapacity(); i++) {
                    ticketGUI.seatNumberComboBox.addItem(i);
                }

                // Adding action listener to bus combo box
                ticketGUI.busComboBox.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int busIndex = ticketGUI.busComboBox.getSelectedIndex();
                        if (busIndex >= 0 && busIndex < ticketGUI.buses.size()) {
                            Bus selectedBus = ticketGUI.buses.get(busIndex);
                            ticketGUI.seatNumberComboBox.removeAllItems();
                            for (int i = 1; i <= selectedBus.getSeatCapacity(); i++) {
                                if (selectedBus.hasAvailableSeat(i)) {
                                    ticketGUI.seatNumberComboBox.addItem(i);
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}

class Bus {
    private String name;
    private int seatCapacity;
    private boolean[] seats;

    public Bus(String name, int seatCapacity) {
        this.name = name;
        this.seatCapacity = seatCapacity;
        this.seats = new boolean[seatCapacity];
    }

    public String getName() {
        return name;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public boolean hasAvailableSeat(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= seatCapacity) {
            return !seats[seatNumber - 1];
        }
        return false;
    }

    public void bookSeat(int seatNumber) {
        if (seatNumber >= 1 && seatNumber <= seatCapacity) {
            seats[seatNumber - 1] = true;
        }
    }
}

class Ticket {
    private Bus bus;
    private String passengerName;
    private int seatNumber;

    public Ticket(Bus bus, String passengerName, int seatNumber) {
        this.bus = bus;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
    }
}