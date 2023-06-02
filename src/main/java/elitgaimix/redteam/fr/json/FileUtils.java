package elitgaimix.redteam.fr.json;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import elitgaimix.redteam.fr.json.Plot.Plot;
import elitgaimix.redteam.fr.json.Plot.PlotFile;
import elitgaimix.redteam.fr.json.Plot.PlotFusion;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.Plugin;

public class FileUtils {
    private static Plugin plugin;

    public FileUtils(Plugin pluggin) {
        FileUtils.plugin = pluggin;
    }

    public static void saveFile(File file, Object profile) {

        final String json = profileSerializationManager.serialize(profile);
        FileUtils.save(file, json);

    }

    public static <T> Object openFile(File file, Class<T> clss) {
        if (file.exists()) {
            final String json = FileUtils.loadContent(file);
            return profileSerializationManager.deserialize(json, clss);
        }
        return null;

    }

    public static Profile openProfileFile(File file, Player player) {
        if (file.exists()) {
            final String json = FileUtils.loadContent(file);
            return (Profile) profileSerializationManager.deserialize(json, Profile.class);
        } else {
            Profile profile = Profile.createProfile(player);
            final String json = profileSerializationManager.serialize(profile);
            FileUtils.save(file, json);
            return profile;
        }
    }

    public static void createPlotFile(File file, Plugin pl) {
        int bx = pl.getConfig().getInt("plot.cornerX");
        int bz = pl.getConfig().getInt("plot.cornerZ");
        List<Plot> plot = new ArrayList<>();
        boolean fin = false;
        while (!fin) {
            if (bz == pl.getConfig().getInt("plot.lastcornerZ")
                    && bx != pl.getConfig().getInt("plot.lastcornerX")) {
                if (pl.getConfig().getInt("plot.cornerX") > pl.getConfig().getInt("plot.lastcornerX")) {
                    plot.add(new Plot(bx, bz, false, pl.getConfig().getInt("plot.tail"), "",
                            new ArrayList<PlotFusion>()));
                    bx = bx - (pl.getConfig().getInt("plot.tail") * 2) - pl.getConfig().getInt("plot.bordertail") - 1;

                }
                if (pl.getConfig().getInt("plot.cornerX") < pl.getConfig().getInt("plot.lastcornerX")) {
                    plot.add(new Plot(bx, bz, false, pl.getConfig().getInt("plot.tail"), "", null));
                    bx = bx + (pl.getConfig().getInt("plot.tail") * 2) + pl.getConfig().getInt("plot.bordertail") + 1;
                }
                bz = pl.getConfig().getInt("plot.cornerZ");
            } else {
                if (bx == pl.getConfig().getInt("plot.lastcornerX")
                        && bz == pl.getConfig().getInt("plot.lastcornerZ")) {
                    plot.add(new Plot(bx, bz, false, pl.getConfig().getInt("plot.tail"), "",
                            new ArrayList<PlotFusion>()));
                    fin = true;

                } else {
                    plot.add(new Plot(bx, bz, false, pl.getConfig().getInt("plot.tail"), "",
                            new ArrayList<PlotFusion>()));
                    if (bz > pl.getConfig().getInt("plot.lastcornerZ")) {
                        bz = bz - (pl.getConfig().getInt("plot.tail") * 2) - pl.getConfig().getInt("plot.bordertail")
                                - 1;
                    }
                    if (bz < pl.getConfig().getInt("plot.lastcornerZ")) {
                        bz = bz + (pl.getConfig().getInt("plot.tail") * 2) + pl.getConfig().getInt("plot.bordertail")
                                + 1;
                    }

                }
            }
        }
        try {
            FileUtils.creatFile(file);
            FileUtils.saveFile(file,
                    new PlotFile(plot));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void creatFile(File file) throws IOException {

        if (!file.exists()) {
            file.getParentFile().mkdir();
            if (!file.createNewFile()) {
                plugin.getLogger().warning("[RedTeam]La creation d'un fichier a échoué");
            }
        }

    }

    public static void fillBlock(int x, int y, int z, int fx, int fy, int fz, Material block, World world) {

        int bx = x;
        int by = y;
        int bz = z;
        boolean fin = false;
        while (!fin) {
            if (world.getBlockAt(new Location(world, bx, by, bz)).getType() != block) {
                world.getBlockAt(new Location(world, bx, by, bz)).setType(block);
            }
            if (bz == fz && bx != fx) {

                if (x > fx) {
                    bx--;
                }
                if (x < fx) {
                    bx++;
                }
                bz = z;
            } else {
                if (bx == fx && bz == fz) {
                    if (by != fy) {
                        if (y > fy) {
                            by--;
                        }
                        if (y < fy) {
                            by++;
                        }
                        bx = x;
                    } else {
                        fin = true;
                    }

                } else {
                    if (z != fz) {
                        if (z > fz) {
                            bz--;
                        }
                        if (z < fz) {
                            bz++;
                        }
                    } else {
                        fin = true;
                    }

                }
            }
        }
    }

    public static void save(File file, String text) {

        try (FileWriter wr = new FileWriter(file)) {
            wr.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadContent(File file) {
        if (file.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(file));
                final StringBuilder text = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                reader.close();

                return text.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
