package net.vicnix.tnttag.command.subcommand;

import com.google.common.io.Files;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.vicnix.tnttag.LevelFactory;
import net.vicnix.tnttag.TNTTag;
import net.vicnix.tnttag.command.SubCommand;
import net.vicnix.tnttag.command.SubCommandAnnotation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SubCommandAnnotation(
        name = "create",
        syntax = "/tnttag create",
        description = "Crear una nueva arena",
        requiresPermission = true,
        requiresArgumentCompletion = true
)
public class CreateSubCommand extends SubCommand {

    @Override
    public void execute(Player player, String[] args) {
        World world = player.getWorld();

        if (LevelFactory.getInstance().getLevels().containsKey(world.getName())) {
            player.spigot().sendMessage(new ComponentBuilder("Este mapa ya se encuentra registrado.").color(ChatColor.RED).create());

            return;
        }

        world.save();

        String folderName = world.getName();

        File target = new File(TNTTag.getInstance().getDataFolder(), "arenas/" + folderName);
        File src = world.getWorldFolder();

        Map<String, Object> data = new HashMap<String, Object>() {{
            this.put("lobbySpawn", player.getLocation());
            this.put("worldSpawn", player.getLocation());
        }};

        Bukkit.getScheduler().runTaskAsynchronously(TNTTag.getInstance(), () -> {
            try {
                //noinspection UnstableApiUsage
                Files.copy(src, target);

                LevelFactory.getInstance().loadLevel(folderName, data).handleUpdate();

                player.sendMessage(ChatColor.GREEN + "Arena " + folderName + " creada correctamente.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
