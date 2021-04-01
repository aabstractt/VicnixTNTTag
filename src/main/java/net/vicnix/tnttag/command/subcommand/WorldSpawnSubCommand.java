package net.vicnix.tnttag.command.subcommand;

import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import net.vicnix.tnttag.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.Location;

@SubCommandAnnotation(
        name = "worldspawn",
        syntax = "/tnttag worldspawn",
        description = "Colocar el punto de spawn del mundo",
        requiresPermission = true
)
public class WorldSpawnSubCommand extends SubCommand {

    @Override
    public void execute(Session session, String[] args) {
        Location loc = session.getSessionStorage().getInstance().getLocation();

        TNTTag.getInstance().getConfig().set("worldSpawn", loc);
        TNTTag.getInstance().saveConfig();

        session.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&9World spawn set to &6X: &b%s &6Y: &b%s &6Z: &b%s &6Yaw: &b%s &6Pitch: &b%s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch())));
    }
}