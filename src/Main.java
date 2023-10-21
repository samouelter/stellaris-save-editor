import utils.ArchiveUtils;
import utils.FileUtils;
import view.MainDialog;

import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Stellaris saveditor v1.0");
        System.out.println("Made by samouelter");
        System.out.println("https://github.com/samouelter/stellaris-save-editor");

        //Asking the user for his savefile
        FileUtils fileUtils = new FileUtils();
        ArchiveUtils archiveUtils = new ArchiveUtils();
        String savefile = fileUtils.chooseSaveFileLocation();
        String saveLocation = new File(savefile).getParent();

        //Getting the gamestate and meta file from the sav
        archiveUtils.unzipFile(savefile, saveLocation);

        //Lauching the editor window
        new MainDialog(savefile, saveLocation);
    }

}