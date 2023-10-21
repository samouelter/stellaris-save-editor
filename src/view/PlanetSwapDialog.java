package view;

import service.StarSystemService;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PlanetSwapDialog extends JFrame implements ActionListener {

    List<String> starSystems = new ArrayList<>();
    private String saveLocation;
    private String gamestateContent;
    private final FileUtils fileUtils = new FileUtils();
    private final StarSystemService starSystemService = new StarSystemService();
    JLabel planetALabel = new JLabel("ID Planet A");
    JLabel planetBLabel = new JLabel("ID Planet B");
    JTextField planetAId = new JTextField(4);
    JTextField planetBId = new JTextField(4);
    JButton swap = new JButton("Swap");
    JButton save = new JButton("Save");
    JButton exit = new JButton("Exit without saving");



    public PlanetSwapDialog(String saveLocation) {

        try {
            this.saveLocation = saveLocation;
            gamestateContent = fileUtils.readGamestate(saveLocation +  "\\gamestate", StandardCharsets.UTF_8);
            starSystems = starSystemService.toStarSystemList(gamestateContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            dispose();
        }


        swap.setActionCommand("swap");
        swap.addActionListener(this);

        save.setActionCommand("save");
        save.addActionListener(this);

        exit.setActionCommand("exit");
        exit.addActionListener(this);

        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(planetALabel, constraints);

        constraints.gridx = 1;
        newPanel.add(planetAId, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(planetBLabel, constraints);

        constraints.gridx = 1;
        newPanel.add(planetBId, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 6;
        constraints.anchor = GridBagConstraints.WEST;
        newPanel.add(swap, constraints);

        constraints.gridx = 3;
        constraints.gridy = 2;
        constraints.gridwidth = 6;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(save, constraints);

        constraints.gridx = 10;
        constraints.gridy = 2;
        constraints.gridwidth = 10;
        constraints.anchor = GridBagConstraints.EAST;

        newPanel.add(exit, constraints);

        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Swap Window"));

        add(newPanel);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("swap")) {
            try {
                int idA = Integer.parseInt(planetAId.getText());
                int idB = Integer.parseInt(planetBId.getText());
                if (idA > starSystems.size() || idB > starSystems.size()) {
                    throw new Exception();
                }
                System.out.println("Swapping systems " + idA + " and " + idB);
                gamestateContent = StarSystemService.swapStarSystem(gamestateContent,
                        idA, starSystems.get(idA),
                        idB, starSystems.get(idB)
                );
                planetAId.setText("");
                planetBId.setText("");
                System.out.println("Systems " + idA + " and " + idB + " swapped");
            } catch ( Exception nbe ){
                System.out.println("Incorrect system Id");
            }

        } else if (command.equals("save")) {
            System.out.println("Saving gamestate");
            try {
                fileUtils.writeSavefile(saveLocation, gamestateContent);
                System.out.println("Gamestate saved");
                dispose();
            } catch (IOException ex) {
                System.out.println("Error while saving gamestate : " + ex.getMessage());
                System.out.println("Please report !");
            }
        } else if (command.equals("exit")) {
            System.out.println("Closing swapping window");
            dispose();
        }
    }

}