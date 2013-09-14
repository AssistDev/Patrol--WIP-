package main.java.me.avastprods.patrol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
	
	/**
	 * Contains information of patroller's inventory contents.
	 */
	public Map<String, ItemStack[]> patrolInv = new HashMap<>();
	
	/**
	 * Contains information of patroller's armor contents.
	 */
	public Map<String, ItemStack[]> patrolArmor = new HashMap<>();
	
	/**
	 * Contains information of patroller's health.
	 */
	public Map<String, Double> patrolHealth = new HashMap<>();
	
	/**
	 * Contains information of patroller's hunger.
	 */
	public Map<String, Integer> patrolHunger = new HashMap<>();
	
	/**
	 * Contains information of patroller's exp level.
	 */
	public Map<String, Integer> patrolLevel = new HashMap<>();
	
	/**
	 * Contains information of patroller's experience.
	 */
	public Map<String, Float> patrolExp = new HashMap<>();
	
	public Player getCurrent(Player player) {
		if(patrolCurrent.containsKey(player.getName())) {
			return Bukkit.getPlayer(patrolCurrent.get(player.getName()));
		}
		
		return null;
	}
	
	/**
	 * Gets the next random patrolled player.
	 * @param player
	 * @return - Next patrolled player
	 */
	public boolean run(Player player) {
		if (patrolList.containsKey(player.getName())) {
			int phase = patrolPhase.get(player.getName());

			if (phase <= Bukkit.getOnlinePlayers().length) {
				Random rand = new Random();

				for (String str : patrolList.get(player.getName())) {
					if (!str.equalsIgnoreCase(Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)].getName()) && str.equalsIgnoreCase(player.getName())) {
						Player r = Bukkit.getOnlinePlayers()[rand.nextInt(Bukkit.getOnlinePlayers().length)];

						List<String> list = patrolList.get(player.getName());
						list.add(r.getName());

						patrolList.put(player.getName(), list);

						patrolPhase.put(player.getName(), phase + 1);
						patrolCurrent.put(player.getName(), r.getName());

						patrol(player, r, true);

						return true;
					}
				}
			}
		}

		return false;
	}
	
	/**
	 * Sets up the patrol.
	 * @param player - Patroller
	 * @param target - Player to be patrolled
	 */
	public void patrol(Player player, Player target, boolean next) {
		if (next) {
			target.hidePlayer(player);

			player.teleport(target.getLocation());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));

			PatrolTask task = new PatrolTask(clazz);
			task.setId(Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(clazz, task, 200L), player);
		} else {
			target.hidePlayer(player);

			player.teleport(target.getLocation());
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
			
			patrolCurrent.put(player.getName(), target.getName());
		}
	}
	
	/**
	 * Prepares a patroller for the first patrol.
	 * @param player Patroller
	 */
	public void prepare(Player player) {
		this.patrolInv.put(player.getName(), player.getInventory().getContents());
		this.patrolArmor.put(player.getName(), player.getInventory().getArmorContents());
		//this.patrolHealth.put(player.getName(), player.getHealth());
		this.patrolHunger.put(player.getName(), player.getFoodLevel());
		this.patrolLevel.put(player.getName(), player.getLevel());
		this.patrolExp.put(player.getName(), player.getExp());
	}
	
	/**
	 * Stops the patrol for given player.
	 * @param player Patroller
	 */
	public void stop(Player player) {
		if(patrolList.containsKey(player.getName())) {
			for(String str : patrolList.get(player.getName())) {
				Bukkit.getPlayer(str).showPlayer(player);
			}
			
			PatrolTask task = new PatrolTask(clazz);
			
			if(task.patrolTasks.containsKey(player.getName())) {
				clazz.getServer().getScheduler().cancelTask(task.patrolTasks.get(player.getName()));
			}
			
			patrolList.remove(player.getName());
			patrolPhase.remove(player.getName());
			patrolCurrent.remove(player.getName());
			
			player.removePotionEffect(PotionEffectType.INVISIBILITY);
			
			player.getInventory().setContents(patrolInv.get(player.getName()));
			player.getInventory().setArmorContents(patrolArmor.get(player.getName()));
			player.setHealth(patrolHealth.containsKey(player.getName()) ? patrolHealth.get(player.getName()) : 10.0);
			player.setFoodLevel(patrolHunger.get(player.getName()));
			player.setLevel(patrolLevel.get(player.getName()));
			player.setExp(patrolExp.get(player.getName()));
			
			patrolInv.remove(player.getName());
			patrolArmor.remove(player.getName());
			patrolHealth.remove(player.getName());
			patrolHunger.remove(player.getName());
			patrolLevel.remove(player.getName());
			patrolExp.remove(player.getName());
		}
	}
	
	/**
	 * Gets the patroller of a player.
	 * @param patrolled Patrolled player.
	 * @return If exists, the patroller of patrolled, otherwise null.
	 */
	public Player getPatroller(Player patrolled) {
		for (Entry<String, String> entry : patrolCurrent.entrySet()) {
			if (entry.getValue().equalsIgnoreCase(patrolled.getName())) {
				return Bukkit.getPlayer(entry.getKey());
			}
		}
		
		return null;
	}
}
