package main.java.me.avastprods.patrol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PatrolManager {

	public Patrol clazz;
	
	public PatrolManager(Patrol instance) {
		clazz = instance;
	}
	
	/**
	 * Contains information of patroller and already patrolled players.
	 * [Patroller, Patrolled players]
	 */
	public Map<String, List<String>> patrolList = new HashMap<>();
	
	/**
	 * Contains information of patroller and patroller phase.
	 * [Patroller, Phase]
	 */
	public Map<String, Integer> patrolPhase = new HashMap<>();
	
	/**
	 * Contains information of patroller and currently patrolled player.
	 * [Patroller, Patrolled]
	 */
	public Map<String, String> patrolCurrent = new HashMap<>();
	
	public Player getCurrent(Player player) {
		if(patrolCurrent.containsKey(player.getName())) {
			return Bukkit.getPlayer(patrolCurrent.get(player.getName()));
		}
		
		return null;
	}
	
	/**
	 * 
	 * @param player
	 * @return - Next patrolled player
	 */
	public Player next(Player player) {
		if(patrolList.containsKey(player.getName())) {
			int phase = patrolPhase.get(player.getName());
			
			if(phase <= Bukkit.getOnlinePlayers().length) {
				int next = phase + 1;
				
				Random rand = new Random();
				
				for(String str : patrolList.get(player.getName())) {
					if(!str.equalsIgnoreCase(Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)].getName())) {
						Player r = Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)];
						patrolPhase.put(player.getName(), next);
						patrolCurrent.put(player.getName(), r.getName());
						
						List<String> list = patrolList.get(player.getName());
						list.add(r.getName());
						
						patrolList.put(player.getName(), list);
						return r;
					}
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Sets up the patrol.
	 * @param player - Patroller
	 * @param target - Player to be patrolled
	 */
	public void patrol(Player player, Player target) {
		target.hidePlayer(player);
		
		player.teleport(target.getLocation());
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
	}
}
