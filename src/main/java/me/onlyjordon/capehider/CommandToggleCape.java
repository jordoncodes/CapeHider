package me.onlyjordon.capehider;

import me.onlyjordon.capehider.commands.Command;
import me.onlyjordon.nicknamingapi.Nicknamer;
import me.onlyjordon.nicknamingapi.utils.SkinLayers;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class CommandToggleCape extends Command {

    private final Nicknamer disguiser;

    protected CommandToggleCape(Nicknamer disguiser) {
        super("togglecape", "capehider.togglecape");
        this.disguiser = disguiser;
    }

    @Override
    protected boolean execute(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length == 0 && sender.hasPermission("capehider.togglecape.self")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command.");
                return true;
            }
            boolean newCapeVisibility = !disguiser.getVisibleSkinLayers((Player) sender).contains(SkinLayers.SkinLayer.CAPE);
            disguiser.setSkinLayerVisible(
                    (Player) sender,
                    SkinLayers.SkinLayer.CAPE,
                    newCapeVisibility
            );
            disguiser.refreshPlayer((Player) sender);
            return true;
        }
        if (args.length == 1 && sender.hasPermission("capehider.togglecape.others")) {
            Player target = disguiser.getPlayerWithNick(args[0]);
            if (target == null) {
                sender.sendMessage("Player not found.");
                return true;
            }
            boolean newCapeVisibility = !disguiser.getVisibleSkinLayers(target).contains(SkinLayers.SkinLayer.CAPE);
            disguiser.setSkinLayerVisible(
                    target,
                    SkinLayers.SkinLayer.CAPE,
                    newCapeVisibility
            );
            disguiser.refreshPlayer(target);
            return true;

        }
        return false;
    }

    @Override
    public HashMap<String, Integer> completeTab(CommandSender sender, String alias, String[] args) {
        return new HashMap<String, Integer>() {
            {
                Bukkit.getOnlinePlayers().forEach(p -> put(disguiser.getNick(p), 0));
            }
        };
    }
}
