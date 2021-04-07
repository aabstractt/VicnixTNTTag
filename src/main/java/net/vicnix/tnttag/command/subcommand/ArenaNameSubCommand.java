package net.vicnix.tnttag.command.subcommand;

import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import net.vicnix.tnttag.session.Session;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@SubCommandAnnotation(
        name = "arenaname",
        syntax = "/tnttag arenaname",
        description = "Dar un nombre de arena al mapa que registraste",
        requiresPermission = true,
        requiresArgumentCompletion = true
)
public class ArenaNameSubCommand extends SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        TNTTag.getInstance().getConfig().set("arenaName", args[0]);
        TNTTag.getInstance().saveConfig();

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Ahora el nombre de la arena es " + args[0]);
    }
}