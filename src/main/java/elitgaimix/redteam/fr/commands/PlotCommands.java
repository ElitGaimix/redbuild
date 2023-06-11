package elitgaimix.redteam.fr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.*;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.List;
import java.util.Objects;

import elitgaimix.redteam.fr.json.FileUtils;
import elitgaimix.redteam.fr.json.Plot.Plot;
import elitgaimix.redteam.fr.json.Plot.PlotFile;
import elitgaimix.redteam.fr.json.Plot.PlotFusion;
import elitgaimix.redteam.fr.json.profile.AddPlot;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.json.profile.UserPlot;
import elitgaimix.redteam.fr.json.profile.UserPlotFusion;
import net.md_5.bungee.api.ChatColor;
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
        if ((args[0].equalsIgnoreCase("f") || args[0].equalsIgnoreCase("fusion")) && sender instanceof Player && args.length > 0) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                Plot plot = FileUtils.getPlot(p);
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                Plot otherPlot = null;
                if(p.getFacing() == BlockFace.EAST && FileUtils.getPlayerPlotByXZ(profile, (plot.getX() + 1) * 4 ,plot.getZ() * 4) != null){
                    otherPlot = FileUtils.getPlotByXZ((plot.getX() + 1) * 4 ,plot.getZ() * 4);
                }else if(p.getFacing() == BlockFace.WEST && FileUtils.getPlayerPlotByXZ(profile, (plot.getX() - 1) * 4 ,plot.getZ() * 4) != null){
                    otherPlot = FileUtils.getPlotByXZ((plot.getX() - 1) * 4 ,plot.getZ() * 4);
                }else if(p.getFacing() == BlockFace.SOUTH && FileUtils.getPlayerPlotByXZ(profile, plot.getX()* 4 ,(plot.getZ() + 1) * 4) != null){
                    otherPlot = FileUtils.getPlotByXZ(plot.getX()* 4 ,(plot.getZ() + 1) * 4);
                }else if(p.getFacing() == BlockFace.NORTH && FileUtils.getPlayerPlotByXZ(profile, plot.getX()* 4 ,(plot.getZ() - 1) * 4) != null){
                    otherPlot = FileUtils.getPlotByXZ( plot.getX()* 4 ,(plot.getZ() - 1) * 4);
                }
                if(otherPlot != null){
                    plugin.plotfile.getPlot().remove(plot);
                    plugin.plotfile.getPlot().remove(otherPlot);
                    plot.getPlotFusion().add(new PlotFusion(otherPlot.getChunk().getX(), otherPlot.getChunk().getZ(), otherPlot.getTail()));
                    otherPlot.getPlotFusion().add(new PlotFusion(plot.getChunk().getX(), plot.getChunk().getZ(), plot.getTail()));
                    plugin.plotfile.getPlot().add(otherPlot);
                    plugin.plotfile.getPlot().add(plot);
                    UserPlot userplot = FileUtils.getPlayerPlot(p, profile);
                    UserPlot otherUserPlot = FileUtils.getPlayerPlotByXZ(profile, otherPlot.getChunk().getX(), otherPlot.getChunk().getZ());
                    profile.getPlot().remove(userplot);
                    profile.getPlot().remove(otherUserPlot);
                    userplot.getFusions().add(new UserPlotFusion(otherPlot.getChunk().getX(), otherPlot.getChunk().getZ(), otherPlot.getTail()));
                    otherUserPlot.getFusions().add(new UserPlotFusion(plot.getChunk().getX(), plot.getChunk().getZ(), plot.getTail()));
                    profile.getPlot().add(otherUserPlot);
                    profile.getPlot().add(userplot);
                    FileUtils.saveFile(new File(saveDir, "Plot.json"), plugin.plotfile);
                    p.sendMessage(ChatColor.GREEN + "Vous avez fusionner les plots");
                }
            }
        }

        if ((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("trust")) && sender instanceof Player
                && args.length > 0) {
            Player p = Bukkit.getServer().getPlayer(args[1]);
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), ((Player) sender).getWorld().getName())
                    && p != null) {
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                    UserPlot userplot = FileUtils.getPlayerPlot((Player) sender, profile);
                    AddPlot addPlot = FileUtils.getPlayerAddPlot((Player) sender, profile);
                    if (userplot != null && addPlot.getTrust() == true) {
                        Boolean trust = false;
                        if (args[0].equalsIgnoreCase("trust")) {
                            trust = true;
                        }
                        profile.getAddplot().add(new AddPlot(userplot.getCoX(), userplot.getCoZ(), plugin.getConfig().getInt(""), trust, ((Player) sender).getName()));
                        FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                        ((Player) sender).sendMessage("§aVous avez add " + p.getName() + " sur votre plot");
                    } else {
                        ((Player) sender).sendMessage("§4Ce plot ne vous appartien pas");
                    }
                } else {
                    ((Player) sender).sendMessage("§4Ce joueur n'existe pas/n'est pas connécté");
                }
            }
        if (args[0].equalsIgnoreCase("reset") && sender instanceof Player){
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                UserPlot userplot = FileUtils.getPlayerPlot(p, profile);
                    if (userplot != null) {

                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), -63,
                                (userplot.getCoZ() * 16 + 31), (userplot.getCoX() * 16 - 16), 0,
                                (userplot.getCoZ() * 16 - 16), Material.QUARTZ_BLOCK,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), 1,
                                (userplot.getCoZ() * 16 + 31), (userplot.getCoX() * 16 - 16), 255,
                                (userplot.getCoZ() * 16 - 16), Material.AIR,
                                p.getWorld());
                        p.getWorld().getBlockAt(new Location(p.getWorld(), (userplot.getCoX() * 16 + 32), 0,
                                (userplot.getCoZ() * 16 - 16))).setType(Material.RED_WOOL);
                        p.sendMessage("§aVous avez reset le plot");
                    } else {
                        p.sendMessage("§4Ce plot ne vous appartien pas");
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        if (args[0].equalsIgnoreCase("delete") && sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                PlotFile plots = Plugin.plotfile;
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                if (profile.getPlotnombre() != 0) {
                    UserPlot userplot = FileUtils.getPlayerPlot(p, profile);
                    if (userplot != null) {
                        List<Plot> finalplot = plugin.plotfile.getPlot();
                        Plot plot = FileUtils.getPlot(p);
                        finalplot.remove(plot);
                        plot.setTake(false);
                        plot.setPlayer("");
                        finalplot.add(plot);
                        plots.setPlot(finalplot);
                        FileUtils.saveFile(new File(saveDir, "Plot.json"), plots);
                        Plugin.plotfile = plots;
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), -63,
                                (userplot.getCoZ() * 16 + 31), (userplot.getCoX() * 16 - 16), 0,
                                (userplot.getCoZ() * 16 - 16), Material.QUARTZ_BLOCK,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), 1,
                                (userplot.getCoZ() * 16 + 31), (userplot.getCoX() * 16 - 16), 255,
                                (userplot.getCoZ() * 16 - 16), Material.AIR,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), 0,
                                (userplot.getCoZ() * 16 + 32), (userplot.getCoX() * 16 + 32), 0,
                                (userplot.getCoZ() * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), 0,
                                (userplot.getCoZ() * 16 - 17), (userplot.getCoX() * 16 - 17), 0,
                                (userplot.getCoZ() * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 + 32), 0,
                                (userplot.getCoZ() * 16 + 32), (userplot.getCoX() * 16 + -17), 0,
                                (userplot.getCoZ() * 16 + 32), Material.LIME_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((userplot.getCoX() * 16 - 17), 0,
                                (userplot.getCoZ() * 16 + 32), (userplot.getCoX() * 16 + -17), 0,
                                (userplot.getCoZ() * 16 - 17), Material.LIME_WOOL,
                                p.getWorld());
                        profile.getPlot().remove(userplot);
                        profile.setPlotnombre(profile.getPlotnombre() - 1);
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
                                plot.getChunk().getX() * 16, 1,
                                plot.getChunk().getZ() * 16));
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
                if (profile.getPlotnombre() != 2 || p.isOp()) {
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
                        UserPlot userplot = FileUtils.getPlayerPlot(p, profile);
                        FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + 32), 0,
                                (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                (plot.getChunk().getZ() * 16 - 17), (plot.getChunk().getX() * 16 - 17), 0,
                                (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + -17), 0,
                                (plot.getChunk().getZ() * 16 + 32), Material.RED_WOOL,
                                p.getWorld());
                        FileUtils.fillBlock((plot.getChunk().getX() * 16 - 17), 0,
                                (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + -17), 0,
                                (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                p.getWorld());
                        profile.getPlot().remove(userplot);
                        profile.setPlotnombre(profile.getPlotnombre() - 1);
                        FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                        p.teleport(
                                new Location(Bukkit.getServer().getWorld("flatroom"), plot.getChunk().getX() * 16 + 8, 0,
                                        plot.getChunk().getZ() * 16 + 8));
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
                if (profile.getPlotnombre() != 0) {
                    int n = 0;
                    p.sendMessage("§aTéléportation en cours...");
                    try {
                        int number = Integer.parseInt(args[1]) - 1;
                        if (number > 0 && number < profile.getPlotnombre()) {
                            n = number;
                        }
                    } catch (Exception e) {
                        n = 0;
                    }
                    try {
                        p.teleport(
                                new Location(
                                        Bukkit.getServer().getWorld(this.plugin.getConfig().getString("plot.world")),
                                        profile.getPlot().get(n).getCoX() * 16, 1,
                                        profile.getPlot().get(n).getCoZ() * 16));
                        p.sendMessage("§aTéléportation réussite !");
                    } catch (Exception e) {
                        p.sendMessage("§4La téléportation a échouée : " + e.getMessage());
                    }
                } else {
                    p.sendMessage("§4Vous n'avez aucun plot");
                }
            }
        }

        if (args[0].equalsIgnoreCase("claim") && sender instanceof Player) {
            Player p = (Player) sender;
            if (Objects.equals(this.plugin.getConfig().getString("plot.world"), p.getWorld().getName())) {
                PlotFile plots = (PlotFile) FileUtils.openFile(new File(saveDir, "Plot.json"), PlotFile.class);
                Plot plot;
                profile = FileUtils.openProfileFile(new File(saveDir2, p.getName() + ".json"), p);
                if (profile.getPlotnombre() != 2 || p.isOp()) { 
                    try {
                        plot = FileUtils.getPlot(p);
                        if (plot != null) {
                            plot.setTake(true);
                            plot.setPlayer(p.getName());
                            plugin.plotfile.getPlot().add(plot);
                            FileUtils.saveFile(new File(saveDir, "Plot.json"), plugin.plotfile);
                            Plugin.plotfile = plots;
                            UserPlot userplot = new UserPlot(plot.getChunk().getX(), plot.getChunk().getZ(), 1, null);
                            FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                    (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + 32), 0,
                                    (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                    (plot.getChunk().getZ() * 16 - 17), (plot.getChunk().getX() * 16 - 17), 0,
                                    (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getChunk().getX() * 16 + 32), 0,
                                    (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + -17), 0,
                                    (plot.getChunk().getZ() * 16 + 32), Material.RED_WOOL,
                                    p.getWorld());
                            FileUtils.fillBlock((plot.getChunk().getX() * 16 - 17), 0,
                                    (plot.getChunk().getZ() * 16 + 32), (plot.getChunk().getX() * 16 + -17), 0,
                                    (plot.getChunk().getZ() * 16 - 17), Material.RED_WOOL,
                                    p.getWorld());
                            profile.getPlot().add(userplot);
                            profile.setPlotnombre(profile.getPlotnombre() + 1);
                            FileUtils.saveFile(new File(saveDir2, p.getName() + ".json"), profile);
                            p.sendMessage("§aVous avez pris le plot");
                        } else {
                                p.sendMessage("§4Le plot est déjà pris/n'existe pas");
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
