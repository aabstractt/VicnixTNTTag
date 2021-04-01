package net.vicnix.tnttag.command.subcommand;

import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import net.vicnix.tnttag.session.Session;
import org.bukkit.ChatColor;

@SubCommandAnnotation(
        name = "load",
        syntax = "/tnttag load",
        description = "Cargar la arena despues de terminar de configurarla",
        requiresPermission = true
)
public class LoadSubCommand extends SubCommand {

    @Override
    public void execute(Session session, String[] args) {
        GameArena.getInstance().init();

        session.sendMessage(ChatColor.BLUE + "Arena cargada!");
    }
}