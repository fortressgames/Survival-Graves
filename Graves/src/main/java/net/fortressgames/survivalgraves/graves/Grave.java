package net.fortressgames.survivalgraves.graves;

import lombok.Getter;
import net.fortressgames.survivalgraves.SurvivalGraves;
import net.fortressgames.survivalgraves.nms.CustomArmorStand;
import net.fortressgames.survivalgraves.nms.PacketHandler;
import net.fortressgames.survivalgraves.utils.ItemBuilder;
import net.fortressgames.survivalgraves.utils.UUIDHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Grave {

	@Getter private final UUID owner;				// Owner of this grave
	@Getter private final List<ItemStack> items;	// Items inside the grave
	@Getter private final int level;				// Level inside the grave
	@Getter private final Location location;		// Death location

	private final String deathCause;				// Death cause
	private final long deathMills;					// Mills time the grave was made

	private CustomArmorStand timerDisplay;
	@Getter private BukkitTask task;				// Timer task int

	@Getter private final List<CustomArmorStand> displayArmorstands = new ArrayList<>();

	public Grave(UUID owner, List<ItemStack> items, int level, Location location, String deathCause) {
		this.owner = owner;
		this.items = items;
		this.level = level;
		this.location = location;
		this.deathCause = deathCause;
		this.deathMills = System.currentTimeMillis();

		createArmorstands();
	}

	public Grave(UUID owner, List<ItemStack> items, int level, Location location, String deathCause, long deathMills) {
		this.owner = owner;
		this.items = items;
		this.level = level;
		this.location = location;
		this.deathCause = deathCause;
		this.deathMills = deathMills;
	}

	public void updateTimer() {
		if(getTimeDifference() <= 0) {
			GraveModule.getInstance().dropItems(null, this);
			GraveModule.getInstance().removeGrave(this);
			return;
		}

		Bukkit.getOnlinePlayers().forEach(player -> PacketHandler.remove(player, timerDisplay));

		this.timerDisplay.newInstance().setCustomName(ChatColor.WHITE + getTimeRemaining());

		Bukkit.getOnlinePlayers().forEach(player -> PacketHandler.spawn(player, timerDisplay));
	}

	private long getTimeDifference() {
		long endTime = deathMills + (SurvivalGraves.getInstance().getConfig().getInt("GraveTimer") * 60 * 60 * 1000);
		return endTime - System.currentTimeMillis();
	}

	private String getTimeRemaining() {
		long timeDifferenceMillis = getTimeDifference();

		long hours = TimeUnit.MILLISECONDS.toHours(timeDifferenceMillis);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDifferenceMillis) - TimeUnit.HOURS.toMinutes(hours);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDifferenceMillis) - TimeUnit.MINUTES.toSeconds(minutes) - TimeUnit.HOURS.toSeconds(hours);

		return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
	}

	private void createArmorstands() {
		CustomArmorStand grave = new CustomArmorStand(
				new Location(location.getWorld(), location.getX(), location.getY() -1.4, location.getZ()))
				.setHelmet(new ItemBuilder(Material.PLAYER_HEAD).skull64Base("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdjYWI1NmM4MmNiODFiZGI5OTc5YTQ2NGJjOWQzYmEzZTY3MjJiYTEyMmNmNmM1Mjg3MzAxMGEyYjU5YWVmZSJ9fX0=").build());

		CustomArmorStand name = new CustomArmorStand(
				new Location(location.getWorld(), location.getX(), location.getY() -0.7, location.getZ()))
				.setCustomName(ChatColor.RED + "☠ " + UUIDHandler.getNameFromUUID(owner.toString()));

		CustomArmorStand cause = new CustomArmorStand(
				new Location(location.getWorld(), location.getX(), location.getY() -1, location.getZ()))
				.setCustomName(ChatColor.GRAY + deathCause);

		CustomArmorStand timer = new CustomArmorStand(
				new Location(location.getWorld(), location.getX(), location.getY() -1.3, location.getZ()))
				.setCustomName(ChatColor.WHITE + getTimeRemaining());

		this.timerDisplay = timer;
		this.task = Bukkit.getScheduler().runTaskTimer(SurvivalGraves.getInstance(), this::updateTimer, 0, 20);

		this.displayArmorstands.add(grave);
		this.displayArmorstands.add(name);
		this.displayArmorstands.add(cause);
		this.displayArmorstands.add(timer);
	}
}