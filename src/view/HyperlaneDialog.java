package view;

import service.HyperlaneService;
import service.StarSystemService;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HyperlaneDialog extends JFrame implements ActionListener {

    List<String> starSystems = new ArrayList<>();
    private String saveLocation;
    private String gamestateContent;
    private final FileUtils fileUtils = new FileUtils();
    private final StarSystemService starSystemService = new StarSystemService();
    JLabel planetALabel = new JLabel("ID Planet A");
    JLabel planetBLabel = new JLabel("ID Planet B");
    JTextField planetAId = new JTextField(4);
    JTextField planetBId = new JTextField(4);
    JButton add = new JButton("Add");

    JButton remove = new JButton("Remove");

    JButton save = new JButton("Save");
    JButton exit = new JButton("Exit without saving");



    public HyperlaneDialog(String saveLocation) {

        try {
            this.saveLocation = saveLocation;
            gamestateContent = fileUtils.readGamestate(saveLocation +  "\\gamestate", StandardCharsets.UTF_8);
            starSystems = starSystemService.toStarSystemList(gamestateContent);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            dispose();
        }


        add.setActionCommand("add");
        add.addActionListener(this);

        remove.setActionCommand("remove");
        remove.addActionListener(this);

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
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(add, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(remove, constraints);

        constraints.gridx = 4;
        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(save, constraints);

        constraints.gridx = 4;
        constraints.gridy = 3;
        constraints.anchor = GridBagConstraints.CENTER;

        newPanel.add(exit, constraints);

        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Add hyperlane Window"));

        add(newPanel);

        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("add")) {
            try {
                int idA = Integer.parseInt(planetAId.getText());
                int idB = Integer.parseInt(planetBId.getText());
                if (idA > starSystems.size() || idB > starSystems.size()) {
                    throw new Exception();
                }
                System.out.println("Adding hyperlane between systems " + idA + " and " + idB);
                gamestateContent = HyperlaneService.addHyperlane(gamestateContent,
                        idA, starSystems.get(idA),
                        idB, starSystems.get(idB)
                );
                planetAId.setText("");
                planetBId.setText("");
                System.out.println("Hyperlane between systems " + idA + " and " + idB + " added");
            } catch (Exception nbe) {
                System.out.println("Incorrect system Id");
            }
        } else if (command.equals("remove")) {
                try {
                    int idA = Integer.parseInt(planetAId.getText());
                    int idB = Integer.parseInt(planetBId.getText());
                    if (idA > starSystems.size() || idB > starSystems.size()) {
                        throw new Exception();
                    }
                    System.out.println("Removing hyperlane between systems " + idA + " and " + idB);
                    gamestateContent = HyperlaneService.removeHyperlane(gamestateContent,
                            idA, starSystems.get(idA),
                            idB, starSystems.get(idB)
                    );
                    planetAId.setText("");
                    planetBId.setText("");
                    System.out.println("Hyperlane between systems " + idA + " and " + idB + " removed");
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
            System.out.println("Closing hyperlane window");
            dispose();
        }
    }

}