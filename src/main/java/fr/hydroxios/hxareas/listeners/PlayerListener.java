package fr.hydroxios.hxareas.listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.hydroxios.hxareas.Area;
import fr.hydroxios.hxareas.Clipboard;
import fr.hydroxios.hxareas.HxAreas;
import fr.hydroxios.hxareas.Vector3;

public class PlayerListener implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getItem() == null)
			return;
		if (e.getItem().equals(HxAreas.areaSetter)) {
			if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
				if (HxAreas.clipboards.containsKey(e.getPlayer())) {
					HxAreas.clipboards.get(e.getPlayer()).setPos1(new Vector3(e.getClickedBlock().getX(),
							e.getClickedBlock().getY(), e.getClickedBlock().getZ()));
				} else {
					Clipboard c = new Clipboard();
					c.setPos1(new Vector3(e.getClickedBlock().getX(), e.getClickedBlock().getY(),
							e.getClickedBlock().getZ()));
					HxAreas.clipboards.put(e.getPlayer(), c);
				}
				e.setCancelled(true);
				e.getPlayer().sendMessage(HxAreas.PREFIX + ChatColor.GREEN + " Pos1 successfuly selected !");
			} else if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
				if (HxAreas.clipboards.containsKey(e.getPlayer())) {
					HxAreas.clipboards.get(e.getPlayer()).setPos2(new Vector3(e.getClickedBlock().getX(),
							e.getClickedBlock().getY(), e.getClickedBlock().getZ()));
				} else {
					Clipboard c = new Clipboard();
					c.setPos2(new Vector3(e.getClickedBlock().getX(), e.getClickedBlock().getY(),
							e.getClickedBlock().getZ()));
					HxAreas.clipboards.put(e.getPlayer(), c);
				}
				e.setCancelled(true);
				e.getPlayer().sendMessage(HxAreas.PREFIX + ChatColor.GREEN + " Pos2 successfuly selected !");
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Area a = HxAreas.instance.getAreaForCoords(e.getPlayer().getLocation());
		if (a != null) {
			if (HxAreas.playersArea.containsKey(e.getPlayer())) {
				Area pa = HxAreas.playersArea.get(e.getPlayer());
				if (pa != a) {
					e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "▶ " + ChatColor.RESET + a.getName()));
					HxAreas.playersArea.replace(e.getPlayer(), a);
				}
			} else {
				e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.AQUA + "▶ " + ChatColor.RESET + a.getName()));
				HxAreas.playersArea.put(e.getPlayer(), a);
			}
		} else {
			if (HxAreas.playersArea.containsKey(e.getPlayer())) {
				HxAreas.playersArea.remove(e.getPlayer());
			}
		}
	}

}
