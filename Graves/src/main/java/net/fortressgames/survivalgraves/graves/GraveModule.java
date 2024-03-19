package net.fortressgames.survivalgraves.graves;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GraveModule {

	@Getter private static GraveModule instance;

	private HashMap<UUID, List<Grave>> graves = new HashMap<>();

	public static GraveModule getInstance() {
		if(instance == null) {
			instance = new GraveModule();
		}

		return instance;
	}

	/**
	 * Creates a new grave for a deceased player.
	 *
	 * @param uuid The UUID of the player who died.
	 * @param items The list of ItemStacks representing items the player had.
	 * @param xp The amount of experience points the player had.
	 */
	public void createGrave(UUID uuid, List<ItemStack> items, double xp, Location location) {
		this.graves.putIfAbsent(uuid, new ArrayList<>());

		Grave grave = new Grave(uuid, items, xp, location);

		this.graves.get(uuid).add(grave);
		spawnGrave(grave);
	}

	private void spawnGrave(Grave grave) {
		for(Player pp : Bukkit.getOnlinePlayers()) {
			grave.getDisplayArmorstands().forEach(displayArmorstand -> displayArmorstand.spawn(pp));
		}
	}
}