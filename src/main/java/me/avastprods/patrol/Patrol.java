package main.java.me.avastprods.patrol;

import org.bukkit.plugin.java.JavaPlugin;

public class Patrol extends JavaPlugin {

	public void onEnable() {
		getCommand("patrol").setExecutor(new CommandManager());
	}
}
