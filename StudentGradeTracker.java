
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGradeTracker extends JFrame {

    private JTextField nameField, marksField;
    private JTable studentTable;
    private DefaultTableModel model;

    private JLabel totalLabel, averageLabel;
    private JLabel highestLabel, lowestLabel;

    private final Color blue = new Color(45, 105, 220);
    private final Color background = new Color(243, 247, 252);

    public StudentGradeTracker() {
        setTitle("Student Grade Tracker");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(background);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(blue);
        header.setBorder(
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        JLabel title = new JLabel("Student Grade Tracker");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Manage student records and performance"
        );
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 0, 5));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Main content
        JPanel main = new JPanel(new BorderLayout(12, 15));
        main.setBackground(background);
        main.setBorder(
                BorderFactory.createEmptyBorder(0, 20, 20, 20)
        );

        // Input form
        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 15));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createTitledBorder("Add Student"));

        nameField = new JTextField(13);
        marksField = new JTextField(7);

        JButton addButton = createButton("Add Student", blue);
        JButton deleteButton = createButton(
                "Delete Selected", new Color(220, 70, 70)
        );
        JButton reportButton = createButton(
                "Summary Report", new Color(35, 145, 100)
        );

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(new JLabel("Marks (0-100):"));
        form.add(marksField);
        form.add(addButton);
        form.add(deleteButton);
        form.add(reportButton);

        main.add(form, BorderLayout.NORTH);

        // Student table
        String[] columns = {
                "Student Name", "Marks", "Grade"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        studentTable = new JTable(model);
        studentTable.setRowHeight(30);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 14));
        studentTable.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 14)
        );
        studentTable.getTableHeader().setBackground(
                new Color(225, 235, 250)
        );

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(
                BorderFactory.createTitledBorder("Student Records")
        );

        main.add(scrollPane, BorderLayout.CENTER);

        // Statistics dashboard
        JPanel stats = new JPanel(new GridLayout(1, 4, 12, 0));
        stats.setBackground(background);

        totalLabel = new JLabel("0", SwingConstants.CENTER);
        averageLabel = new JLabel("0.00", SwingConstants.CENTER);
        highestLabel = new JLabel("N/A", SwingConstants.CENTER);
        lowestLabel = new JLabel("N/A", SwingConstants.CENTER);

        stats.add(createCard("Total Students", totalLabel));
        stats.add(createCard("Average Marks", averageLabel));
        stats.add(createCard("Highest Marks", highestLabel));
        stats.add(createCard("Lowest Marks", lowestLabel));

        main.add(stats, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);

        // Button actions
        addButton.addActionListener(e -> addStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        reportButton.addActionListener(e -> showReport());

        getRootPane().setDefaultButton(addButton);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        );
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );
        return button;
    }

    private JPanel createCard(String title, JLabel value) {
        JPanel card = new JPanel(new GridLayout(2, 1, 0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 228, 240)
                        ),
                        BorderFactory.createEmptyBorder(12, 5, 12, 5)
                )
        );

        JLabel heading = new JLabel(title, SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.PLAIN, 13));

        value.setFont(new Font("Arial", Font.BOLD, 21));
        value.setForeground(blue);

        card.add(heading);
        card.add(value);
        return card;
    }

    private void addStudent() {
        String name = nameField.getText().trim();
        String marksText = marksField.getText().trim();

        if (name.isEmpty() || marksText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please enter both name and marks."
            );
            return;
        }

        double marks;

        try {
            marks = Double.parseDouble(marksText);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this, "Marks must be a valid number."
            );
            return;
        }

        if (!Double.isFinite(marks) || marks < 0 || marks > 100) {
            JOptionPane.showMessageDialog(
                    this, "Marks must be between 0 and 100."
            );
            return;
        }

        model.addRow(new Object[] {
                name, marks, getGrade(marks)
        });

        nameField.setText("");
        marksField.setText("");
        nameField.requestFocus();

        updateStatistics();
    }

    private String getGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a student to delete."
            );
            return;
        }

        model.removeRow(
                studentTable.convertRowIndexToModel(selectedRow)
        );

        updateStatistics();
    }

    private void updateStatistics() {
        int count = model.getRowCount();
        totalLabel.setText(String.valueOf(count));

        if (count == 0) {
            averageLabel.setText("0.00");
            highestLabel.setText("N/A");
            lowestLabel.setText("N/A");
            return;
        }

        double total = 0;
        double highest = -1;
        double lowest = 101;

        String highestName = "";
        String lowestName = "";

        for (int i = 0; i < count; i++) {
            String name = model.getValueAt(i, 0).toString();
            double marks = Double.parseDouble(
                    model.getValueAt(i, 1).toString()
            );

            total += marks;

            if (marks > highest) {
                highest = marks;
                highestName = name;
            }

            if (marks < lowest) {
                lowest = marks;
                lowestName = name;
            }
        }

        averageLabel.setText(
                String.format("%.2f", total / count)
        );

        highestLabel.setText(
                String.format("%.1f (%s)", highest, highestName)
        );

        lowestLabel.setText(
                String.format("%.1f (%s)", lowest, lowestName)
        );
    }

    private void showReport() {
        int count = model.getRowCount();

        if (count == 0) {
            JOptionPane.showMessageDialog(
                    this, "No student records available."
            );
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("STUDENT SUMMARY REPORT\n");
        report.append("==============================\n\n");

        for (int i = 0; i < count; i++) {
            report.append("Name: ")
                    .append(model.getValueAt(i, 0))
                    .append("\nMarks: ")
                    .append(model.getValueAt(i, 1))
                    .append("\nGrade: ")
                    .append(model.getValueAt(i, 2))
                    .append("\n\n");
        }

        report.append("Total Students: ").append(count);
        report.append("\nAverage Marks: ")
                .append(averageLabel.getText());
        report.append("\nHighest: ").append(highestLabel.getText());
        report.append("\nLowest: ").append(lowestLabel.getText());

        JTextArea textArea = new JTextArea(report.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setCaretPosition(0);

        JScrollPane pane = new JScrollPane(textArea);
        pane.setPreferredSize(new Dimension(450, 400));

        JOptionPane.showMessageDialog(
                this, pane, "Summary Report",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentGradeTracker app = new StudentGradeTracker();
            app.setVisible(true);
        });
    }
}
