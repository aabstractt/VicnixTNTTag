package net.vicnix.tnttag.command;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommandAnnotation {

    /**
     * The name of the subcommand.
     *
     * @return subcommand name
     */
    String name();

    /**
     * The usage of the subcommand.
     *
     * @return subcommand syntax
     */
    String syntax();

    /**
     * Information about the subcommand.
     *
     * @return subcommand description
     */
    String description();

    /**
     * Subcommand requires that the user complete arguments.
     *
     * @return true if requires argument completion
     */
    boolean requiresArgumentCompletion() default false;

    boolean requiresPermission() default false;
}
