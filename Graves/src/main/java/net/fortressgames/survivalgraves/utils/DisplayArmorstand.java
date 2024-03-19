package net.fortressgames.survivalgraves.utils;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DisplayArmorstand {

	private final EntityArmorStand entityArmorStand;
	private final int id;

	public DisplayArmorstand(Location location) {
		this.entityArmorStand = new EntityArmorStand(EntityTypes.d,((CraftWorld) location.getWorld()).getHandle());
		this.entityArmorStand.b(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

		entityArmorStand.j(true);

		this.id = entityArmorStand.af();
	}

	public void spawn(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutSpawnEntity(entityArmorStand));
		connection.sendPacket(new PacketPlayOutEntityMetadata(this.id, entityArmorStand.aj().b()));

		connection.sendPacket(new PacketPlayOutEntityEquipment(this.id,
				List.of(new Pair<>(EnumItemSlot.f, entityArmorStand.c(EnumItemSlot.f)))));
	}

	public void remove(Player player) {
		PacketConnection connection = PacketConnection.getConnection(player);

		connection.sendPacket(new PacketPlayOutEntityDestroy(this.id));
	}

	public DisplayArmorstand setCustomName(String displayName) {
		IChatBaseComponent iChatBaseComponent = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" +
				ChatColor.translateAlternateColorCodes('&', displayName) + "\"}");

		entityArmorStand.b(iChatBaseComponent);
		entityArmorStand.n(true);

		return this;
	}

	public DisplayArmorstand setHelmet(ItemStack itemstack) {
		entityArmorStand.a(EnumItemSlot.f, CraftItemStack.asNMSCopy(itemstack));

		return this;
	}
}