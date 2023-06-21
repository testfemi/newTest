package utility;

import helper.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileHandler {

    /**
     * Creates an empty file within the given {@param directory} with the given
     * {@param fileName}.
     *
     * @param directory
     *            - The directory to create the file in.
     * @param fileName
     *            - The name to assign the file.
     * @return The newly created file as a {@link File} object.
     * @throws IOException if folder cannot/is not created successfully
     */
    public static File createFile(String directory, String fileName) throws IOException {
        File file = new File(String.format("%s/%s", createFolder(directory).getAbsolutePath(), fileName));
        FileUtils.touch(file);
        return file;
    }

    /**
     * Creates an empty file at the given {@param path}.
     *
     * @param path where to create this file.
     * @return The newly created file as a {@link File} object.
     */
    public static File createFile(String path) {
        return new File(path);
    }

    public static String getFileType(String filePath) throws IOException {
        return Files.probeContentType(new File(filePath).toPath());
    }

    public static String getFileExtension(String filePath) {
        String extension = FilenameUtils.getExtension(filePath);
        return extension == null || extension.isEmpty()
                ? null
                : extension;
    }

    /**
     * Returns the file name (including prefix and suffix).  For Example, for the
     * absolute path of "C:/dev/project/file.txt", this method would return
     * "file.txt".
     *
     * @param filePath the path to the file
     * @return the name of the file as a {@link String}
     */
    public static String getFileName(String filePath) {
        return FilenameUtils.getName(filePath);
    }

    public static String getBaseName(String filePath) {
        return FilenameUtils.getBaseName(filePath);
    }

    public static String getBaseName(File file) {
        return FilenameUtils.getBaseName(file.getName());
    }

    public static String getNativePath(String filePath) {
        return FilenameUtils.getFullPath(filePath);
    }

    public static void deleteFile(String filePath) {
        FileUtils.deleteQuietly(new File(filePath));
    }

    /**
     * Creates a new folder directory at the given {@param path}.
     *
     * @param path
     *           - The path to create this new file.
     * @return The newly created folder directory as a {@link File} object
     * @throws IOException if folder cannot be created
     */
    public static File createFolder(String path) throws IOException {
        File fileLocation = new File(path);
        if(!fileLocation.mkdirs()){
            throw new IOException("Failed to create new directory at \"" + path + "\"");
        }
        return fileLocation;
    }

    public static boolean fileExists(String filePath){
        return new File(filePath).exists();
    }

    public static boolean directoryExists(String directoryPath){
        return new File(directoryPath).exists();
    }

    public static String generateUniqueFileName(String filePath) throws IOException {
        return generateUniqueFileName(getNativePath(filePath), getBaseName(filePath), getFileExtension(filePath));
    }

    public static String generateUniqueFileName(String dir, String filename, String fileExtension) throws IOException {
        File currentName = new File(
                String.format("%s/%s%s",
                        dir,
                        filename,
                        fileExtension == null ? "" : fileExtension));

        // Test given filename as a temp file
        try{
            /*Will throw exception if file cannot be created with this unique filename for
            some reason (e.g., filename may contain illegal characters)*/
            File.createTempFile(filename, fileExtension);
        }catch(IOException e) {
            Log.error("Cannot create temporary file with the following filename: \"%s%s\".  " +
                    "Please check that this filename is acceptable",
                    filename,
                    fileExtension);
            throw e;
        }

        //Generate unique filename
        int counter = 2;
        String unique = filename;
        while(true){
            if(currentName.exists()){ //If file already exists, increment filename with counter
                unique = String.format("%s(%d)", filename, counter);
                currentName = new File(String.format("%s/%s%s",
                        dir,
                        unique,
                        fileExtension == null ? "" : fileExtension));
                counter++;
            }else { // Test unique filename as a temp file
                try{
                    /*Will throw exception if file cannot be created with this unique filename for
                    some reason (e.g., filename may contain illegal characters)*/
                    File.createTempFile(unique, fileExtension);
                }catch(IOException e) {
                    Log.error("Cannot create unique filename with the following: \"%s%s\"",
                            unique,
                            fileExtension);
                    throw e;
                }
                return unique;
            }
        }
    }

    public static void zipFile(File dir) {
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dir))) {
            zos.putNextEntry(new ZipEntry(dir.getName()));
            byte[] bytes = Files.readAllBytes(Paths.get(dir.getAbsolutePath()));
            zos.write(bytes, 0, bytes.length);
            zos.closeEntry();
            // zos.close();
        } catch (FileNotFoundException ex) {
            System.err.format("The file %s does not exist", dir);
        } catch (IOException ex) {
            System.err.println("The following error occurred during ZIP compression: " + ex);
        }
    }

    public static void zipFile(String dirPath) {
        zipFile(new File(dirPath));
    }


}