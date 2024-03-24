package net.fortressgames.survivalgraves;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import net.fortressgames.survivalgraves.listeners.PlayerDeathListener;
import net.fortressgames.survivalgraves.nms.PacketListener;
import net.fortressgames.survivalgraves.utils.ConsoleMessage;
import org.bukkit.plugin.java.JavaPlugin;

public class SurvivalGraves extends JavaPlugin {

	@Getter private static SurvivalGraves instance;

	/**
	 * Called when plugin first loads by spigot and is called before onEnable
	 */
	@Override
	public void onLoad() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	/**
	 * Called when the plugin is first loaded by Spigot
	 */
	@Override
	public void onEnable() {
		instance = this;

		// Listeners
		this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);

		PacketListener packetListener = new PacketListener();
		this.getServer().getPluginManager().registerEvents(packetListener, this);
		ProtocolLibrary.getProtocolManager().addPacketListener(packetListener);

		getLogger().info(ConsoleMessage.GREEN + "Version: " + getDescription().getVersion() + " Enabled!" + ConsoleMessage.RESET);
	}

	/**
	 * Called when the server is restarted or stopped
	 */
	@Override
	public void onDisable() {
		getLogger().info(ConsoleMessage.RED + "Version: " + getDescription().getVersion() + " Disabled!" + ConsoleMessage.RESET);
	}

	public String getLang(String name) {
		return getConfig().getString(name);
	}
}