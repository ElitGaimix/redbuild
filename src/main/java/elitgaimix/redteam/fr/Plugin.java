package elitgaimix.redteam.fr;

import elitgaimix.redteam.fr.listeners.*;
import elitgaimix.redteam.fr.npc.npc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import elitgaimix.redteam.fr.commands.*;
import elitgaimix.redteam.fr.commands.invsee;
import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.Plot.PlotFile;
import elitgaimix.redteam.fr.json.load.EditorSign;
import elitgaimix.redteam.fr.json.load.PlayerFile;
import elitgaimix.redteam.fr.json.npc.NPCJson;
import elitgaimix.redteam.fr.json.profile.Profile;

public class Plugin extends JavaPlugin {
    public static PlotFile plotfile;
    public static NPCJson npc = new NPCJson(null);
    public static PlayerFile playerfile = new PlayerFile(null);
    public static List<EditorSign> OpenEditorSign = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("menu").setExecutor(new PluginManagerCommand(this));
        getCommand("plot").setExecutor(new PlotCommands(this));
        getCommand("tphere").setExecutor(new tp(this));
        getCommand("tp").setExecutor(new tp(this));
        getCommand("build").setExecutor(new build(this));
        getCommand("unicode").setExecutor(new unicode());
        getCommand("invsee").setExecutor(new invsee());
        getCommand("npc").setExecutor(new npc(this));

        getServer().getPluginManager().registerEvents(new MenuGui(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        this.getLogger().info("plugin enable!");
        if (!this.getDataFolder().exists()) {
            this.getDataFolder().mkdir();
        }
        if (!new File(new File(this.getDataFolder(), this.getConfig().getString("repertory.main")), "Plot.json")
                .exists()) {
            FileUtils.createPlotFile(
                    new File(new File(this.getDataFolder(), this.getConfig().getString("repertory.main")), "Plot.json"),
                    this);
        }
        plotfile = (PlotFile) FileUtils.openFile(
                new File(new File(getDataFolder(), getConfig().getString("repertory.main")), "Plot.json"),
                PlotFile.class);

        if (!new File(new File(this.getDataFolder(), this.getConfig().getString("repertory.main")), "NPC.json")
                .exists()) {
            try {
                FileUtils.creatFile(
                        new File(new File(this.getDataFolder(), this.getConfig().getString("repertory.main")),
                                "NPC.json"));
                FileUtils.saveFile(
                        new File(new File(this.getDataFolder(), this.getConfig().getString("repertory.main")),
                                "NPC.json"),
                        new NPCJson(new ArrayList<>()));
            } catch (Exception e) {
                this.getLogger().warning(e.getMessage());
            }
        } else {
            npc = (NPCJson) FileUtils.openFile(
                    new File(new File(getDataFolder(), getConfig().getString("repertory.main")), "NPC.json"),
                    NPCJson.class);
        }
        boolean fin = false;
        File repertory = new File(getDataFolder(), getConfig().getString("repertory.player"));
        String[] findfile = repertory.list();
        int n = 0;
        List<Profile> pr = new ArrayList<Profile>();
        while (fin != true) {
            try {
                pr.add((Profile) FileUtils.openFile(
                        new File(new File(getDataFolder(), getConfig().getString("repertory.player")), findfile[n]),
                        Profile.class));
                n++;
            } catch (Exception e) {
                fin = true;
            }

        }
        playerfile.setProfile(pr);

    }

    @Override
    public void onDisable() {
        this.getLogger().info("plugin disabled!");
    }

}
