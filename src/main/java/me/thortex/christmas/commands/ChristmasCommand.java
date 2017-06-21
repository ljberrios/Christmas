package me.thortex.christmas.commands;

import com.google.inject.Inject;
import me.thortex.christmas.common.command.Command;
import me.thortex.christmas.common.util.Colors;
import me.thortex.christmas.tree.TreeConfig;
import me.thortex.christmas.util.Trees;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ChristmasCommand implements Command {

    private final TreeConfig config;

    @Inject
    public ChristmasCommand(TreeConfig config) {
        this.config = config;
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("mct");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("tree")) {
                config.setTreeLocation(location);
                Trees.placeTree(location, config.getTreeSchematic());
                player.sendMessage(Colors.GREEN + "Set the tree's location!");
            } else if (args[0].equalsIgnoreCase("present")) {
                config.addPresentLocation(location);
                player.sendMessage(Colors.GREEN + "Added present location!");
            } else {
                help(player);
            }
        } else {
            help(player);
        }
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.isOp();
    }

    private void help(Player player) {
        player.sendMessage(Colors.DARK_GRAY + "---- " + Colors.RED + Colors.BOLD
            + "ChristmasPlugin Help " + Colors.DARK_GRAY + "----");
        player.sendMessage(" ");
        player.sendMessage(Colors.GREEN + "/mct tree " + Colors.DARK_GRAY + "- "
            + Colors.GRAY + "Place the tree.");
        player.sendMessage(" ");
        player.sendMessage(Colors.GREEN + "/mct present " + Colors.DARK_GRAY
            + "- " + Colors.GRAY + "Set a present location.");
        player.sendMessage(" ");
        player.sendMessage(Colors.GREEN + "/mct " + Colors.DARK_GRAY + "- "
            + Colors.GRAY + " Show this menu.");
    }

}

