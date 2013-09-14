package main.java.me.avastprods.patrol;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventManager implements Listener {

	static Patrol clazz;

	public EventManager(Patrol instance) {
		clazz = instance;
	}

	@EventHandler
	public void onEvent(PlayerMoveEvent event) {
		PatrolManager manager = new PatrolManager(clazz);

		if (manager.patrolCurrent.containsValue(event.getPlayer().getName())) {
			Player patrolled = event.getPlayer();
			Player patroller = manager.getPatroller(patrolled);

			patroller.getLocation().setX(patrolled.getLocation().getX());
			patroller.getLocation().setY(patrolled.getLocation().getY());
			patroller.getLocation().setZ(patrolled.getLocation().getZ());
			patroller.getLocation().setPitch(patrolled.getLocation().getPitch());
			patroller.getLocation().setYaw(patrolled.getLocation().getYaw());
		}
	}

	@EventHandler
	public void onOpen(InventoryOpenEvent event) {
		PatrolManager manager = new PatrolManager(clazz);

		if (manager.patrolCurrent.containsValue(event.getPlayer().getName())) {
			Player patrolled = (Player) event.getPlayer();
			Player patroller = manager.getPatroller(patrolled);

			patroller.openInventory(patrolled.getInventory());
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		PatrolManager manager = new PatrolManager(clazz);

		if (manager.patrolCurrent.containsValue(event.getPlayer().getName())) {
			Player patroller = manager.getPatroller((Player) event.getPlayer());

			patroller.closeInventory();
		}
	}

	@EventHandler
	public void onClick(InventoryClickEvent event) {
		PatrolManager manager = new PatrolManager(clazz);

		if (manager.patrolCurrent.containsValue(event.getWhoClicked().getName())) {
			Player patrolled = (Player) event.getWhoClicked();
			Player patroller = manager.getPatroller(patrolled);

			patroller.getInventory().setContents(patrolled.getInventory().getContents());
		}
	}
}
