package Discord;

import Storage.ConfigDriver;
import Util.Console;
import Util.Footer;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class BotUtils {
    public static String BOT_PREFIX = ConfigDriver.getInstance().getProperty("prefix", ".");

    // Handles the creation and getting of a IDiscordClient object for a token
    static IDiscordClient getBuiltDiscordClient(String token){
        // The ClientBuilder object is where you will attach your params for configuring the instance of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withRecommendedShardCount()
                .withToken(token)
                .build();

    }
    public static void sendMessage(IChannel channel, String message){
        RequestBuffer.request(() -> {
            try{
                channel.sendMessage(message);
            } catch (DiscordException e){
                Console.error("Message could not be sent with error: "+ e.getMessage());
            }
        });

    }
    public static void sendPrivMessage(IPrivateChannel userchannel, String message){
        RequestBuffer.request(() -> {
            try{
                userchannel.sendMessage(message);
            } catch (DiscordException e){
                Console.error("Message could not be sent with error: "+ e.getMessage());
            }
        });

    }
    public static void sendEmbMessage(IChannel channel, EmbedBuilder builder){
        RequestBuffer.request(() -> {
            try{
                Footer.addFooter(builder);
                channel.sendMessage(builder.build());
            } catch (DiscordException e){
                Console.error("Message could not be sent with error: "+ e.getMessage());
            }
        });

    }
    public static void sendPrivEmbMessage(IPrivateChannel channel, EmbedBuilder builder){
        RequestBuffer.request(() -> {
            try{
                Footer.addFooter(builder);
                channel.sendMessage(builder.build());
            } catch (DiscordException e){
                Console.error("Message could not be sent with error: "+ e.getMessage());
            }
        });

    }
    public static void deleteMessageOne(IMessage message) {
        RequestBuffer.request(() -> {
            try {
                message.delete();
            } catch (DiscordException e) {
                Console.error("Message could not be deleted with error: " + e.getMessage());
            }
        });
    }
    public static void bulkdeleteMessage(IChannel channel, List<IMessage> messages) {
        RequestBuffer.request(() -> {
            try {
                channel.bulkDelete(messages);
            } catch (DiscordException e) {
                Console.error("Message could not be deleted with error: " + e.getMessage());
            }
        });
    }
}
