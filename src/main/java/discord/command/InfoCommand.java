package discord.command;

import com.jagrosh.jdautilities.commandclient.Command;
import com.jagrosh.jdautilities.commandclient.CommandEvent;

public class InfoCommand extends Command {

    public InfoCommand() {
        this.name = "info";
        this.aliases = new String[]{"i"};
        this.help = "Info About the Bot";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reactSuccess();
        commandEvent.replyInDm("Info Message");
        commandEvent.getMessage().delete();
    }
}
