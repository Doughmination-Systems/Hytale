package win.doughmination.plural;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent;
import win.doughmination.plural.commands.SystemCommand;
import win.doughmination.plural.commands.FrontCommand;
import win.doughmination.plural.listeners.ChatListener;
import win.doughmination.plural.data.DataManager;

import javax.annotation.Nonnull;

public class MainClass extends JavaPlugin {

    private static MainClass instance;
    private DataManager dataManager;

    public MainClass(@Nonnull JavaPluginInit init) {
        super(init);
    }

    @Override
    protected void setup() {
        instance = this;

        // Initialize data manager
        dataManager = new DataManager(this);
        dataManager.load();

        // Register commands
        this.getCommandRegistry().registerCommand(new SystemCommand(this));
        this.getCommandRegistry().registerCommand(new FrontCommand(this));

        // Register event listener
        this.getEventRegistry().registerGlobal(PlayerChatEvent.class, ChatListener::onPlayerChat);

        // this.getLogger().info("CPT Plugin has been enabled!");
    }

    public static MainClass getInstance() {
        return instance;
    }

    public DataManager getDataManager() {
        return dataManager;
    }
}