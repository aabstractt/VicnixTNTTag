package net.vicnix.tnttag.command.subcommand;

import net.vicnix.tnttag.arena.ArenaException;
import net.vicnix.tnttag.arena.GameArena;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import net.vicnix.tnttag.session.Session;

@SubCommandAnnotation(
        name = "load",
        syntax = "/tnttag load",
        description = "Cargar la arena despues de terminar de configurarla",
        requiresPermission = true
)
public class LoadSubCommand extends SubCommand {

    @Override
    public void execute(Session session, String[] args) {
        try {
            GameArena.getInstance().init();
        } catch (ArenaException e) {
            e.printStackTrace();
        }
    }
}