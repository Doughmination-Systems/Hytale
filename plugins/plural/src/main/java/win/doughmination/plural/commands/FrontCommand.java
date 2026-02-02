package win.doughmination.plural.commands;

import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import win.doughmination.plural.MainClass;
import win.doughmination.plural.data.PlayerSystem;
import win.doughmination.plural.data.ProxyMember;

import javax.annotation.Nonnull;
import java.util.UUID;

public class FrontCommand extends CommandBase {

    private final MainClass plugin;

    public FrontCommand(MainClass plugin) {
        super("front", "Manage your fronting members");
        this.plugin = plugin;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext context) {

        PlayerRef player = (PlayerRef) context.sender();
        UUID playerId = player.getUuid();

        if (!plugin.getDataManager().hasSystem(playerId)) {
            context.sendMessage(Message.raw("You need to create a system first! Use /system add"));
            return;
        }

        String input = context.getInputString();
        String[] parts = input.split("\\s+");

        if (parts.length < 2) {
            sendUsage(context);
            return;
        }

        String subCommand = parts[1].toLowerCase();

        switch (subCommand) {
            case "add":
                handleAdd(context, player, parts);
                break;
            case "delete":
                handleDelete(context, player, parts);
                break;
            case "set":
                handleSet(context, player, parts);
                break;
            default:
                sendUsage(context);
                break;
        }
    }

    private void handleAdd(CommandContext context, PlayerRef player, String[] parts) {
        if (parts.length < 4) {
            context.sendMessage(Message.raw("Usage: /front add <n> <displayName>"));
            return;
        }

        UUID playerId = player.getUuid();
        PlayerSystem system = plugin.getDataManager().getSystem(playerId);

        String memberName = parts[2];
        String displayName = parts[3];

        if (parts.length > 4) {
            StringBuilder sb = new StringBuilder(displayName);
            for (int i = 4; i < parts.length; i++) {
                sb.append(" ").append(parts[i]);
            }
            displayName = sb.toString();
        }

        if (system.hasMember(memberName)) {
            context.sendMessage(Message.raw("A member with that name already exists!"));
            return;
        }

        ProxyMember member = new ProxyMember(memberName, displayName);
        system.addMember(memberName, member);
        plugin.getDataManager().save();

        context.sendMessage(Message.join(
                Message.raw("Added member: "),
                Message.raw(displayName),
                Message.raw(" ("),
                Message.raw(memberName),
                Message.raw(")")
        ));
    }

    private void handleDelete(CommandContext context, PlayerRef player, String[] parts) {
        if (parts.length < 3) {
            context.sendMessage(Message.raw("Usage: /front delete <n>"));
            return;
        }

        UUID playerId = player.getUuid();
        PlayerSystem system = plugin.getDataManager().getSystem(playerId);

        String memberName = parts[2];

        if (!system.hasMember(memberName)) {
            context.sendMessage(Message.raw("No member found with that name!"));
            return;
        }

        ProxyMember member = system.getMember(memberName);
        system.removeMember(memberName);
        plugin.getDataManager().save();

        context.sendMessage(Message.join(
                Message.raw("Deleted member: "),
                Message.raw(member.getDisplayName())
        ));
    }

    private void handleSet(CommandContext context, PlayerRef player, String[] parts) {
        if (parts.length < 3) {
            context.sendMessage(Message.raw("Usage: /front set <n>"));
            return;
        }

        UUID playerId = player.getUuid();
        PlayerSystem system = plugin.getDataManager().getSystem(playerId);

        String memberName = parts[2];

        if (memberName.equalsIgnoreCase("none") || memberName.equalsIgnoreCase("clear")) {
            system.clearFront();
            plugin.getDataManager().save();
            context.sendMessage(Message.raw("Front cleared. You will now send messages as yourself."));
            return;
        }

        if (!system.hasMember(memberName)) {
            context.sendMessage(Message.raw("No member found with that name!"));
            return;
        }

        ProxyMember member = system.getMember(memberName);
        system.setFront(memberName);
        plugin.getDataManager().save();

        context.sendMessage(Message.join(
                Message.raw("Now fronting as: "),
                Message.raw(member.getDisplayName())
        ));
    }

    private void sendUsage(CommandContext context) {
        context.sendMessage(Message.join(
                Message.raw("Front Commands:\n"),
                Message.raw("/front add <n> <displayName> - Add a new member\n"),
                Message.raw("/front delete <n> - Delete a member\n"),
                Message.raw("/front set <n> - Set who is fronting\n"),
                Message.raw("/front set none - Clear front (speak as yourself)")
        ));
    }
}