package dev.mruniverse.guardianlib.core.utils.world;

import dev.mruniverse.guardianlib.core.GuardianLIB;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.FileUtil;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class WorldController {
    private final GuardianLIB main;

    public WorldController(GuardianLIB main) {
        this.main = main;
    }

    public void importWorld2(String gameName,String worldName, boolean unload) {
        File source = new File(this.main.getDataFolder(), "GameBackup/" + gameName + "/" + worldName);
        if (source.isDirectory()) {
            File target = new File(this.main.getServer().getWorldContainer(), source.getName());
            if (unload)
                unloadWorld(worldName);
            copyDir(source, target);
            loadWorld(worldName);
        }
    }

    public void loadWorld(String arena) {
        if ((new File(this.main.getDataFolder(), arena)).exists()) {
            File f = new File(Bukkit.getWorldContainer(), arena);
            f.mkdirs();
            FileUtil.copy(new File(this.main.getDataFolder(), arena), f);
        }

        WorldCreator wc = new WorldCreator(arena);
        wc.type(WorldType.FLAT);
        wc.generatorSettings("36");
        wc.generateStructures(false);
        Bukkit.createWorld(wc);
        World world = Bukkit.getWorld(arena);
        if(world == null) return;
        WorldBorder wb = world.getWorldBorder();
        wb.reset();
        for (Entity e : world.getEntities()) {
            if (!(e instanceof Player))
                e.remove();
        }
        world.setGameRuleValue("doMobSpawning", "false");
        world.setAutoSave(false);
    }

    public void importWorld(String gameName,String worldName, boolean importMap) {
        if (importMap) {
            importWorld2(gameName,worldName, false);
        } else {
            if (!Bukkit.getWorlds().contains(Bukkit.getWorld(worldName)))
                loadWorld(worldName);
            //World world = Bukkit.getWorld(worldName);
            //chest load & spawn loads
        }
    }

    private void unloadWorld(String worldName) {
        File target = new File(this.main.getServer().getWorldContainer(), worldName);
        World world = Bukkit.getWorld(worldName);
        if (target.isDirectory() &&
                world != null) {
            Bukkit.getServer().unloadWorld(world, false);
            target.delete();
        }
    }

    public void copyDir(File source, File target) {
        if (source.isDirectory()) {
            if (!target.exists())
                target.mkdir();
            String[] files = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(target, file);
                copyDir(srcFile, destFile);
            }
        } else {
            try {
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0)
                    out.write(buffer, 0, length);
                in.close();
                out.close();
            } catch (Throwable throwable) {
                main.getLogs().error(throwable);
            }
        }
    }
}