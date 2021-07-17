package com.github.ya64.IrwinBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.util.Properties;

/**
 * The grand bot of all the irwins!
 * @version 1.1.0
 */
public class Main {
    /** The prefix of the bot, defaults to '+' */
    private static String prefix;
    private static JDA jda;

    public static void main(String[] args) {
        String token = null;
        File propertiesFile = new File("configuration.properties");
        Properties properties = new Properties();
        try (FileInputStream propertiesInputStream = new FileInputStream(propertiesFile)) {
            properties.load(propertiesInputStream);
            token = properties.getProperty("token");
            prefix = properties.getProperty("prefix", "+");
        } catch (FileNotFoundException e) {
            try {
                propertiesFile.createNewFile();
                FileOutputStream propertiesOutputStream = new FileOutputStream(propertiesFile);
                properties.setProperty("token", "");
                properties.setProperty("prefix", "+");
                properties.store(propertiesOutputStream, "Refer to the README file for more information");
            } catch (IOException ex) {
                throw new IOError(ex);
            }
        } catch (IOException ex) {
            throw new IOError(ex);
        }

        if (token == null) {
            System.out.println("Please put a token into configuration.properties and try again yo");
            System.exit(0);
        }

        System.out.println("Indexing the Irwins (this might take a while if there's a lot of them yo) . . .");
        IrwinLibrary.init();

        System.out.println("Logging into Discord (Don't worry about any SLF4J Errors yo) . . .");
        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new Listener())
                    .build()
                    .awaitReady();
        } catch (LoginException e) {
            System.out.println("Error logging into Discord. Please check to make sure your tokens correct and try again yo.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        (new Thread(new Terminal())).start();
        System.out.println("I am up ready to go, yo! I need to be restarted after you add/delete Irwins.\nIt's also reccomended to back up all irwins in a seperate folder, just in case yo.\nEnter help for a list of commands you can run from the terminal yo.");
    }

    /**
     * Gets the prefix of the bot
     * @return the prefix
     */
    public static String getPrefix() {
        return prefix;
    }

    /**
     * Exits the bot
     */
    public static void exit() {
        System.out.println("Logging out of Discord . . .");
        jda.shutdown();
        System.exit(0);
    }
}
