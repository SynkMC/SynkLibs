package cc.synkdev.synkLibs.bukkit.commands;


import cc.synkdev.synkLibs.bukkit.Analytics;
import cc.synkdev.synkLibs.bukkit.Lang;
import cc.synkdev.synkLibs.bukkit.SynkLibs;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Subcommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandAlias("sl|synklibs")
public class SlCmd extends BaseCommand {
    private final SynkLibs core;
    public SlCmd(SynkLibs core) {
        this.core = core;
    }

    @Subcommand("reload")
    @CommandPermission("synklibs.command.reload")
    public void onReload(CommandSender sender) {
        core.loadConfig();
        sender.sendMessage(core.prefix()+ ChatColor.GREEN+ Lang.translate("reloaded", core));
    }
}
