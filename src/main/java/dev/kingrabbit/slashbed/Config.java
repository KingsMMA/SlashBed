package dev.kingrabbit.slashbed;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Config {

    public String teleportedMessage = "Teleported to bed spawn location.";
    public String teleportCancelled = "Teleport cancelled because you moved.";
    public String teleportingTitle = "Teleporting to bed, please stand still...";
    public boolean cancelOnMove = true;
    public int delay = 3;

    // region Saving and loading - Heavily inspired by https://github.com/Patbox/get-off-my-lawn-reserved/blob/1.21.4/src/main/java/draylar/goml/config/GOMLConfig.java#L102
    // Credit to Patbox on GitHub, licensed under MIT
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static Config loadOrCreateConfig() {
        try {
            Config config;
            File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "slashbed.json");

            if (configFile.exists()) {
                String json = IOUtils.toString(new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8));

                config = GSON.fromJson(json, Config.class);
            } else {
                config = new Config();
            }

            saveConfig(config);
            return config;
        } catch(IOException exception) {
            SlashBed.LOGGER.error("Something went wrong while reading config!");
            exception.printStackTrace();
            return new Config();
        }
    }

    public static void saveConfig(Config config) {
        File configFile = new File(FabricLoader.getInstance().getConfigDir().toFile(), "slashbed.json");
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8));
            writer.write(GSON.toJson(config));
            writer.close();
        } catch (Exception e) {
            SlashBed.LOGGER.error("Something went wrong while saving config!");
            e.printStackTrace();
        }
    }
    // endregion

}
