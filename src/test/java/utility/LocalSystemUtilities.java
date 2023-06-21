package utility;

import helper.Log;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class LocalSystemUtilities {

    private static Clipboard getSystemClipboard() {
        return Toolkit.getDefaultToolkit().getSystemClipboard();
    }

    public static String getSystemClipboardAsString() throws HeadlessException, UnsupportedFlavorException, IOException {
        return (String) getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
    }

    public static Image getImageFromClipboard(Clipboard clipboard) throws IOException, UnsupportedFlavorException {
        return (Image) clipboard.getData(DataFlavor.imageFlavor);
    }

    public static void clearSystemClipboard() {
        getSystemClipboard().setContents(new StringSelection(""), null);
    }

    private static void killApplication(String processName) {
        Runtime rt = Runtime.getRuntime();
        Log.info("Attempting to kill application process: " + processName);
        try {
            String[] killProcess = ArrayUtils.toArray("taskkill /F /IM " + processName);
            rt.exec(killProcess);
            while (isProcessRunning(processName)) {
                TimeLimit.waitFor(2);
                Log.debug("Running commands: %s" + Arrays.toString(killProcess));
                rt.exec(killProcess);
            }
            Log.info(processName + " has now been killed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isProcessRunning(String processName) {
        ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
        Log.debug("ProcessBuilder created");
        Process process = null;
        String tasksList = "";
        try {
            Log.debug("Checking what system processes are running");
            Log.debug("Starting new process");
            process = processBuilder.start();
            tasksList = toString(process.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Objects.requireNonNull(process).destroy();
            Log.debug("Process destroyed");
        }
        Log.debug("Process: " + processName + " running = " + tasksList.contains(processName));
        return tasksList.contains(processName);
    }

    private static String toString(InputStream inputStream) {
        String value;
        try (Scanner scanner = new Scanner(inputStream)){
            scanner.useDelimiter("\\A");
            value = scanner.hasNext() ? scanner.next() : "";
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

}
