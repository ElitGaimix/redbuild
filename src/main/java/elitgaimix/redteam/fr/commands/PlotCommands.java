package elitgaimix.redteam.fr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Objects;

import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.Plot.Plot;
import elitgaimix.redteam.fr.json.Plot.PlotFile;
import elitgaimix.redteam.fr.json.profile.AddPlot;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.json.profile.UserPlot;
import elitgaimix.redteam.fr.Plugin;

public class PlotCommands implements CommandExecutor {

    private File saveDir;
    private File saveDir2;
    private Plugin plugin;

    public PlotCommands(Plugin plugin) {
        this.plugin = plugin;
        this.saveDir = new File(plugin.getDataFolder(), this.plugin.getConfig().getString("repertory.main"));
        if (!saveDir.exists()) {
            this.saveDir.mkdir();
        }
        this.saveDir2 = new File(plugin.getDataFolder(), this.plugin.getConfig().getString("repertory.player"));
        if (!saveDir2.exists()) {
            this.saveDir2.mkdir();
        }
    }

    @Override
    // @SuppressWarnings("deprecation")
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Profile profile;
        if ((args[0].equalsIgnoreCase("f") || args[0].equalsIgnoreCase("fusion")) && sender instanceof Player
                && args.length > 0) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {

            }
        }

        if ((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("trust")) && sender instanceof Player
                && args.length > 0) {
            Player p = (Player) sender;
            Player padd = Bukkit.getServer().getPlayer(args[1]);
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())
                    && padd != null) {
                Chunk PlayerChunk = p.getLocation().getChunk();
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                UserPlot Pplot = profile.getPlot();
                Profile addprofile = FileUtils.openProfileFile(new File(saveDir2, padd.getName() + ".json"), padd);
                AddPlot addPplot = profile.getAddplot();
                if (Pplot.getNombre() != 0) {
                    int n = 0;
                    boolean find = false;
                    boolean fin = false;
                    while (!fin) {
                        try {
                            if ((Pplot.getCoX().get(n) == PlayerChunk.getX() + 1
                                    || Pplot.getCoX().get(n) == PlayerChunk.getX()
                                    || Pplot.getCoX().get(n) == PlayerChunk.getX() - 1)
                                    && (Pplot.getCoZ().get(n) == PlayerChunk.getZ() + 1
                                            || Pplot.getCoZ().get(n) == PlayerChunk.getZ()
                                            || Pplot.getCoZ().get(n) == PlayerChunk.getZ() - 1)) {
                                fin = true;
                                find = true;
                            } else {
                                n++;
                            }
                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }
                    }

                    if (find) {
                        List<Integer> coX = addPplot.getCoX();
                        List<Integer> coZ = addPplot.getCoZ();
                        List<Integer> taille = addPplot.getTaille();
                        List<Boolean> trust = addPplot.getTrust();
                        List<String> pla = addPplot.getPlayer();
                        pla.add(p.getName());
                        coX.add(PlayerChunk.getX());
                        coZ.add(PlayerChunk.getZ());
                        taille.add(plugin.getConfig().getInt("plot.tail"));
                        addPplot.setCoX(coX);
                        addPplot.setCoZ(coZ);
                        addPplot.setTaille(taille);
                        addPplot.setPlayer(pla);
                        if (args[0].equalsIgnoreCase("trust")) {
                            trust.add(true);
                        } else {
                            trust.add(false);
                        }

                        addPplot.setTrust(trust);
                        addprofile.setAddplot(addPplot);
                        FileUtils.saveFile(new File(saveDir2, padd.getName() + ".json"), addprofile);
                        p.sendMessage("§aVous avez add " + padd.getName() + " sur votre plot");
                    } else {
                        p.sendMessage("§4Ce plot ne vous appartien pas");
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        }
        if (args[0].equalsIgnoreCase("reset") && sender instanceof Player)

        {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                Chunk PlayerChunk = p.getLocation().getChunk();
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                UserPlot Pplot = profile.getPlot();
                if (Pplot.getNombre() != 0) {
                    int n = 0;
                    boolean find = false;
                    boolean fin = false;
                    while (!fin) {

                        try {
                            if ((Pplot.getCoX().get(n) == PlayerChunk.getX() + 1
                                    || Pplot.getCoX().get(n) == PlayerChunk.getX()
                                    || Pplot.getCoX().get(n) == PlayerChunk.getX() - 1)
                                    && (Pplot.getCoZ().get(n) == PlayerChunk.getZ() + 1
                                            || Pplot.getCoZ().get(n) == PlayerChunk.getZ()
                                            || Pplot.getCoZ().get(n) == PlayerChunk.getZ() - 1)) {
                                fin = true;
                                find = true;
                            } else {
                                n++;
                            }
                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }

                    }
                    if (find) {
                        UserPlot userplot = profile.getPlot();
                        FileUtils.fillBlock((Pplot.getCoX().get(n) * 16 + 32), -63,
                                (userplot.getCoZ().get(n) * 16 + 31), (userplot.getCoX().get(n) * 16 - 16), 0,
                                (userplot.getCoZ().get(n) * 16 - 16), Material.QUARTZ_BLOCK,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(n) * 16 + 32), 1,
                                (userplot.getCoZ().get(n) * 16 + 31), (userplot.getCoX().get(n) * 16 - 16), 255,
                                (userplot.getCoZ().get(n) * 16 - 16), Material.AIR,
                                p.getWorld());
                        p.getWorld().getBlockAt(new Location(p.getWorld(), (Pplot.getCoX().get(n) * 16 + 32), 0,
                                (userplot.getCoZ().get(n) * 16 - 16))).setType(Material.RED_WOOL);
                        p.sendMessage("§aVous avez reset le plot");
                    } else {
                        p.sendMessage("§4Ce plot ne vous appartien pas");
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        }
        if (args[0].equalsIgnoreCase("delete") && sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {

                Chunk PlayerChunk = p.getLocation().getChunk();
                PlotFile plots = Plugin.plotfile;
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                Plot plot;
                UserPlot Pplot = profile.getPlot();
                if (Pplot.getNombre() != 0) {
                    int n = 0;
                    int nn = 0;
                    boolean find = false;
                    boolean fin = false;
                    while (!fin) {
                        try {
                            if ((Pplot.getCoX().get(nn) == PlayerChunk.getX() + 1
                                    || Pplot.getCoX().get(nn) == PlayerChunk.getX()
                                    || Pplot.getCoX().get(nn) == PlayerChunk.getX() - 1)
                                    && (Pplot.getCoZ().get(nn) == PlayerChunk.getZ() + 1
                                            || Pplot.getCoZ().get(nn) == PlayerChunk.getZ()
                                            || Pplot.getCoZ().get(nn) == PlayerChunk.getZ() - 1)) {
                                fin = true;
                                find = true;
                            } else {
                                nn++;
                            }
                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }

                    }
                    fin = false;
                    while (!fin) {
                        try {
                            plot = plots.getPlot().get(n);
                            if ((plot.getCoX() == PlayerChunk.getX() + 1
                                    || plot.getCoX() == PlayerChunk.getX()
                                    || plot.getCoX() == PlayerChunk.getX() - 1)
                                    && (plot.getCoZ() == PlayerChunk.getZ() + 1
                                            || plot.getCoZ() == PlayerChunk.getZ()
                                            || plot.getCoZ() == PlayerChunk.getZ() - 1)) {
                                fin = true;
                            } else {
                                n++;
                            }
                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }

                    }
                    if (find) {
                        List<Plot> finalplot = plots.getPlot();
                        plot = plots.getPlot().get(n);
                        plot.setTake(false);
                        plot.setPlayer("");
                        finalplot.set(n, plot);
                        plots.setPlot(finalplot);
                        FileUtils.saveFile(new File(saveDir, "Plot.json"), plots);
                        Plugin.plotfile = plots;
                        UserPlot userplot = profile.getPlot();
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 + 32), -63,
                                (userplot.getCoZ().get(nn) * 16 + 31), (userplot.getCoX().get(nn) * 16 - 16), 0,
                                (userplot.getCoZ().get(nn) * 16 - 16), Material.QUARTZ_BLOCK,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 + 32), 1,
                                (userplot.getCoZ().get(nn) * 16 + 31), (userplot.getCoX().get(nn) * 16 - 16), 255,
                                (userplot.getCoZ().get(nn) * 16 - 16), Material.AIR,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 + 32), 0,
                                (userplot.getCoZ().get(nn) * 16 + 32), (userplot.getCoX().get(nn) * 16 + 32), 0,
                                (userplot.getCoZ().get(nn) * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 + 32), 0,
                                (userplot.getCoZ().get(nn) * 16 - 17), (userplot.getCoX().get(nn) * 16 - 17), 0,
                                (userplot.getCoZ().get(nn) * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 + 32), 0,
                                (userplot.getCoZ().get(nn) * 16 + 32), (userplot.getCoX().get(nn) * 16 + -17), 0,
                                (userplot.getCoZ().get(nn) * 16 + 32), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((Pplot.getCoX().get(nn) * 16 - 17), 0,
                                (userplot.getCoZ().get(nn) * 16 + 32), (userplot.getCoX().get(nn) * 16 + -17), 0,
                                (userplot.getCoZ().get(nn) * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        List<Integer> usercoX = userplot.getCoX();
                        List<Integer> usercoZ = userplot.getCoZ();
                        usercoX.remove(nn);
                        usercoZ.remove(nn);
                        userplot.setCoX(usercoX);
                        userplot.setCoZ(usercoZ);
                        userplot.setNombre(userplot.getNombre() - 1);
                        profile.setPlot(userplot);
                        FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                        p.sendMessage("§aVous avez supprimer le plot");
                    } else {
                        p.sendMessage("§4Ce plot ne vous appartien pas");
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        }
        if ((args[0].equalsIgnoreCase("visite") || args[0].equalsIgnoreCase("v")) && sender instanceof Player
                && args[1] != null) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                PlotFile plots = Plugin.plotfile;
                Plot plot;
                boolean fin = false;
                boolean find = false;
                int n = 0;
                while (!fin) {
                    plot = plots.getPlot().get(n);

                    try {
                        if (Objects.equals(plot.getPlayer(), args[1])) {
                            fin = true;
                            find = true;
                        } else {
                            n++;
                        }
                    } catch (Exception e) {
                        find = false;
                        fin = true;

                    }

                }
                if (find) {
                    plot = plots.getPlot().get(n);
                    p.sendMessage("§aTéléportation en cours...");
                    try {
                        p.teleport(new Location(
                                Bukkit.getServer().getWorld(this.plugin.getConfig().getString("plot.world")),
                                plot.getCoX() * 16, 1,
                                plot.getCoZ() * 16));
                        p.sendMessage("§aTéléportation réussite");
                    } catch (Exception ex) {
                        p.sendMessage("§4La téléportation a échoué : " + ex.getMessage());
                    }
                } else {
                    p.sendMessage("§4Ce joueur n'a pas de plot");
                }
            }
        }
        if (args[0].equalsIgnoreCase("auto") && sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                PlotFile plots = Plugin.plotfile;
                Plot plot;
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                if (profile.getPlot().getNombre() != 2 || p.isOp()) {
                    int n = 0;
                    boolean find = false;
                    boolean fin = false;
                    while (!fin) {
                        try {
                            plot = plots.getPlot().get(n);
                            if (Boolean.FALSE.equals(plot.getTake())) {
                                fin = true;
                                find = true;
                            } else {
                                n++;
                            }
                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }

                    }
                    if (find) {
                        plot = plots.getPlot().get(n);
                        plot.setTake(false);
                        plot.setPlayer(p.getName());
                        List<Plot> finalPlot = plots.getPlot();
                        finalPlot.set(n, plot);
                        plots.setPlot(finalPlot);
                        FileUtils.saveFile(new File(saveDir, "Plot.json"), plots);
                        Plugin.plotfile = plots;
                        UserPlot userplot = profile.getPlot();
                        List<Integer> usercoX;
                        List<Integer> usercoZ;
                        FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + 32), 0,
                                (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                (plot.getCoZ() * 16 - 17), (plot.getCoX() * 16 - 17), 0,
                                (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + -17), 0,
                                (plot.getCoZ() * 16 + 32), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getCoX() * 16 - 17), 0,
                                (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + -17), 0,
                                (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        usercoX = userplot.getCoX();
                        usercoZ = userplot.getCoZ();
                        usercoX.add(plot.getCoX());
                        usercoZ.add(plot.getCoZ());
                        userplot.setCoX(usercoX);
                        userplot.setCoZ(usercoZ);
                        int usernb = userplot.getNombre() + 1;
                        userplot.setNombre(usernb);
                        profile.setPlot(userplot);
                        FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                        p.teleport(
                                new Location(Bukkit.getServer().getWorld("flatroom"), plot.getCoX() * 16 + 8, 0,
                                        plot.getCoZ() * 16 + 8));
                        p.sendMessage("§aVous avez pris le plot");
                    } else {
                        p.sendMessage("§4Il n'y a plus de plot disponible ! ! !");
                    }
                } else {
                    p.sendMessage("§cVous avez trop de plot");
                }
            }
        }

        if ((args[0].equalsIgnoreCase("home") || args[0].equalsIgnoreCase("h")) && sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                if (profile.getPlot().getNombre() != 0) {
                    int n = 0;
                    p.sendMessage("§aTéléportation en cours...");
                    try {
                        int number = Integer.parseInt(args[1]) - 1;
                        if (number > 0 && number < profile.getPlot().getNombre()) {
                            n = number;
                        }
                    } catch (Exception e) {
                        n = 0;
                    }
                    try {
                        p.teleport(
                                new Location(
                                        Bukkit.getServer().getWorld(this.plugin.getConfig().getString("plot.world")),
                                        profile.getPlot().getCoX().get(n) * 16, 1,
                                        profile.getPlot().getCoZ().get(n) * 16));
                        p.sendMessage("§aTéléportation réussite !");
                    } catch (Exception e) {
                        p.sendMessage("§4La téléportation a échouée : " + e.getMessage());
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        }

        if (args[0].equalsIgnoreCase("claim") && sender instanceof Player)

        {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {

                Chunk pChunk = p.getLocation().getChunk();
                PlotFile plots = (PlotFile) FileUtils.openFile(new File(saveDir, "Plot.json"), PlotFile.class);
                Plot plot;
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                if (profile.getPlot().getNombre() != 2 || p.isOp()) {
                    int coX = 0;
                    int coZ = 0;
                    int n = 0;
                    boolean find = false;
                    boolean fin = false;
                    while (!fin) {
                        try {
                            plot = plots.getPlot().get(n);
                            coX = plot.getCoX();
                            coZ = plot.getCoZ();
                            if ((coX == pChunk.getX() + 1 || coX == pChunk.getX()
                                    || coX == pChunk.getX() - 1)
                                    && (coZ == pChunk.getZ() + 1 || coZ == pChunk.getZ()
                                            || coZ == pChunk.getZ() - 1)) {
                                if (Boolean.TRUE.equals(plot.getTake())) {
                                    find = true;
                                }
                                fin = true;
                            } else {
                                n++;
                            }

                        } catch (Exception e) {
                            find = false;
                            fin = true;

                        }

                    }
                    try {
                        if (!find) {
                            plot = plots.getPlot().get(n);
                            List<Plot> finalPlot = plots.getPlot();
                            plot.setTake(true);
                            plot.setPlayer(p.getName());
                            finalPlot.set(n, plot);
                            plots.setPlot(finalPlot);
                            FileUtils.saveFile(new File(saveDir, "Plot.json"), plots);
                            Plugin.plotfile = plots;
                            UserPlot userplot = profile.getPlot();
                            FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                    (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + 32), 0,
                                    (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                    (plot.getCoZ() * 16 - 17), (plot.getCoX() * 16 - 17), 0,
                                    (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getCoX() * 16 + 32), 0,
                                    (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + -17), 0,
                                    (plot.getCoZ() * 16 + 32), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getCoX() * 16 - 17), 0,
                                    (plot.getCoZ() * 16 + 32), (plot.getCoX() * 16 + -17), 0,
                                    (plot.getCoZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            List<Integer> usercoX;
                            List<Integer> usercoZ;
                            usercoX = userplot.getCoX();
                            usercoZ = userplot.getCoZ();
                            usercoX.add(plot.getCoX());
                            usercoZ.add(plot.getCoZ());
                            userplot.setCoX(usercoX);
                            userplot.setCoZ(usercoZ);
                            int usernb = userplot.getNombre() + 1;
                            userplot.setNombre(usernb);
                            profile.setPlot(userplot);
                            FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                            p.sendMessage("§aVous avez pris le plot");
                        } else {
                            plot = plots.getPlot().get(n);
                            if (Boolean.TRUE.equals(plot.getTake())) {
                                p.sendMessage("§4Le plot est déjà pris");
                            } else {
                                p.sendMessage("§4Le plot n'existe pas");
                            }

                        }
                    } catch (Exception e) {
                        p.sendMessage("§4Le plot n'existe pas");
                    }

                } else {
                    p.sendMessage("§4Vous avez trop de plot");
                }
            }
        }
        return false;
    }
}
