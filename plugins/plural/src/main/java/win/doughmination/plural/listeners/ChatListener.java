package win.doughmination.plural.listeners;

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import win.doughmination.plural.MainClass;
import win.doughmination.plural.data.PlayerSystem;
import win.doughmination.plural.data.ProxyMember;

import java.util.UUID;

public class ChatListener {

    public static void onPlayerChat(PlayerChatEvent event) {
        MainClass plugin = MainClass.getInstance();
        PlayerRef sender = event.getSender();
        UUID playerId = sender.getUuid();

        if (!plugin.getDataManager().hasSystem(playerId)) {
            return;
        }

        PlayerSystem system = plugin.getDataManager().getSystem(playerId);
        ProxyMember frontingMember = system.getFrontingMember();

        if (frontingMember == null) {
            return;
        }

        event.setFormatter((playerRef, message) ->
                Message.join(
                        Message.raw(frontingMember.getDisplayName()),
                        Message.raw(" ("),
                        Message.raw(sender.getUsername()),
                        Message.raw(")"),
                        Message.raw(" : "),
                        Message.raw(message)
                )
        );
    }
}