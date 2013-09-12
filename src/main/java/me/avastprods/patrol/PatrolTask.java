package main.java.me.avastprods.patrol;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;


public class PatrolTask implements Runnable {

	Map<String, Integer> patrolTasks = new HashMap<>();
	
	static Patrol clazz;

	public PatrolTask(Patrol instance) {
		clazz = instance;
	}

	private int id;
	private Player player;

	public int getId() {
		return id;
	}

	public void setId(int id, Player player) {
		this.id = id;
		this.player = player;
	}
	
	public void run() {
		PatrolManager manager = new PatrolManager(clazz);
		
		if(!manager.next(player)) manager.stop(player);
	}
}
