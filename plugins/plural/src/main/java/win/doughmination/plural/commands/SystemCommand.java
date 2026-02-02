package win.doughmination.plural.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import win.doughmination.plural.MainClass;

import javax.annotation.Nonnull;
import java.util.UUID;

public class SystemCommand extends CommandBase {

    private final MainClass plugin;

    public SystemCommand(MainClass plugin) {
        super("system", "Manage your plurality system");
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {

        PlayerRef player = (PlayerRef) context.sender();
        String input = context.getInputString();
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            sendUsage(context);
            return;
        }

        String subCommand = parts[1].toLowerCase();

        switch (subCommand) {
            case "add":
                handleAdd(context, player);
                break;
            case "delete":
                handleDelete(context, player);
                break;
            default:
                sendUsage(context);
                break;
        }
    }

    private void handleAdd(CommandContext context, PlayerRef player) {
        UUID playerId = player.getUuid();

        if (plugin.getDataManager().hasSystem(playerId)) {
            context.sendMessage(Message.raw("You already have a system! Use /system delete to remove it first."));
            return;
        }

        plugin.getDataManager().createSystem(playerId);
        context.sendMessage(Message.raw("System created successfully! You can now use /front commands."));
    }

    private void handleDelete(CommandContext context, PlayerRef player) {
        UUID playerId = player.getUuid();

        if (!plugin.getDataManager().hasSystem(playerId)) {
            context.sendMessage(Message.raw("You don't have a system to delete!"));
            return;
        }

        plugin.getDataManager().deleteSystem(playerId);
        context.sendMessage(Message.raw("System deleted successfully."));
    }

    private void sendUsage(CommandContext context) {
        context.sendMessage(Message.join(
                Message.raw("System Commands:\n"),
                Message.raw("/system add - Create a new system\n"),
                Message.raw("/system delete - Delete your system")
        ));
    }
}