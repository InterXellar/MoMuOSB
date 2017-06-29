package util;

import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;

/**
 * Created by ModdyLP on 29.06.2017. Website: https://moddylp.de/
 */
public class ShortMessageBuilder {
    public static EmbedBuilder shortMessage(String message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(message);
        builder.withColor(Color.CYAN);
        return builder;
    }
}
