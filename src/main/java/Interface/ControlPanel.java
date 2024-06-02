package Interface;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {
    final MainFrame frame;

    JButton hospBtn = new JButton("Hospitals");
    JButton resBtn = new JButton("Residents");
    JButton gmrBtn = new JButton("Get matching as a resident");
    JButton gmhBtn = new JButton("Get matching as a hospital");
    JButton seeMatches = new JButton("See matches");
    JButton exit = new JButton("Exit");
    JButton insertSample = new JButton("Insert sample data");
    public ControlPanel(MainFrame frame) {
        this.frame = frame; init();
    }
    private void init() {
        GridLayout mgr = new GridLayout(7, 1, 0, 10);
        setLayout(mgr);

        add(hospBtn);

        add(resBtn);

        add(gmrBtn);

        add(gmhBtn);

        add(seeMatches);

        add(insertSample);

        add(exit);

        hospBtn.addActionListener(e -> frame.seeHospitals());
        resBtn.addActionListener(e-> frame.seeResidents());
        gmrBtn.addActionListener(e -> frame.getMatchingR());
        gmhBtn.addActionListener(e -> frame.getMatchingH());
        insertSample.addActionListener(e -> frame.insertSample());
        seeMatches.addActionListener(e -> frame.seeMatches());
        exit.addActionListener(e -> frame.dispose());
    }
}
