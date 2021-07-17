package com.github.ya64.IrwinBot;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class IrwinLibrary {
    private static volatile List<Integer> irwinNums;
    private static final File irwinDir = new File("irwins");

    /**
     * Initialization method. Creates the ./irwins directory if none exists and also indexes irwins
     */
    public static synchronized void init() {
        if (!irwinDir.isDirectory()) {
            if (!irwinDir.mkdir()) {
                throw new IOError(new IOException("Could not create directory " + irwinDir.getPath()));
            }
            System.out.println("An irwins folder was created. Add some irwins and restart the bot, yo!");
            System.exit(0);
        }

        irwinNums = new ArrayList<>();
        for (String name : Objects.requireNonNull(irwinDir.list())) {
            int num = Irwin.getNumberFromName(name.split("_")[0]);
            if (num != -1) {
                irwinNums.add(num);
            }
        }
    }

    /**
     * Returns the file name of an Irwin given a directory and a number to look for
     * @param num The number of the Irwin
     * @return The name of the Irwin file, or {@code null} if no Irwin with that name was found
     */
    private static String getIrwinName(int num) {
        if (irwinNums.stream().noneMatch(n -> n == num)) {
            return null;
        }

        try {
            for (String name : Objects.requireNonNull(irwinDir.list())) {
                if (num == Irwin.getNumberFromName(name.split("_")[0])) {
                    return name;
                }
            }
        } catch (NullPointerException e) {
            return null;
        }

        return null;
    }

    /**
     * Gets a random Irwin. Will first try to get an HD Irwin but will return a normal irwin if no HD ones are found.
     * @return An {@link Irwin}, or null if no irwins were found
     */
    public static synchronized Irwin getIrwin(int num) {
        String fileName = getIrwinName(num);
        if (fileName == null) {
            return null;
        }

        return new Irwin(fileName);
    }

    /**
     * Gets a random Irwin. Will first try to get an HD Irwin but will return a normal irwin if no HD ones are found.
     * @return An {@link Irwin}, or null if no irwins were found
     */
    public static synchronized Irwin getIrwin() {
        return getIrwin(irwinNums.get((int) (Math.random() * irwinNums.size())));
    }
}
