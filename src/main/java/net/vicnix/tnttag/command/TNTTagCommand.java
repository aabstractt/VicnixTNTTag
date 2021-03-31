package net.vicnix.tnttag.command;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.vicnix.tnttag.command.subcommand.ArenaNameSubCommand;
import net.vicnix.tnttag.command.subcommand.LoadSubCommand;
import net.vicnix.tnttag.command.subcommand.LobbySpawnSubCommand;
import net.vicnix.tnttag.command.subcommand.WorldSpawnSubCommand;
import net.vicnix.tnttag.session.Session;
import net.vicnix.tnttag.session.SessionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TNTTagCommand implements CommandExecutor {

    private final Map<String, SubCommand> commands = new HashMap<>();

    public TNTTagCommand() {
        this.registerSubCommand(new LobbySpawnSubCommand());
        this.registerSubCommand(new WorldSpawnSubCommand());
        this.registerSubCommand(new ArenaNameSubCommand());
        this.registerSubCommand(new LoadSubCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("tnttag")) return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Run this command in-game");

            return false;
        }

        Player player = (Player) sender;

        if (args.length <= 0) {
            showHelpMessage(player);

            return false;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);

        SubCommand subCommand = this.getCommand(args[0]);

        if (subCommand == null) {
            showHelpMessage(player);

            return false;
        }

        SubCommandAnnotation annotations = subCommand.getAnnotations();

        if (annotations == null) return false;

        if (newArgs.length == 0 && annotations.requiresArgumentCompletion()) {
            player.spigot().sendMessage(new TextComponent("Uso: "),
                    new ComponentBuilder(annotations.syntax()).color(net.md_5.bungee.api.ChatColor.RED)
                            .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tnttag "
                                    + args[0] + " "))
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(ChatColor.YELLOW + "Click to prepare command.")}))
                            .create()[0]);
            return false;
        }

        if (!player.hasPermission("tnttag.command." + annotations.name()) && annotations.requiresPermission()) {
            player.spigot().sendMessage(new ComponentBuilder("No tienes permisos para ejecutar este comando").color(ChatColor.RED).create());

            return false;
        }

        Session session = SessionManager.getInstance().getSessionPlayer(player);

        if (session == null) {
            player.spigot().sendMessage(new ComponentBuilder("An error occurred!").color(ChatColor.RED).create());

            return false;
        }

        subCommand.execute(session, newArgs);

        return false;
    }

    private void showHelpMessage(Player player) {
        player.spigot().sendMessage(new ComponentBuilder("Comandos de TNT Tag").color(ChatColor.GOLD).bold(true).create());

        TextComponent prepareMSG = new TextComponent("Click para ejecutar el comando.");

        prepareMSG.setColor(ChatColor.WHITE);
        prepareMSG.setItalic(true);

        for (SubCommand command : commands.values()) {
            SubCommandAnnotation annotations = command.getAnnotations();

            if (annotations == null) continue;

            if (!player.hasPermission("tnttag.command." + annotations.name()) && annotations.requiresPermission()) continue;

            TextComponent pt1 = new TextComponent(annotations.syntax());
            pt1.setColor(ChatColor.YELLOW);
            pt1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{prepareMSG}));
            pt1.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tnttag " + annotations.name() + " "));

            TextComponent pt2 = new TextComponent(" - ");
            pt2.setColor(ChatColor.DARK_GRAY);

            TextComponent pt3 = new TextComponent(annotations.description());
            pt3.setColor(ChatColor.WHITE);
            pt3.setItalic(true);

            player.spigot().sendMessage(pt1, pt2, pt3);
        }

        player.spigot().sendMessage(new TextComponent(" "));
    }

    private void registerSubCommand(SubCommand subCommand) {
        SubCommandAnnotation annotation = subCommand.getAnnotations();

        if (annotation == null) return;

        commands.put(annotation.name(), subCommand);
    }

    private SubCommand getCommand(String name) {
        return commands.get(name.toLowerCase());
    }
}