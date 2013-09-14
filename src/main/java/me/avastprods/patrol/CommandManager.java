package main.java.me.avastprods.patrol;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
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
		PatrolManager manager = new PatrolManager(clazz);
		
		if(cmd.getName().equalsIgnoreCase("patrol")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("all")) {
					if(args.length == 1) {	
						if(manager.run(s)) {
							s.sendMessage("[Patrol] Now patrolling: " + manager.patrolCurrent.get(s.getName()));
						} else {
							s.sendMessage("[Patrol] Found 0 available players.");
							return true;
						}
						
						s.sendMessage("[Patrol] Now looping through all online players.");
						return true;
					}
					
					return true;
				}
				
				if(args[0].equalsIgnoreCase("stop")) {
					if(args.length == 1) {
						s.sendMessage("[Patrol] Finished patrolling.");
						s.sendMessage("[Patrol] Players you patrolled: " + StringUtils.join(manager.patrolList.get(s.getName()).toArray(), ' ', 0, manager.patrolList.get(s.getName()).toArray().length));
						manager.stop(s);
					}
					
					return true;
				}
				
				Player target = Bukkit.getPlayer(args[0]);
				
				if(target == null || !target.isOnline()) {
					s.sendMessage("[Patrol] Player " + args[0] + " not found.");
					return true;
				}
				
				manager.patrol(s, target, false);
				s.sendMessage("[Patrol] Now patrolling " + target.getName());
			}
		}
		
		return false;
	}
}
