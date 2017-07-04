package events;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord.BotUtils;
import util.Fast;
import modules.music.MainMusic;
import org.tritonus.share.ArraySet;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;
import util.Console;
import util.GetAnnotation;
import util.SMB;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class EventListener implements Fast{
    private static EventListener instance;

    /**
     * Get Instance
     * @return Class Instance
     */
    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }

    /**
     * If Bot recieves any Message
     * @param event The Event
     */
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) { // This method is NOT called because it doesn't have the @EventSubscriber annotation
        new Thread(() -> {
            try {
                //Check if Channel is Private (DM)
                if (!event.getChannel().isPrivate()) {
                    String message = event.getMessage().getContent();
                    String[] messageparts = message.split(" ");
                    if (messageparts.length > 0) {
                        String prefix = messageparts[0].substring(0, 1).trim();
                        String[] args = new String[]{};
                        Command command = COMMAND.getCommandByName(messageparts[0].replace(prefix, ""));
                        Console.debug("Command: "+command);
                        if (command != null && command.prefix().equalsIgnoreCase(prefix)) {
                            Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                            //Check if Invoke Messages should be deleted
                            if (DRIVER.getProperty(DRIVER.CONFIG, "deleteinvokes", true).equals(true)) {
                                if (INIT.BOT.getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                                    Console.debug(Console.sendprefix + "Message deleted: [" + message + "]");
                                    event.getMessage().delete();
                                } else {
                                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("nomanagepermission_error")), true);
                                }
                            }
                            if (PERM.hasPermission(event.getAuthor(), event.getGuild(), command.permission())) {
                                if (messageparts.length > 1) {
                                    args = message.replace(messageparts[0], "").trim().split(" ");
                                }
                                initiateCommand(args, command, event);
                            } else {
                                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("nomanagepermission_error")), true);
                            }
                        }
                    }
                } else {
                    BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.ERROR+LANG.getTranslation("private_error"));
                }
            } catch (Exception ex) {
                Console.error(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace())));
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * Executes the Command
     * @param args Arguments which should be delivered
     * @param command The Command Annotation Object
     * @param event The Message Event
     */
    private void initiateCommand(String[] args, Command command, MessageReceivedEvent event) {
        try {
            ArrayList<String> newargs = new ArrayList<>();
            if (args.length >= command.arguments().length) {
                for (int i = 0; i < command.arguments().length; i++) {
                    if (command.arguments()[i].contains("[]")) {
                        newargs.addAll(Arrays.asList(args).subList(i, args.length));
                    } else {
                        newargs.add(args[i]);
                    }

                }
                if(newargs.size() < args.length) {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+String.format(LANG.getTranslation("tomanyarguments_error"), args.length, command.arguments().length)), true);
                } else {
                    String[] printargs = newargs.toArray(new String[]{});
                    COMMAND.getModules().get(command).invoke(COMMAND.getInstances().get(command), event, printargs);
                }
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+String.format(LANG.getTranslation("tofewarguments_error"), args.length, command.arguments().length)), true);
            }

        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("execution_error"), ex.getCause()));
            ex.printStackTrace();
        }
    }

    /**
     * If Bot is started and ready to recieve anything
     * @param event The Event
     */
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        Console.println("Bot login success");
        Console.println("Shards: "+INIT.BOT.getShardCount());
        StringBuilder serverstr = new StringBuilder();
        int count = 1;
        for (IGuild server: INIT.BOT.getGuilds()) {
            serverstr.append("\n")
                    .append(count)
                    .append(". [")
                    .append(server.getName())
                    .append("   ")
                    .append(server.getStringID())
                    .append("]");
            count++;
        }
        Console.println("Servers: "+serverstr);
        RegisterCommands.registerAll();
        Console.println("Loading Permissions from SaveFile");
        PERM.loadPermissions(INIT.BOT.getGuilds());
        PERM.setDefaultPermissions(INIT.BOT.getGuilds());
        INIT.BOT.changePlayingText(DRIVER.getPropertyOnly(DRIVER.CONFIG,"defaultplaying").toString());
        INIT.BOT.changeUsername(DRIVER.getPropertyOnly(DRIVER.CONFIG,"defaultUsername").toString());
        Console.println("Loading Command Descriptions");
        for (Command command: COMMAND.getAllCommands()) {
            LANG.getMethodDescription(command);
        }
        Console.println("Register Audioprovider");
        AudioSourceManagers.registerRemoteSources(MainMusic.playerManager);
        AudioSourceManagers.registerLocalSource(MainMusic.playerManager);

        Console.println("====================================Bot Start completed===============================");
    }
}
