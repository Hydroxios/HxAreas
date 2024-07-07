package fr.hydroxios.hxareas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.hydroxios.hxareas.listeners.PlayerListener;
import fr.hydroxios.hxlib.utils.ItemBuilder;

public class HxAreas extends JavaPlugin {

	public static HxAreas instance;

	private List<Area> areas = new ArrayList<>();

	public static Map<Player, Clipboard> clipboards = new HashMap<>();

	public static Map<Player, Area> playersArea = new HashMap<>();

	public static ItemStack areaSetter;

	public static final String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.AQUA + "HxAreas" + ChatColor.DARK_GRAY
			+ "]" + ChatColor.RESET;

	@Override
	public void onEnable() {
		instance = this;
		ItemBuilder ib = new ItemBuilder(Material.BLAZE_ROD);
		ib.setDisplayName(ChatColor.RED + "Area Setter");
		List<String> lore = new ArrayList<>();
		lore.add(ChatColor.AQUA + "Left-Click" + ChatColor.GRAY + " set the first corner");
		lore.add(ChatColor.GOLD + "Right-Click" + ChatColor.GRAY + " set the second corner");
		ib.setLore(lore);
		areaSetter = ib.build();

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), instance);

		if (getConfig().contains("areas")) {
			getConfig().getConfigurationSection("areas").getKeys(false).forEach(area -> {
				UUID uuid = UUID.fromString(area);
				String name = getConfig().getString("areas." + area + ".name");
				Vector3 corner1 = new Vector3();
				corner1.setX(getConfig().getInt("areas." + area + ".corner1.x"));
				corner1.setY(getConfig().getInt("areas." + area + ".corner1.y"));
				corner1.setZ(getConfig().getInt("areas." + area + ".corner1.z"));
				Vector3 corner2 = new Vector3();
				corner2.setX(getConfig().getInt("areas." + area + ".corner2.x"));
				corner2.setY(getConfig().getInt("areas." + area + ".corner2.y"));
				corner2.setZ(getConfig().getInt("areas." + area + ".corner2.z"));
				Area a = new Area(uuid, name, corner1, corner2);
				areas.add(a);
			});
		} else {
			getLogger().info("No areas found !");
		}
	}

	@Override
	public void onDisable() {
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("getareasetter")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp()) {
					player.getInventory().addItem(areaSetter);
				} else {
					player.sendMessage(ChatColor.RED + "You don't have the permission to perform this command !");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only players can do this command !");
			}
		}
		if (label.equalsIgnoreCase("savearea")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (p.isOp()) {
					if (clipboards.containsKey(p)) {
						Clipboard c = clipboards.get(p);
						if (c.getPos1() != null && c.getPos2() != null) {
							if (args.length > 0) {
								StringBuilder sb = new StringBuilder();
								for (int i = 0; i < args.length; i++) {
									if(i > 0) sb.append(" ");
									sb.append(args[i]);
								}
								String name = ChatColor.translateAlternateColorCodes('&', sb.toString());
								Area a = new Area(name, c.getPos1(), c.getPos2());
								areas.add(a);
								getConfig().set("areas." + a.getUUID() + ".name", a.getName());
								getConfig().set("areas." + a.getUUID() + ".corner1.x", a.getCorner1().getX());
								getConfig().set("areas." + a.getUUID() + ".corner1.y", a.getCorner1().getY());
								getConfig().set("areas." + a.getUUID() + ".corner1.z", a.getCorner1().getZ());

								getConfig().set("areas." + a.getUUID() + ".corner2.x", a.getCorner2().getX());
								getConfig().set("areas." + a.getUUID() + ".corner2.y", a.getCorner2().getY());
								getConfig().set("areas." + a.getUUID() + ".corner2.z", a.getCorner2().getZ());
								saveConfig();
								p.sendMessage(PREFIX + " Area " + a.getName() + ChatColor.GREEN + " saved !");
							} else {
								p.sendMessage(
										PREFIX + ChatColor.RED + "You need to provied a name eq: /savearea <name>");
							}
						} else {
							p.sendMessage(PREFIX + ChatColor.RED + "You have selected only one corner !");
						}
					} else {
						p.sendMessage(PREFIX + ChatColor.RED + "You don't have selected the corners !");
					}
				} else {
					p.sendMessage(ChatColor.RED + "You don't have the permission to perform this command !");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "Only players can do this command !");
			}
		}
		return false;
	}

	public Area getAreaByUUID(UUID uuid) {
		for (Area a : areas) {
			if (a.getUUID().equals(uuid)) {
				return a;
			}
		}
		return null;
	}

	public Area getAreaForCoords(Location loc) {
		for (Area a : areas) {
			if (loc.getBlockX() < Math.max(a.getCorner1().getX(), a.getCorner2().getX())
					&& loc.getBlockX() > Math.min(a.getCorner1().getX(), a.getCorner2().getX())) {
				if (loc.getBlockY() < Math.max(a.getCorner1().getY(), a.getCorner2().getY())
						&& loc.getBlockY() > Math.min(a.getCorner1().getY(), a.getCorner2().getY())) {
					if (loc.getBlockZ() < Math.max(a.getCorner1().getZ(), a.getCorner2().getZ())
							&& loc.getBlockZ() > Math.min(a.getCorner1().getZ(), a.getCorner2().getZ())) {
						return a;
					}
				}
			}
		}
		return null;
	}

}
