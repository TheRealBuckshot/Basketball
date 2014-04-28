package me.therealbuckshot.basketball;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin implements Listener {
	private ArrayList<Entity> balls = new ArrayList<Entity>();
	private ArrayList<Player> shooter = new ArrayList<Player>();
	private ArrayList<Player> red = new ArrayList<Player>();
	private ArrayList<Player> blue = new ArrayList<Player>();
	int redscore = 0;
	int bluescore = 0;
	int testint = 900;
	int countdown = 20;
	int startcountdown = 0;
	int startgame = 0;
	World w;
	Location pos;
	
	

	Scoreboard board;
	Scoreboard cboard;
	Score rScore;
	Score bScore;
	Score c;
	Score online;
	Score test;
	Objective obj;
	Objective obj1;

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		ScoreboardManager sbManager = Bukkit.getScoreboardManager();
		board = sbManager.getNewScoreboard();
		obj = board.registerNewObjective("score", "dummy");

		getConfig().options().copyDefaults(true);
		saveConfig();
		w =Bukkit.getWorld(getConfig().getString("arena.world"));
		pos = new Location(w, getConfig().getInt("arena.x"), getConfig().getInt("arena.y"), getConfig().getInt("arena.z"));
		
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj.setDisplayName(ChatColor.AQUA + "Scoreboard");

		test = obj.getScore(Bukkit.getServer().getOfflinePlayer(
				ChatColor.BLUE + "" + ChatColor.BOLD + "Time"));
		test.setScore(testint);
		rScore = obj.getScore(Bukkit.getServer().getOfflinePlayer(
				ChatColor.RED + "RedScore"));
		bScore = obj.getScore(Bukkit.getServer().getOfflinePlayer(
				ChatColor.BLUE + "BlueScore"));
		rScore.setScore(redscore);
		bScore.setScore(bluescore);

		cboard = sbManager.getNewScoreboard();
		obj1 = cboard.registerNewObjective("score", "dummy");

		obj1.setDisplaySlot(DisplaySlot.SIDEBAR);
		obj1.setDisplayName(ChatColor.AQUA + "Countdown");

		c = obj1.getScore(Bukkit.getServer().getOfflinePlayer(
				ChatColor.ITALIC + "" + ChatColor.BOLD + "Time Left"));
		
		online = obj1.getScore(Bukkit.getServer().getOfflinePlayer(
				ChatColor.GREEN + "" + ChatColor.BOLD + "Players"));
		c.setScore(countdown);
		

	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (label.equalsIgnoreCase("addarena")) {
			Player p = (Player) sender;
			w = p.getWorld();
			String world = w.getName();
			getConfig().set("arena.world", world);
			int X = pos.getBlockX();
			int Y = pos.getBlockY();
			int Z = pos.getBlockZ();
			getConfig().set("arena.x", X);
			getConfig().set("arena.y", Y);
			getConfig().set("arena.z", Z);
			saveConfig();
			
			
		}
		if (label.equalsIgnoreCase("addscorered")) {
			if (!(sender instanceof Player)) {
				redscore++;
				rScore.setScore(redscore);
				for (Entity en : w
						.getEntitiesByClasses(Slime.class))
					en.remove();
				
				Slime ball = (Slime) w
						.spawnEntity(pos, EntityType.SLIME);
				ball.setRemoveWhenFarAway(false);
				ball.setSize(1);
				LivingEntity lv = (LivingEntity) ball;
				lv.setCustomNameVisible(true);
				lv.setCustomName(ChatColor.RED + "Basketball");
				balls.add(ball);
				
				obj.setDisplayName(ChatColor.RED + "Red Scores!");
				this.getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable(){
					public  void run(){
						obj.setDisplayName(ChatColor.AQUA + "Scoreboard");
					}
				}, 200L);

				if(redscore == 25){
					this.getServer().reload();
					for (Player p : Bukkit.getServer()
							.getOnlinePlayers()) {
						p.kickPlayer(ChatColor.GOLD + "GAME OVER " + ChatColor.RED + "" + ChatColor.BOLD + "RED WINS!");
					}
				}
			}
		}
		if (label.equalsIgnoreCase("addscoreblue")) {
			if (!(sender instanceof Player)) {
				bluescore++;
				bScore.setScore(bluescore);
				for (Entity en : w
						.getEntitiesByClasses(Slime.class))
					en.remove();
				
				Slime ball = (Slime) w
						.spawnEntity(pos, EntityType.SLIME);
				ball.setRemoveWhenFarAway(false);
				ball.setSize(1);
				LivingEntity lv = (LivingEntity) ball;
				lv.setCustomNameVisible(true);
				lv.setCustomName(ChatColor.RED + "Basketball");
				balls.add(ball);

				
				obj.setDisplayName(ChatColor.BLUE + "Blue Scores!");
				this.getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable(){
					public  void run(){
						obj.setDisplayName(ChatColor.AQUA + "Scoreboard");
					}
				}, 200L);
					
				if(bluescore == 25){
					this.getServer().reload();
					for (Player p : Bukkit.getServer()
							.getOnlinePlayers()) {
						p.kickPlayer(ChatColor.GOLD + "GAME OVER " + ChatColor.BLUE + "" + ChatColor.BOLD + "BLUE WINS!");
						
					}
				}
			}
		}
		return false;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onRightClick(PlayerInteractEntityEvent event) {
		if (balls.contains(event.getRightClicked())) {
			event.setCancelled(true);
			event.getRightClicked().setVelocity(
					event.getPlayer().getLocation().getDirection().normalize()
							.multiply(2));
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDdeath(EntityDamageByEntityEvent e) {
		if (balls.contains(e.getEntity())) {
			final Player p = (Player) e.getDamager();
			shooter.add(p);
			p.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "Shoot!");
			p.getInventory().setItemInHand(new ItemStack(Material.SLIME_BALL));
			

			this.getServer().getScheduler()
					.scheduleAsyncDelayedTask(this, new Runnable() {

						@Override
						public void run() {
							if (shooter.contains(p)) {

								p.setHealth(0);
								p.setLevel(0);
								p.setExp(0f);
								
								Slime ball = (Slime) w.spawnEntity(pos,
										EntityType.SLIME);
								ball.setRemoveWhenFarAway(false);
								ball.setSize(1);
								LivingEntity lv = (LivingEntity) ball;
								lv.setCustomNameVisible(true);
								lv.setCustomName(ChatColor.RED + "Basketball");
								balls.add(ball);
								shooter.remove(p);

							}

						}

					}, 60L);

			if (!(e.getCause() == DamageCause.ENTITY_ATTACK)) {
				e.setCancelled(true);
				e.setDamage(0);
			}
		}
	}

	@EventHandler
	public void onEntityDeath(EntityDeathEvent e) {
		if (balls.contains(e.getEntity())) {
			e.getDrops().clear();
			balls.remove(e.getEntity());
			Entity ent = e.getEntity();
			EntityDamageEvent ede = ent.getLastDamageCause();
			DamageCause dc = ede.getCause();
			if (dc.equals(DamageCause.FALL)) {
				Location pos = e.getEntity().getLocation();
				Slime ball = (Slime) w
						.spawnEntity(pos, EntityType.SLIME);
				ball.setRemoveWhenFarAway(false);
				ball.setSize(1);
				LivingEntity lv = (LivingEntity) ball;
				lv.setCustomNameVisible(true);
				lv.setCustomName(ChatColor.RED + "Basketball");
				balls.add(ball);
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (shooter.contains(p)) {
			shooter.remove(p);
			Location pos = p.getLocation();
			Slime ball = (Slime) w
					.spawnEntity(pos, EntityType.SLIME);
			ball.setRemoveWhenFarAway(false);
			ball.setSize(1);
			LivingEntity lv = (LivingEntity) ball;
			lv.setCustomNameVisible(true);
			lv.setCustomName(ChatColor.RED + "Basketball");
			balls.add(ball);
			ball.setVelocity(p.getLocation().getDirection().normalize()
					.multiply(2));
			p.getInventory().remove(new ItemStack(Material.SLIME_BALL));

		}

	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		if (shooter.contains(e.getEntity())) {
			shooter.remove(e.getEntity());
			e.getDrops().clear();
			e.getEntity().setLevel(0);
			e.getEntity().setExp(0f);

		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(cboard);
		if (this.getServer().getOnlinePlayers().length >= 1) {
			online.setScore(this.getServer().getOnlinePlayers().length);
			startcountdown++;
			if (startcountdown == 1) {
				startcountdown++;
				startgame++;
				
				this.getServer().getScheduler()
						.scheduleAsyncRepeatingTask(this, new Runnable() {
ItemStack i = new ItemStack(Material.LEATHER_HELMET, 1);
LeatherArmorMeta im = (LeatherArmorMeta) i.getItemMeta();

							@Override
							public void run() {
								if (countdown > 0) {
									countdown--;
									c.setScore(countdown);
								}
								if (countdown == 0) {
									countdown--;
									
									game();
									
									Slime ball = (Slime) w
											.spawnEntity(pos, EntityType.SLIME);
									ball.setRemoveWhenFarAway(false);
									ball.setSize(1);
									LivingEntity lv = (LivingEntity) ball;
									lv.setCustomNameVisible(true);
									lv.setCustomName(ChatColor.RED + "Basketball");
									balls.add(ball);
									for (Player p : Bukkit.getServer()
											.getOnlinePlayers()) {
										startcountdown = 1;
										
										p.teleport(pos);
										Random object = new Random();
										p.setScoreboard(board);
										int random;
										
										for (int counter = 1; counter <= 1; counter++) {
											random = 1 + object.nextInt(2);
											if (random == 1) {
												red.add(p);
												im.setColor(Color.RED);
												i.setItemMeta(im);
												p.getInventory().setHelmet(i);
											} else {
												blue.add(p);
												im.setColor(Color.BLUE);
												i.setItemMeta(im);
												p.getInventory().setHelmet(i);
											}
										}
									}
								}
							}

						}, 0L, 20L);
			}
		}

	}

	@SuppressWarnings("deprecation")
	public void game() {
	
			
			this.getServer().getScheduler()
					.scheduleAsyncRepeatingTask(this, new Runnable() {

						@Override
						public void run() {
							if (testint != -1) {
								testint--;
								test.setScore(testint);
							} else {
								for (Player p : Bukkit.getServer()
										.getOnlinePlayers()) {
									p.kickPlayer(ChatColor.GOLD + "GAME OVER");
									p.getServer().reload();
								}
							}

						}

					}, 0L, 20L);
		
	}

}
