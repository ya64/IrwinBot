package com.github.ya64.IrwinBot;

import java.util.Scanner;

/**
 * The Terminal for the bot
 */
public class Terminal extends Thread {
    @Override
    public void run() {
        Scanner keyboardInput = new Scanner(System.in);
        while (true) {
            String command = keyboardInput.nextLine().trim();
            if (command.equalsIgnoreCase("stop")) {
                Main.exit();
            } else if (command.equalsIgnoreCase("help")) {
                System.out.println("Help - Shows this message\nStop - Turns off the bot");
            }
        }
    }
}
