package elitgaimix.redteam.fr.json;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.BlockFace;

import elitgaimix.redteam.fr.json.Plot.Plot;
import elitgaimix.redteam.fr.json.Plot.PlotFile;
import elitgaimix.redteam.fr.json.Plot.PlotFusion;
import elitgaimix.redteam.fr.json.profile.AddPlot;
import elitgaimix.redteam.fr.json.profile.Profile;
import elitgaimix.redteam.fr.json.profile.UserPlot;
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

    //fonction pour récuperer le plot dans lequelle il est et si il l'a claim
    public static UserPlot getPlayerPlot(Player player, Profile profile){
        Chunk PlayerChunk = player.getChunk();
        int coX;
        int coZ;
        for(UserPlot plot : profile.getPlot()){
            coX = plot.getCoX();
                        coZ = plot.getCoZ();
                        if ((coX == PlayerChunk.getX() + 1 || coX == PlayerChunk.getX()
                                || coX == PlayerChunk.getX() - 1)
                                && (coZ == PlayerChunk.getZ() + 1 || coZ == PlayerChunk.getZ()
                                        || coZ == PlayerChunk.getZ() - 1)) {
                            return plot;
                        }
        }
        return null;
    }
    public static UserPlot getPlayerPlotByXZ(Profile profile,Chunk chunk){
        int X = chunk.getX();
        int Z = chunk.getZ();
        int coX;
        int coZ;
        for(UserPlot plot : profile.getPlot()){
            coX = plot.getCoX();
                        coZ = plot.getCoZ();
                        if ((coX == X + 1 || coX == X || coX == X - 1)
                            && (coZ == Z + 1 || coZ == Z || coZ == Z - 1)) {
                            return plot;
                        }
        }
        return null;
    }

    public static AddPlot getPlayerAddPlotByXZ(Profile profile,Chunk chunk){
        int X = chunk.getX();
        int Z = chunk.getZ();
        int coX;
        int coZ;
        for(AddPlot plot : profile.getAddplot()){
            coX = plot.getCoX();
                        coZ = plot.getCoZ();
                        if ((coX == X + 1 || coX == X || coX == X - 1)
                            && (coZ == Z + 1 || coZ == Z || coZ == Z - 1)) {
                            return plot;
                        }
        }
        return null;
    }
    //fonction pour récuperer le plot dans lequelle il est et si il est add sur se plot
    public static AddPlot getPlayerAddPlot(Player player, Profile profile){
        Chunk PlayerChunk = player.getChunk();
        int coX;
        int coZ;
        for(AddPlot addplot : profile.getAddplot()){
            coX = addplot.getCoX();
                        coZ = addplot.getCoX();
                        if ((coX == PlayerChunk.getX() + 1 || coX == PlayerChunk.getX()
                                || coX == PlayerChunk.getX() - 1)
                                && (coZ == PlayerChunk.getZ() + 1 || coZ == PlayerChunk.getZ()
                                        || coZ == PlayerChunk.getZ() - 1)) {
                            if (Bukkit.getPlayer(addplot.getPlayer()) != null
                                    || Boolean.TRUE.equals(addplot.getTrust())) {
                               return addplot;
                            }
                            break;
                        }
        }
        return null;
    }
    //fonction pour récuperer le plot dans lequelle il est
    public static Plot getPlot(Player player){
        Chunk PlayerChunk = player.getChunk();
        int coX;
        int coZ;
        for(Plot plot : plugin.plotfile.getPlot()){
                coX = plot.getX() * 4;
                coZ = plot.getZ() *4;
                if ((coX == PlayerChunk.getX() + 1 || coX == PlayerChunk.getX()
                        || coX == PlayerChunk.getX() - 1)
                        && (coZ == PlayerChunk.getZ() + 1 || coZ == PlayerChunk.getZ()
                                || coZ == PlayerChunk.getZ() - 1)) {
                                    return plot;
                }           
            }
            return null;
        }
        public static Plot getPlotByXZ(Chunk chunk){
        int X = chunk.getX();
        int Z = chunk.getZ();
        int coX;
        int coZ;
        for(Plot plot : plugin.plotfile.getPlot()){
                coX = plot.getX() * 4;
                coZ = plot.getZ() * 4;
                if ((coX ==X + 1 || coX ==X || coX ==X - 1)
                    && (coZ == X + 1 || coZ == X || coZ == X - 1)) {
                    return plot;
                }           
            }
            return null;
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
    public static PlotFusion getPlayerPlotFusion(Player p,Profile profile){
        Plot plot = FileUtils.getPlot(p);
            if(plot != null){
            for(PlotFusion pFusion : plot.getPlotFusion()){
                Chunk chunk = p.getWorld().getChunkAt( pFusion.getCoX()*16, pFusion.getCoZ()*16);
                if(FileUtils.getPlayerPlotByXZ(profile, chunk) != null || FileUtils.getPlayerAddPlotByXZ(profile, chunk) != null){
                    return pFusion;
                }
            }}
            return null;
    }

     public static PlotFusion getPlayerPlotFusionByXZ(Chunk chunk,Profile profile){
        Plot plot = FileUtils.getPlotByXZ(chunk);
            if(plot != null){
            for(PlotFusion pFusion : plot.getPlotFusion()){
                chunk = chunk.getWorld().getChunkAt( pFusion.getCoX()*16, pFusion.getCoZ()*16);
                if(FileUtils.getPlayerPlotByXZ(profile, chunk) != null || FileUtils.getPlayerAddPlotByXZ(profile, chunk) != null){
                    return pFusion;
                }
            }}
            return null;
    }

    //pour les CO: on commence par les CO plus proche de l'autre plot et ensuite les plus proche du plot du joueur
    public static void FillPlotBlock(int X,int Z, int OX,int OZ,int Ymax,BlockFace bf,Material Mmat,Material Bmat,World world){
        int X1 = 0;
        int X2 = 0;
        int Z1 = 0;
        int Z2 = 0;
        boolean a = true;
                if(bf == BlockFace.EAST){
                    X1 = X;
                    Z1 = Z + 1;
                    X2 = OX;
                    Z2 = OZ - 1;
                    FileUtils.fillBlock(X, -63,
                                Z, OX, Ymax,
                                Z, Bmat,
                                world);

                     FileUtils.fillBlock(OX, -63,
                                OZ, X, Ymax,
                                OZ, Bmat,
                                world);
                }else if(bf == BlockFace.WEST ){
                    X1 = X ;
                    Z1 = Z- 1;
                    X2 = OX;
                    Z2 = OZ + 1;
                    FileUtils.fillBlock(X, -63,
                                Z, OX, Ymax,
                                Z ,Bmat,
                                world);

                     FileUtils.fillBlock(OX, -63,
                                OZ, X, Ymax,
                                OZ, Bmat,
                                world);
                }else if(bf == BlockFace.SOUTH ){
                    X1 = X + 1;
                    Z1 = Z;
                    X2 = OX - 1;
                    Z2 = OZ;
                    FileUtils.fillBlock(X, -63,
                                Z, X, Ymax,
                                OZ, Bmat,
                                world);

                     FileUtils.fillBlock(OX, -63,
                                OZ, OX, Ymax,
                                Z, Bmat,
                                world);
                }else if(bf == BlockFace.NORTH ){
                    X1 = X - 1;
                    Z1 = Z;
                    X2 = OX + 1;
                    Z2 = OZ;
                    FileUtils.fillBlock(X, -63,
                                Z, X, Ymax,
                                OZ, Bmat,
                                world);

                     FileUtils.fillBlock(OX, -63,
                                OZ, OX, Ymax,
                                Z, Bmat,
                                world);
                }else{
                    a = false;
                }
                if(a){
                FileUtils.fillBlock(X1, -63,
                                Z1, X2, Ymax,
                                Z2, Mmat,
                                world);

                     FileUtils.fillBlock(X1, Ymax + 1,
                                Z1, X2, 255,
                                Z2, Material.AIR,
                                world);
                            }
    }


    public static void createPlotFile(File file, Plugin pl) {
        int bx = pl.getConfig().getInt("plot.cornerX");
        int bz = pl.getConfig().getInt("plot.cornerZ"); 
        List<Plot> plot = new ArrayList<>();
        boolean fin = false;
        new WorldCreator(pl.getConfig().getString("plot.world")).createWorld();
        while (!fin) {
            if (bz == pl.getConfig().getInt("plot.lastcornerZ")
                    && bx != pl.getConfig().getInt("plot.lastcornerX")) {
                if (pl.getConfig().getInt("plot.cornerX") > pl.getConfig().getInt("plot.lastcornerX")) {
                    plot.add(new Plot((bx/4), (bz/4),false, pl.getConfig().getInt("plot.tail"), "",
                            new ArrayList<PlotFusion>()));
                    bx = bx - (pl.getConfig().getInt("plot.tail") * 2) - pl.getConfig().getInt("plot.bordertail") - 1;

                }
                if (pl.getConfig().getInt("plot.cornerX") < pl.getConfig().getInt("plot.lastcornerX")) {
                    plot.add(new Plot((bx/4), (bz/4),false, pl.getConfig().getInt("plot.tail"), "", null));
                    bx = bx + (pl.getConfig().getInt("plot.tail") * 2) + pl.getConfig().getInt("plot.bordertail") + 1;
                }
                bz = pl.getConfig().getInt("plot.cornerZ");
            } else {
                if (bx == pl.getConfig().getInt("plot.lastcornerX")
                        && bz == pl.getConfig().getInt("plot.lastcornerZ")) {
                    plot.add(new Plot((bx/4), (bz/4),false, pl.getConfig().getInt("plot.tail"), "",
                            new ArrayList<PlotFusion>()));
                    fin = true;

                } else {
                    plot.add(new Plot((bx/4), (bz/4),false, pl.getConfig().getInt("plot.tail"), "",
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
