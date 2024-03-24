package net.fortressgames.survivalgraves.nms;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import lombok.SneakyThrows;
import net.fortressgames.survivalgraves.SurvivalGraves;
import net.fortressgames.survivalgraves.graves.Grave;
import net.fortressgames.survivalgraves.graves.GraveModule;
import net.minecraft.network.protocol.game.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.lang.reflect.Field;

public class PacketListener extends PacketAdapter implements Listener {

	public PacketListener() {
		super(SurvivalGraves.getInstance(), PacketType.Play.Client.USE_ENTITY);
	}

	@Override
	public void onPacketReceiving(PacketEvent e) {
		PacketContainer packet = e.getPacket();
		Player player = e.getPlayer();

		for(Grave grave : GraveModule.getInstance().getAllGraves()) {
			for(CustomArmorStand armorstand : grave.getDisplayArmorstands()) {

				// check if the ID matches current click entity on event
				if(armorstand.getId() == packet.getIntegers().read(0)) {
					PacketPlayInUseEntity packetPlayInUseEntity = (PacketPlayInUseEntity) packet.getHandle();

					Object objectValue = getFieldObject(packetPlayInUseEntity);

					try {
						objectValue.getClass().getDeclaredField("a");
					} catch (NoSuchFieldException ee) {
						//RIGHT CLICK

						if(player.getUniqueId().equals(grave.getOwner())) {

							Bukkit.getScheduler().runTask(SurvivalGraves.getInstance(), () ->
									GraveModule.getInstance().dropItems(player, grave));

							GraveModule.getInstance().removeGrave(grave);

						} else {
							player.sendMessage(SurvivalGraves.getInstance().getLang("NotOwner"));
						}
					}
				}
			}
		}
	}

	@SneakyThrows
	private Object getFieldObject(PacketPlayInUseEntity packetPlayInUseEntity) {
		Field value = packetPlayInUseEntity.getClass().getDeclaredField("b");
		value.setAccessible(true);

		return value.get(packetPlayInUseEntity);
	}
}