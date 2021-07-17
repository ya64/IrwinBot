package com.github.ya64.IrwinBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;

public class Listener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        Message m = e.getMessage();
        if (!m.getContentRaw().trim().startsWith(Main.getPrefix()) || !e.isFromGuild()) {
            return;
        }

        String content = m.getContentRaw().trim().substring(Main.getPrefix().length());
        String[] params = content.split(" ");
        String command = params[0];
        Irwin irwin = null;

        if (command.toLowerCase().startsWith("help")) {
            m.getChannel().sendMessageEmbeds((new EmbedBuilder())
                    .setTitle("Command List")
                    .addField(Main.getPrefix() + "help", "Displays the list of commands you can use yo", true)
                    .addField(Main.getPrefix() + "irwin", "Shows a random irwin", true)
                    .addField(Main.getPrefix() + "irwin [number]", "Gets an irwin by its number", true)
                    .addField(Main.getPrefix() + "backyardirwins", "Sends a link to The Backyardirwins story on Wattpad", true)
                    .addField(Main.getPrefix() + "kermwinstale", "Sends a link to the Kermwin's Tale story on Wattpad", true)
                    .addField(Main.getPrefix() + "github", "The source code for this bot", true)
                    .addField(Main.getPrefix() + "ping", "Gets the ping", true)
                    .setFooter("Don't do " + Main.getPrefix() + "mandy")
                    .build()
            ).queue();
        } else if (command.toLowerCase().startsWith("irwin")) {
            if (params.length == 1 || getNum(params[1]) == -1) {
                irwin = IrwinLibrary.getIrwin();
                if (irwin == null) {
                    m.getChannel().sendMessage("Oops, something went wrong with getting an irwin yo :(").queue();
                    return;
                }
            } else {
                irwin = IrwinLibrary.getIrwin(getNum(params[1]));
                if (irwin == null) {
                    m.getChannel().sendMessage("Oops, seems like we couldn't find that Irwin, sorry yo :(").queue();
                    return;
                }
            }
        } else if (command.toLowerCase().startsWith("mandy")) {
            int amountOfY = (int) (Math.random() * 9) + 1;
            StringBuilder anExcessiveAmountOfYs = new StringBuilder();
            for (int i = 0; i < amountOfY; i++) {
                anExcessiveAmountOfYs.append("y");
            }
            m.getChannel().sendMessage("He" + anExcessiveAmountOfYs + " Mandy!").queue();
        } else if (command.toLowerCase().startsWith("pog")) {
            m.getChannel().sendMessage("Poggers!").queue();
        } else if (command.toLowerCase().startsWith("backyard")) {
            m.getChannel().sendMessage("https://www.wattpad.com/story/275145195-the-backyardirwins").queue();
        } else if (command.toLowerCase().startsWith("kermwin")) {
            m.getChannel().sendMessage("https://www.wattpad.com/story/277598881-kermwin%27s-tale").queue();
        } else if (command.toLowerCase().startsWith("github")) {
            m.getChannel().sendMessage("https://github.com/ya64/IrwinBot").queue();
        } else if (command.toLowerCase().startsWith("ping")) {
            long now = System.currentTimeMillis();
            String pong = Math.random() * 100 < 1 ? "Pog!" : "Pong!";
            m.getChannel().sendMessage(pong).queue(res -> res.editMessage(pong + " "+ (System.currentTimeMillis() - now) + "ms").queue());
        }

        if (irwin == null) {
            return;
        }

        String parsedFileName = irwin.getFileName().replaceAll(" ", "_").substring(1);

        m.getChannel()
         .sendFile(new File("irwins" + File.separator + irwin.getFileName()), parsedFileName)
         .setEmbeds((new EmbedBuilder())
                 .setTitle(irwin.getNumber() + " - " + irwin.getName())
                 .setImage("attachment://" + parsedFileName)
                 .build()
         ).queue();
    }

    /**
     * Gets a number from a command parameter
     * @param param The parameter
     * @return The number, or -1 if the parameter was not a valid number
     */
    private int getNum(String param) {
        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            return -1;
        }
    }
}
