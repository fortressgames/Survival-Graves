package net.fortressgames.survivalgraves.nms;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.protocol.game.PacketPlayOutEntityMetadata;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.server.network.PlayerConnection;
import net.minecraft.world.entity.EnumItemSlot;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

public class PacketHandler {

	public static void spawn(Player player, CustomArmorStand customArmorStand) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().c;

		connection.a(new PacketPlayOutSpawnEntity(customArmorStand.getEntityArmorStand()));
		connection.a(new PacketPlayOutEntityMetadata(customArmorStand.getId(), customArmorStand.getEntityArmorStand().aj().b()));

		connection.a(new PacketPlayOutEntityEquipment(customArmorStand.getId(),
				List.of(new Pair<>(EnumItemSlot.f, customArmorStand.getEntityArmorStand().c(EnumItemSlot.f)))));
	}

	public static void remove(Player player, CustomArmorStand customArmorStand) {
		PlayerConnection connection = ((CraftPlayer) player).getHandle().c;

		connection.a(new PacketPlayOutEntityDestroy(customArmorStand.getId()));
	}
}