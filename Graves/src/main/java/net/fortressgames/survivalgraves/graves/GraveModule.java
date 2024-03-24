package net.fortressgames.survivalgraves.graves;

import lombok.Getter;
import net.fortressgames.survivalgraves.SurvivalGraves;
import net.fortressgames.survivalgraves.nms.PacketHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
	public void createGrave(UUID uuid, List<ItemStack> items, int level, Location location, String deathCause) {
		this.graves.putIfAbsent(uuid, new ArrayList<>());

		Grave grave = new Grave(uuid, items, level, location, deathCause);

		this.graves.get(uuid).add(grave);
		spawnGrave(grave);
	}

	public void removeGrave(Grave grave) {
		grave.getTask().cancel();

		this.graves.get(grave.getOwner()).remove(grave);

		deSpawnGrave(grave);
	}

	public List<Grave> getAllGraves() {
		return graves.values().stream()
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	public void dropItems(Player player, Grave grave) {
		for(ItemStack item : grave.getItems()) {
			if(item == null) continue;

			if(player == null) {
				grave.getLocation().getWorld().dropItem(grave.getLocation(), item);
				continue;
			}

			if(!player.getInventory().addItem(item).isEmpty()) {
				grave.getLocation().getWorld().dropItem(grave.getLocation(), item);
			}
		}

		if(SurvivalGraves.getInstance().getConfig().getBoolean("StoreEXP")) {
			if(player == null) {
				ExperienceOrb orb = (ExperienceOrb) grave.getLocation().getWorld().spawnEntity(grave.getLocation(), EntityType.EXPERIENCE_ORB);

				int pointsPerLevel = 7;
				int totalXP = 0;

				for(int level = 1; level <= grave.getLevel(); level++) {
					int pointsForThisLevel = pointsPerLevel + (2 * (level - 1));
					totalXP += pointsForThisLevel;
				}

				orb.setExperience(totalXP);

				return;
			}

			player.setLevel(grave.getLevel());
		}
	}

	private void spawnGrave(Grave grave) {
		for(Player pp : Bukkit.getOnlinePlayers()) {
			grave.getDisplayArmorstands().forEach(displayArmorstand -> PacketHandler.spawn(pp, displayArmorstand));
		}
	}

	private void deSpawnGrave(Grave grave) {
		for(Player pp : Bukkit.getOnlinePlayers()) {
			grave.getDisplayArmorstands().forEach(displayArmorstand -> PacketHandler.remove(pp, displayArmorstand));
		}
	}
}