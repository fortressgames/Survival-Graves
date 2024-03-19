package net.fortressgames.survivalgraves.graves;

import lombok.Getter;
import net.fortressgames.survivalgraves.utils.DisplayArmorstand;
import net.fortressgames.survivalgraves.utils.ItemBuilder;
import net.fortressgames.survivalgraves.utils.UUIDHandler;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Grave {

	private final UUID owner;				// Owner of this grave
	private final List<ItemStack> items;	// Items inside the grave
	private final double xp;				// XP inside the grave
	private final Location location;		// Death location
	private final long deathMills;			// Mills time the grave was made

	@Getter private final List<DisplayArmorstand> displayArmorstands = new ArrayList<>();

	public Grave(UUID owner, List<ItemStack> items, double xp, Location location) {
		this.owner = owner;
		this.items = items;
		this.xp = xp;
		this.location = location;
		this.deathMills = System.currentTimeMillis();

		createArmorstands();
	}

	public Grave(UUID owner, List<ItemStack> items, double xp, Location location, long deathMills) {
		this.owner = owner;
		this.items = items;
		this.xp = xp;
		this.location = location;
		this.deathMills = deathMills;
	}

	private void createArmorstands() {
		DisplayArmorstand grave = new DisplayArmorstand(
				new Location(location.getWorld(), location.getX(), location.getY() -1.4, location.getZ()))
				.setHelmet(new ItemBuilder(Material.PLAYER_HEAD).skull64Base("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjYWI1NmM4MmNiODFiZGI5OTc5YTQ2NGJjOWQzYmEzZTY3MjJiYTEyMmNmNmM1Mjg3MzAxMGEyYjU5YWVmZSJ9fX0=").build());

		DisplayArmorstand name = new DisplayArmorstand(
				new Location(location.getWorld(), location.getX(), location.getY() -1, location.getZ()))
				.setCustomName(ChatColor.RED + "☠ " + UUIDHandler.getNameFromUUID(owner.toString()));


		this.displayArmorstands.add(grave);
		this.displayArmorstands.add(name);
	}
}