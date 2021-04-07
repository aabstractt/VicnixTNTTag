package net.vicnix.tnttag.command;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract void execute(Player player, String[] args);

    /**
     * Gets the Annotation of a PartyAnnotationCommand class.
     *
     * @return Annotation if it exists, null if invalid
     */
    public SubCommandAnnotation getAnnotations() {
        if (this.getClass().isAnnotationPresent(SubCommandAnnotation.class)) {
            return this.getClass().getAnnotation(SubCommandAnnotation.class);
        }

        return null;
    }
}