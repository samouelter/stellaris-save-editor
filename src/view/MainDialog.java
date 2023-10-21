package view;

import utils.ArchiveUtils;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class MainDialog extends JFrame implements ActionListener {

    private final ArchiveUtils archiveUtils = new ArchiveUtils();
    private final FileUtils fileUtils = new FileUtils();

    private final String saveLocation;
    JTextField newSaveName = new JTextField();

    public MainDialog(String saveFile, String saveLocation) {

        this.saveLocation = saveLocation;

        setBounds(100, 100, 450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel upPanel = new JPanel();
        upPanel.setLayout(new GridLayout(2,2));

        JLabel loadedSaveLabel = new JLabel("Loaded save : ");
        upPanel.add(loadedSaveLabel);

        JLabel saveFileLabel = new JLabel(trimSaveFileName(saveFile));
        upPanel.add(saveFileLabel);
        JLabel newSaveLabel = new JLabel("New save name :");
        upPanel.add(newSaveLabel);

        newSaveName.setText("editedSave");
        upPanel.add(newSaveName);

        JPanel midPanel = new JPanel();
        midPanel.setLayout(new GridLayout(1,1));

        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new GridLayout(3,1));

        JButton swap = new JButton("Swap systems");
        swap.setActionCommand("swap");
        swap.addActionListener(this);
        JButton hyperlane = new JButton("Hyperlane Editor");
        hyperlane.setActionCommand("hyperlane");
        hyperlane.addActionListener(this);
        JButton save = new JButton("Save");
        save.setActionCommand("save");
        save.addActionListener(this);
        JButton exit = new JButton("Exit without saving");
        exit.setActionCommand("exit");
        exit.addActionListener(this);

        eastPanel.add(swap);
        eastPanel.add(hyperlane);
        eastPanel.add(save);
        eastPanel.add(exit);


        getContentPane().add(upPanel, BorderLayout.NORTH);
        getContentPane().add(midPanel, BorderLayout.WEST);
        getContentPane().add(eastPanel, BorderLayout.EAST);

        pack();

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals("save")) {
            System.out.println("Saving");
            try {
                archiveUtils.zipFiles(saveLocation, newSaveName.getText());
                fileUtils.cleanUp(saveLocation);
                System.out.println("Save " + newSaveName.getText() + " created at " + saveLocation);
                System.out.println("Have a nice game :)");
                dispose();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (command.equals("swap")) {
            System.out.println("Opening swapping window");
            new PlanetSwapDialog(saveLocation);
        }
        if (command.equals("hyperlane")) {
            System.out.println("Opening hyperlane window");
            new HyperlaneDialog(saveLocation);
        }
        if (command.equals("exit")) {
            try {
                fileUtils.cleanUp(saveLocation);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Exited without saving");
            dispose(); // Close the dialog
        }
    }

    public String trimSaveFileName(String savefile) {
        if (savefile.length() > 25) {
            if (savefile.contains("\\save games\\")) {
                return savefile.substring(savefile.indexOf("\\save games\\") + "\\save games\\".length());
            } else {
                return savefile.substring(savefile.length() - 25);
            }
        } else {
            return savefile;
        }
    }

}