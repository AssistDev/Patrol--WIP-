package main.java.me.avastprods.patrol;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {

	static Patrol clazz;

	public CommandManager(Patrol instance) {
		clazz = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			System.out.println("Patrol commands can be sent in-game only.");
			return true;
		}
		
		Player s = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("patrol")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("all")) {
					if(args.length == 1) {
						PatrolManager manager = new PatrolManager(clazz);
				
						if(manager.next(s)) {
							s.sendMessage("[Patrol] Now patrolling: " + manager.patrolCurrent.get(s.getName()));
						}
						
						s.sendMessage("[Patrol] Now looping through all online players.");
						return true;
					}
				}
				
				if(args[0].equalsIgnoreCase("stop")) {
					if(args.length == 1) {
						
					}
				}
			}
		}
		
		return false;
	}
}