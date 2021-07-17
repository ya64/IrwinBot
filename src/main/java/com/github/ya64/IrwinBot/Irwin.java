package com.github.ya64.IrwinBot;

import java.util.Arrays;

/**
 * A class for storing a specific Irwin drawing and its attributes
 */
public class Irwin {
    /** The number of the Irwin, which must be at the beggining of the file name */
    private final int number;

    /** The file name of the Irwin */
    private final String fileName;

    /** The parsed name of the Irwin */
    private final String name;

    public Irwin(String fileName) {
        this.number = getNumberFromName(fileName);
        this.fileName = fileName;
        this.name = parseName(fileName);
    }

    /**
     * Gets the number of the Irwin
     * @return The number of the Irwin
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets the file name of the Irwin
     * @return The file name of the Irwin
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Gets the actual name of the Irwin
     * @return The actual name of the Irwin
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the number of the Irwin derived by its file name
     * @param name The name of the Irwin
     * @return The number of the Irwin
     */
    public static int getNumberFromName(String name) {
        int number;
        String numStr = name.substring(1, name.indexOf(" "));

        try {
            number = Integer.parseInt(numStr);
        } catch (Exception e) {
            number = -1;
        }

        return number;
    }

    /**
     * Gets the actual name of the Irwin from the file name
     * @param fileName The name of the image file
     * @return The parsed name, or "???" if the name could not be parsed
     */
    public static String parseName(String fileName) {
        StringBuilder parsedIrwinNameBuilder = new StringBuilder();
        int extensionIndex = fileName.lastIndexOf(".");
        int nameStartIndex = fileName.indexOf(" ") + 1;
        if (extensionIndex == -1 || nameStartIndex == 0) {
            return "???";
        }

        Arrays.stream(fileName
                .substring(nameStartIndex, extensionIndex)
                .replaceAll("[hH][dD]", "")
                .replaceAll(" +", " ")
                .trim()
                .split(" "))
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase() + " ")
                .forEachOrdered(parsedIrwinNameBuilder::append);
        return parsedIrwinNameBuilder.toString().trim();
    }
}
