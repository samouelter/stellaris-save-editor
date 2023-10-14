import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("Stellaris saveditor v1.0");
        System.out.println("Made by samouelter");
        System.out.println("https://github.com/samouelter/stellaris-save-editor");

        //Asking the user for his savefile
        FileUtils fileUtils = new FileUtils();
        String savefile = fileUtils.inputSaveFileLocation();
        String saveLocation = new File(savefile).getParent();

        //Getting the gamestate and meta file from the sav
        fileUtils.unzipFile(savefile, saveLocation);

        //Lauching the editor window
        new MainDialog(savefile, saveLocation);
    }

}