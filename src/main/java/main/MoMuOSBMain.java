package main;

import discord.DiscordInit;
import util.Console;

import java.sql.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class MoMuOSBMain implements Fast{

    public static Date starttime = new Date(System.currentTimeMillis());

    /**
     * main Method
     * @param args Start Argumente
     */
    public static void main(String[] args) {
        try {
            Console.println("Bot starting...");
            Console.println("Bot was created by ModdyLP - Niklas H. https://moddylp.de.");
            DRIVER.createNewFile(DRIVER.CONFIG);
            Console.println("Loading Language....");
            LANG.createTranslations();
            LANG.setDefaultLanguage();
            Console.println("Language loading complete!");
            Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        shutdown();
                    }
                });
            DiscordInit.getInstance().init();

        } catch (Exception ex) {
            Console.error(ex.getMessage());
            ex.printStackTrace();
        }

    }

    /**
     * Shutdown Method
     */
    private static void shutdown() {
        Console.println("Bot shutting down...");
        if (INIT.BOT != null && INIT.BOT.isLoggedIn()) {
            INIT.BOT.logout();
        }
        Console.println("ByeBye... Created by ModdyLP @2017");
    }
}
