import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class AttendenceManagementSys extends JFrame implements ActionListener {
    JTextField nameField, dateField;
    JRadioButton presentBtn, absentBtn;
    JButton submitBtn, viewBtn;
    ButtonGroup statusGroup;

    public AttendenceManagementSys() {
        setTitle("Attendance Management System");
        setSize(500, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(7, 2, 10, 10));

        Color lavender = new Color(230, 230, 250);
        getContentPane().setBackground(lavender);
        setLocationRelativeTo(null); // Center window

        add(new JLabel("Student Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Date (dd-mm-yyyy):"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Attendance:"));
        JPanel radioPanel = new JPanel(new FlowLayout());
        radioPanel.setBackground(lavender);
        presentBtn = new JRadioButton("Present");
        absentBtn = new JRadioButton("Absent");
        presentBtn.setBackground(lavender);
        absentBtn.setBackground(lavender);
        statusGroup = new ButtonGroup();
        statusGroup.add(presentBtn);
        statusGroup.add(absentBtn);
        radioPanel.add(presentBtn);
        radioPanel.add(absentBtn);
        add(radioPanel);

        submitBtn = new JButton("Submit");
        submitBtn.addActionListener(this);
        add(submitBtn);

        viewBtn = new JButton("View Records");
        viewBtn.addActionListener(this);
        add(viewBtn);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitBtn) {
            saveAttendance();
        } else if (e.getSource() == viewBtn) {
            showAttendanceTable();
        }
    }

    private void saveAttendance() {
        String name = nameField.getText().trim();
        String date = dateField.getText().trim();
        String status = presentBtn.isSelected() ? "Present" : absentBtn.isSelected() ? "Absent" : "";

        if (name.isEmpty() || date.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("attendance.txt", true))) {
            writer.write(name + "," + date + "," + status);
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Attendance marked successfully!");
            nameField.setText("");
            dateField.setText("");
            statusGroup.clearSelection();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving attendance", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAttendanceTable() {
        JFrame tableFrame = new JFrame("Attendance Records");
        tableFrame.setSize(500, 300);
        tableFrame.setLocationRelativeTo(null); // Center

        String[] columns = {"Name", "Date", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);

        try (BufferedReader reader = new BufferedReader(new FileReader("attendance.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row.length == 3) {
                    model.addRow(row);
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane);
        tableFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new AttendenceManagementSys();
    }
}

