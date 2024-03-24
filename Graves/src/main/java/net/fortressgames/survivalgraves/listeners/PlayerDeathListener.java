package net.fortressgames.survivalgraves.listeners;

import net.fortressgames.survivalgraves.graves.GraveModule;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("unused")
public class PlayerDeathListener implements Listener {

	@EventHandler
	public void death(PlayerDeathEvent e) {
		Player player = e.getEntity();

		for(int y = 0; y < 384; y++) {
			if(!new Location(player.getWorld(),
					player.getLocation().getX(), (player.getLocation().getY() - 1) - y, player.getLocation().getZ())
					.getBlock().getType().isAir()) {
				//Found ground

				GraveModule.getInstance().createGrave(
						player.getUniqueId(),
						new ArrayList<>(Arrays.asList(player.getInventory().getContents())),
						player.getLevel(),
						new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - y, player.getLocation().getZ()),
						e.getDeathMessage().replace(player.getName() , ""));

				e.getDrops().clear();
				e.setDroppedExp(0);
				break;
			}
		}
	}
}