package Util;

import sx.blah.discord.util.EmbedBuilder;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Footer {
    /**
     * Set Footer of Embeded Messages
     * @param builder builder
     */
    public static void addFooter(EmbedBuilder builder) {
        builder.withFooterText("Created by ModdyLP @2017");
    }
}
