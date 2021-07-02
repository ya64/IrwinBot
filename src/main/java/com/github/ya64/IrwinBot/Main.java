package com.github.ya64.IrwinBot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDA;

import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Properties;

/**
 * The bot of irwins
 */
public class Main {
    /** The prefix of the bot, defaults to '!' */
    private static String prefix;
    private static JDA jda;

    public static void main(String[] args) {
        String token = null;
        File propertiesFile = new File("configuration.properties");
        Properties properties = new Properties();
        try (FileInputStream propertiesInputStream = new FileInputStream(propertiesFile)) {
            properties.load(propertiesInputStream);
            token = properties.getProperty("token");
            prefix = properties.getProperty("prefix", "!");
        } catch (FileNotFoundException e) {
            try {
                propertiesFile.createNewFile();
                FileOutputStream propertiesOutputStream = new FileOutputStream(propertiesFile);
                properties.setProperty("token", "");
                properties.setProperty("prefix", "!");
                properties.store(propertiesOutputStream, "Refferr to the README file for more information");
            } catch (IOException ex) {
                throw new IOError(ex);
            }
        } catch (IOException ex) {
        throw new IOError(ex);
        }

        if (token == null) {
            System.out.println("Please put a token into configuration.properties and try again");
            System.exit(0);
        }

        System.out.println("Indexing the Irwins (this might take a while if there's a lot of them) . . .");
        IrwinLibrary.init();

        System.out.println("Logging into Discord (Don't worry about any SLF4J Errors) . . .");
        try {
            jda = JDABuilder.createDefault(token)
                    .addEventListeners(new Listener())
                    .build()
                    .awaitReady();
        } catch (LoginException e) {
            System.out.println("Error logging into Discord. Please check to make sure your tokens correct and try again.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        (new Thread(new Terminal())).start();
        System.out.println("The bots up and ready to go, yo! You'd need to restart the bot after you add/delete Irwins.\nIt's also reccomended to back up all irwins in a seperate folder, just in case.\nEnter help for a list of commands you can run from the terminal.");
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