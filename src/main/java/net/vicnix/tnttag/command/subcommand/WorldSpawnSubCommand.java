package net.vicnix.tnttag.command.subcommand;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.vicnix.tnttag.LevelFactory;
import net.vicnix.tnttag.arena.GameLevel;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@SubCommandAnnotation(
        name = "worldspawn",
        syntax = "/tnttag worldspawn",
        description = "Colocar el punto de spawn del mundo",
        requiresPermission = true
)
public class WorldSpawnSubCommand extends SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        GameLevel level = LevelFactory.getInstance().getLevelWorld(player.getWorld());

        if (level == null) {
            player.spigot().sendMessage(new ComponentBuilder("Arena no encontrada.").color(net.md_5.bungee.api.ChatColor.RED).create());

            return;
        }

        Location loc = player.getLocation();

        level.setWorldSpawn(loc);
        level.handleUpdate();

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&9World spawn set to &6X: &b%s &6Y: &b%s &6Z: &b%s &6Yaw: &b%s &6Pitch: &b%s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch())));
    }
}