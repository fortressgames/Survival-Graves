package net.fortressgames.survivalgraves;

import lombok.Getter;
import net.fortressgames.survivalgraves.listeners.PlayerDeathListener;
import net.fortressgames.survivalgraves.utils.ConsoleMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalGraves extends JavaPlugin {

	@Getter private static SurvivalGraves instance;

	/**
	 * Called when plugin first loads by spigot and is called before onEnable
	 */
	@Override
	public void onLoad() {
	}

	/**
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		// Listeners
		this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

		getLogger().info(ConsoleMessage.GREEN + "Version: " + getDescription().getVersion() + " Enabled!" + ConsoleMessage.RESET);
	}

	/**
	 * Called when the server is restarted or stopped
	 */
	@Override
	public void onDisable() {
		getLogger().info(ConsoleMessage.RED + "Version: " + getDescription().getVersion() + " Disabled!" + ConsoleMessage.RESET);
	}
}