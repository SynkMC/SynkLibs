package cc.synkdev.synkLibs.components;

import cc.synkdev.synkLibs.bukkit.SynkLibs;
import cc.synkdev.synkLibs.bukkit.commands.ReportCmd;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandIssuer;
import co.aikar.commands.ExceptionHandler;
import co.aikar.commands.RegisteredCommand;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GlobalErrorHandler implements Thread.UncaughtExceptionHandler, ExceptionHandler {
    public GlobalErrorHandler (String url) {
        this.url = url;
    }
    private final String url;
    private Boolean log = true;

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!SynkLibs.getLoopReport()) sendWH(e);
        if (log) logError(e);
        log = true;
    }

    private void logError(Throwable e) {
        throw new RuntimeException(e);
    }

    private void sendWH(Throwable e) {
        String msg = e.toString();
        String stackTrace = Arrays.toString(e.getStackTrace());

        DiscordWebhook wH = new DiscordWebhook(url);

        wH.setContent(":warning: New error caught!");
        wH.setUsername("Error Catcher");
        wH.setAvatarUrl("https://thumbs.dreamstime.com/b/black-silhouette-butterfly-net-classic-net-design-wooden-handle-vector-illustration-isolated-white-background-black-silhouette-116525798.jpg"); //now don't judge that part
        wH.addEmbed(new DiscordWebhook.EmbedObject()
                .setTitle(msg)
                .setDescription("```"+stackTrace+"```")
                .setColor(Color.RED)
                .addField("Dump", new ReportCmd(SynkLibs.getInstance()).send(), false));
        try {
            wH.execute();
            SynkLibs.setLoopReport(false);
        } catch (IOException ex) {
            SynkLibs.setLoopReport(true);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean execute(BaseCommand command, RegisteredCommand registeredCommand, CommandIssuer sender, List<String> args, Throwable t) {
        if (!SynkLibs.getLoopReport()) sendWH(t);
        if (log) logError(t);
        log = true;
        return false;
    }
}
