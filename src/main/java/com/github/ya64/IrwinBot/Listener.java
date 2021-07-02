package com.github.ya64.IrwinBot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
                    .setTitle("Help")
                    .addField(Main.getPrefix() + "help", "Shows this message", true)
                    .addField(Main.getPrefix() + "irwin", "Gets a random irwin", true)
                    .addField(Main.getPrefix() + "irwin [number]", "Gets an irwin by its number", true)
                    .addField(Main.getPrefix() + "backyardigans", "The Backyardirwins story", true)
                    .addField(Main.getPrefix() + "github", "The source for this bot", true)
                    .addField(Main.getPrefix() + "ping", "Gets the ping", true)
                    .setFooter("Don't do " + Main.getPrefix() + "mandy")
                    .build()
            ).queue();
        } else if (command.toLowerCase().startsWith("irwin")) {
            if (params.length == 1 || getNum(params[1]) == -1) {
                irwin = IrwinLibrary.getIrwin();
                if (irwin == null) {
                    m.getChannel().sendMessage("Oops, something went wrong with getting an irwin :(").queue();
                    return;
                }
            } else {
                irwin = IrwinLibrary.getIrwin(getNum(params[1]));
                if (irwin == null) {
                    m.getChannel().sendMessage("Oops, seems like we couldn't find that Irwin :(").queue();
                    return;
                }
            }
        } else if (command.toLowerCase().startsWith("mandy")) {
            int amountOfY = (int) (Math.random() * 9) + 1;
            StringBuilder y = new StringBuilder();
            for (int i = 0; i < amountOfY; i++) {
                y.append("y");
            }
            m.getChannel().sendMessage("He" + y.toString() + " Mandy!").queue();
        } else if (command.toLowerCase().startsWith("pog")) {
            m.getChannel().sendMessage("Poggers!").queue();
        } else if (command.toLowerCase().startsWith("backyard")) {
            m.getChannel().sendMessage("https://www.wattpad.com/story/275145195-the-backyardirwins").queue();
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

        m.getChannel()
         .sendFile(new File("irwins" + File.separator + irwin.getFileName()), irwin.getFileName())
         .setEmbeds((new EmbedBuilder())
                 .setTitle(irwin.getNumber() + " - " + irwin.getName())
                 .setImage("attachment://" + irwin.getFileName())
                 .build()
         ).queue();
    }

    private int getNum(String param) {
        try {
            return Integer.parseInt(param);
        } catch (Exception e) {
            return -1;
        }
    }
}