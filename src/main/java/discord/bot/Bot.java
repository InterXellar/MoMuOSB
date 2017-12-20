package discord.bot;

import discord.command.CommandHandler;
import javafx.concurrent.Task;
import main.MoMuOSBMain;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.requests.SessionReconnectQueue;
import storage.api.Storage;

public class Bot implements Storage{

    private static Bot botinstance;

    public static Bot getBotInst() {
        if (botinstance == null) {
            botinstance = new Bot();
        }
        return botinstance;
    }

    private JDA bot;



    public void startBot() {
        try {
            MoMuOSBMain.logger.info("Starting Bot....");
            MoMuOSBMain.logger.info("===========================================================================");
            if (DRIVER.getPropertyOnly(DRIVER.CONFIG, "auth.token") != null && DRIVER.getPropertyOnly(DRIVER.CONFIG, "bot.owner") != null) {
                        JDABuilder shardBuilder = new JDABuilder(AccountType.BOT)
                                .setToken(DRIVER.getPropertyOnly(DRIVER.CONFIG, "auth.token").toString())
                                .setReconnectQueue(new SessionReconnectQueue());
                        //register your listeners here using shardBuilder.addEventListener(...)
                        shardBuilder.addEventListener(new BotMainEvents());
                        shardBuilder.addEventListener(CommandHandler.registerCommands());
                        for (int i = 0; i < ((long) DRIVER.getProperty(DRIVER.CONFIG, "bot.Shards", 3)); i++)
                        {
                            //using buildBlocking(JDA.Status.AWAITING_LOGIN_CONFIRMATION)
                            // makes sure we start to delay the next shard once the current one actually
                            // sent the login information, otherwise we might hit nasty race conditions
                            bot = shardBuilder.useSharding(i, (Integer.valueOf(DRIVER.getProperty(DRIVER.CONFIG, "bot.Shards", 3).toString())))
                                    .buildAsync();
                            Thread.sleep(5000); //sleep 5 seconds between each login
                        }
            } else {
                MoMuOSBMain.logger.error("There is no Bot Token or Owner ID provided in the "+ DRIVER.CONFIG);
            }
            MoMuOSBMain.logger.info("===========================================================================");
        } catch (Exception ex) {
            MoMuOSBMain.logger.error(ex);
        }
    }

    public JDA getBot() {
        return bot;
    }
}
