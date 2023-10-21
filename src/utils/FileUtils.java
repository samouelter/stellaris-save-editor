package utils;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {


    //Prompt for choosing the savefile to edit
    public String chooseSaveFileLocation() {
        System.out.print("Selecting savefile\n");
        String defaultPath = System.getProperty("user.home") + "\\Documents\\Paradox Interactive\\Stellaris\\save games";
        JFileChooser fileChooser = new JFileChooser(defaultPath);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Stellaris savefile", "sav", "text");
        fileChooser.setFileFilter(filter);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            String saveFile = fileChooser.getSelectedFile().getAbsolutePath();
            System.out.print("Savefile selected : " + saveFile + "\n");
            return saveFile;
        } else {
            System.out.print("Error selecting savefile\n");
            return null;
        }
    }

    //Return the content of the gamestate into a String
    public String readGamestate(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    //Update the gamestate with the edited data
    public void writeSavefile(String saveLocation, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(saveLocation, "gamestate")));
        writer.write(data);

        writer.close();
    }

    public void cleanUp(String saveLocation) throws IOException {
        System.out.println("Cleaning up save directory");
        if (Files.deleteIfExists(new File(saveLocation, "gamestate").toPath())) {
            System.out.println("Gamestate deleted");
        } else {
            System.out.println("Failed to delete gamestate file!");
        };
        if (Files.deleteIfExists(new File(saveLocation, "meta").toPath())) {
            System.out.println("Meta deleted");
        } else {
            System.out.println("Failed to delete meta file!");
        };
        Files.deleteIfExists(new File(saveLocation, "meta").toPath());

    }

}
