package net.fortressgames.survivalgraves.nms;

import lombok.Getter;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class CustomArmorStand {

	@Getter private EntityArmorStand entityArmorStand;
	@Getter private int id;
	private final Location location;

	public CustomArmorStand(Location location) {
		this.location = location;

		this.entityArmorStand = new EntityArmorStand(EntityTypes.d,((CraftWorld) location.getWorld()).getHandle());
		this.entityArmorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.id = entityArmorStand.af();

		entityArmorStand.j(true);
	}

	public CustomArmorStand newInstance() {
		this.entityArmorStand = new EntityArmorStand(EntityTypes.d,((CraftWorld) location.getWorld()).getHandle());
		this.entityArmorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
		this.id = entityArmorStand.af();

		entityArmorStand.j(true);

		return this;
	}

	public CustomArmorStand setCustomName(String displayName) {
		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
				ChatColor.translateAlternateColorCodes('&', displayName) + "\"}");

		entityArmorStand.b(iChatBaseComponent);
		entityArmorStand.n(true);

		return this;
	}

	public CustomArmorStand setHelmet(ItemStack itemstack) {
		entityArmorStand.a(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemstack));

		return this;
	}
}