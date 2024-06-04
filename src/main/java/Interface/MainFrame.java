package Interface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import classes.Resident;
import classes.Specialization;
import classes.Hospital;
import db.Database;
import lombok.Getter;
import lombok.Setter;
import main.Main;

@Getter
@Setter
public class MainFrame extends JFrame {
    ControlPanel controlPanel;
    JTextArea textArea;
    public MainFrame() {
        super("Hospital assigner");
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set the background color for the entire frame
        getContentPane().setBackground(new Color(173, 216, 230));

        controlPanel = new ControlPanel(this);
        stylePanel(controlPanel);
        add(controlPanel, BorderLayout.WEST);

        textArea = new JTextArea();
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(new Dimension(400, 200));
        add(scrollPane, BorderLayout.CENTER);

        pack();
        setSize(1000, 500);
    }

    public void seeHospitals() {
        List<Hospital> hospitals = fetchHospitals();
        StringBuilder hospitalsText = new StringBuilder();

        for (Hospital hospital : hospitals) {
            hospitalsText.append("ID: ").append(hospital.getHospital_id())
                    .append(", Name: ").append(hospital.getName())
                    .append(", Rating: "). append(hospital.getGrade())
                    .append(", Capacity: "). append(hospital.getCapacity())
                    .append(", Specializations: ").append(hospital.getSpecialization())
                    .append("\n");
        }

        textArea.setText(hospitalsText.toString());
    }

    private List<Hospital> fetchHospitals() {
        return Database.getAllHospitals();
    }

    public void seeResidents() {
        List<Resident> residents = fetchResidents();
        StringBuilder residentsText = new StringBuilder();

        for (Resident resident : residents) {
            residentsText.append("ID: ").append(resident.getResident_id())
                    .append(", Name: ").append(resident.getName())
                    .append(", Grade: ").append(resident.getGrade())
                    .append(", Specialization: ").append(resident.getSpecialization())
                    .append("\n");
        }

        textArea.setText(residentsText.toString());
    }

    private List<Resident> fetchResidents() {
        return Database.getAllResidents();
    }

    public void getMatchingR() {
        JDialog residentDialog = new JDialog(this, "Sign up as a resident!", true);
        residentDialog.setSize(300, 400);

        // Using GridLayout for the dialog
        residentDialog.setLayout(new GridLayout(7, 1, 0, 5));
        residentDialog.getContentPane().setBackground(new Color(173, 216, 230));
        // Creating labels and text fields
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel gradeLabel = new JLabel("Grade:");
        JTextField gradeField = new JTextField();
        JLabel specializationLabel = new JLabel("Preferred specializations:");
        JTextField specializationField = new JTextField();

        styleLabel(nameLabel);
        styleLabel(gradeLabel);
        styleLabel(specializationLabel);
        styleTextField(nameField);
        styleTextField(gradeField);
        styleTextField(specializationField);

        // Submit button
        JButton submitButton = new JButton("Submit");
        styleButton(submitButton);

        submitButton.addActionListener((x) -> {
            String nameText = nameField.getText();
            int gradeText = Integer.parseInt(gradeField.getText());
            String specializationsText = specializationField.getText();

            List<Specialization> specializations = new ArrayList<>();
            String[] specializationsArray = specializationsText.split(",");

            for (String spec : specializationsArray) {
                specializations.add(new Specialization(spec.trim()));
            }

            Resident resident = new Resident(nameText, gradeText, specializations);
            int id = Database.addResident(resident, specializationsText);

            Database.makePairings();
            textArea.setText(Database.getPairingR(id));

            residentDialog.dispose();
        });

        // Adding components to the dialog
        residentDialog.add(nameLabel);
        residentDialog.add(nameField);
        residentDialog.add(gradeLabel);
        residentDialog.add(gradeField);
        residentDialog.add(specializationLabel);
        residentDialog.add(specializationField);
        residentDialog.add(submitButton);
        center(residentDialog);
        residentDialog.setVisible(true);
    }

    public void getMatchingH() {
        JDialog hospitalDialog = new JDialog(this, "Add a Hospital", true);
        hospitalDialog.setSize(300, 400);

        hospitalDialog.setLayout(new GridLayout(9, 1, 0, 5));
        hospitalDialog.getContentPane().setBackground(new Color(173, 216, 230));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel capLabel = new JLabel("Capacity:");
        JTextField capacityField = new JTextField();
        JLabel gradeLabel = new JLabel("Grade:");
        JTextField gradeField = new JTextField();
        JLabel specLabel = new JLabel("Specializations:");
        JTextField specializationField = new JTextField();
        JButton submitButton = new JButton("Submit");

        styleLabel(nameLabel);
        styleLabel(capLabel);
        styleLabel(gradeLabel);
        styleLabel(specLabel);
        styleTextField(nameField);
        styleTextField(capacityField);
        styleTextField(gradeField);
        styleTextField(specializationField);
        styleButton(submitButton);

        submitButton.addActionListener((x) -> {
            String name = nameField.getText();
            int capacity = Integer.parseInt(capacityField.getText());
            int grade = Integer.parseInt(gradeField.getText());
            String specializationsText = specializationField.getText();

            List<Specialization> specializations = new ArrayList<>();
            String[] specializationsArray = specializationsText.split(",");

            for (String spec : specializationsArray) {
                specializations.add(new Specialization(spec.trim()));
            }

            Hospital hospital = new Hospital(name, capacity, grade, specializations);
            int id = Database.addHospital(hospital, specializationsText);
            Database.makePairings();
            textArea.setText(Database.getPairingH(id));

            hospitalDialog.dispose();
        });

        // Adding components to the dialog
        hospitalDialog.add(nameLabel);
        hospitalDialog.add(nameField);
        hospitalDialog.add(capLabel);
        hospitalDialog.add(capacityField);
        hospitalDialog.add(gradeLabel);
        hospitalDialog.add(gradeField);
        hospitalDialog.add(specLabel);
        hospitalDialog.add(specializationField);
        hospitalDialog.add(submitButton);

        center(hospitalDialog);

        hospitalDialog.setVisible(true);
    }

    public void seeMatches() {
        textArea.setText(Database.getAllMatchings());
    }


    public static void center(Container c)
    {
        Container parent = c.getParent();
        if (parent == null) {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            c.setLocation(screenSize.width/2 - c.getWidth()/2, screenSize.height/2 - c.getHeight()/2);
            return;
        }
        Point parentLocation = parent.getLocation();
        c.setLocation((int)parentLocation.getX() + parent.getWidth()/2 - c.getWidth()/2,
                (int)parentLocation.getY() + parent.getHeight()/2 - c.getHeight()/2);
    }
    private static void stylePanel(JPanel panel) {
        panel.setBorder(new EmptyBorder(40, 20, 40, 20));
        panel.setBackground(new Color(25, 25, 112));
    }

    private static void styleLabel(JLabel label) {
        label.setBorder(new EmptyBorder(0,10,0,10));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.BLACK);
    }


    private static void styleTextField(JTextField textField) {
        textField.setBorder(new EmptyBorder(0,10,0,10));
        textField.setFont(new Font("Arial", Font.PLAIN, 12));
        textField.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        textField.setBackground(new Color(240, 248, 255));
        textField.setForeground(new Color(25, 25, 112));
    }

    private static void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(new Color(240, 248, 255));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(25, 25, 112), 1));
    }

    public void insertSample() {
        try {
            Main.insertDummy(Database.getInstance());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Failed to insert", JOptionPane.ERROR_MESSAGE);
        }
    }
}

