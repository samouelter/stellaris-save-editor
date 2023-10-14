import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
public class FileUtils {


    //Prompt for choosing the savefile to edit
    public String inputSaveFileLocation() {
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
    public String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    //Look through a String to return the substring between the @start and @end args
    public String extractString(String data, String start, String end) {

        int startIndex = data.indexOf(start);
        int endIndex = data.indexOf(end);

        if (startIndex >= 0 && endIndex >= 0) {
            return data.substring(startIndex, endIndex).trim();
        } else {
            return null;
        }
    }

    //Update the gamestate with the edited data
    public void writeSavefile(String saveLocation, String data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(saveLocation, "gamestate")));
        writer.write(data);

        writer.close();
    }

    //Extracting the gamestate and meta files from the savefile
    //https://www.baeldung.com/java-compress-and-uncompress
    public void unzipFile(String savefile, String savefileLocation) throws IOException {
        System.out.println("unzipping file");

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(savefile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(new File(savefileLocation), zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
        System.out.println("savefile unzipped");
    }

    //Logic function
    public static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    //Zipping back the gamestate and meta file into a .sav file
    public void zipFiles(String saveLocation, String savename) throws IOException {
        System.out.println("zipping files");
        String gamestate = saveLocation + "\\" + "gamestate";
        String metadata = saveLocation +  "\\" + "meta";
        final List<String> srcFiles = Arrays.asList(gamestate, metadata);

        final FileOutputStream fos = new FileOutputStream(Paths.get(saveLocation ).toAbsolutePath() + "\\" + savename + ".sav");
        ZipOutputStream zipOut = getZipOutputStream(fos, srcFiles);

        zipOut.close();
        fos.close();
        System.out.println("savefile zipped");
    }

    //Logic function
    private static ZipOutputStream getZipOutputStream(FileOutputStream fos, List<String> srcFiles) throws IOException {
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : srcFiles) {
            File fileToZip = new File(srcFile);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        return zipOut;
    }

}
