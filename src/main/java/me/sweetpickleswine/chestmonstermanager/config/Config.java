package me.sweetpickleswine.chestmonstermanager.config;
import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.ConfigUtils;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigHandler;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.util.FileUtils;
import fi.dy.masa.malilib.util.JsonUtils;
import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.ListOptions.SortType;

import java.io.File;


public class Config implements IConfigHandler {

    private static final String CONFIG_FILE_NAME = ChestMonsterManager.ModId + ".json";






    public static final ConfigOptionList SORTING_ACTION = new ConfigOptionList("Sort action", SortType.NAME, "What the button in inventories do");

    public static final ConfigInteger SORT_ACTION_DELAY = new ConfigInteger("Sort action delay", 75, 25, 400, true, "Time in milliseconds between button clicking (lower = faster)");


    public static final ImmutableList<IConfigBase> OPTIONS = ImmutableList.of(
            Hotkeys.OPEN_GUI_MAIN_MENU,
            SORT_ACTION_DELAY,
            SORTING_ACTION
    );

    public static void loadFromFile()
    {

        File configFile = new File(FileUtils.getConfigDirectory(), CONFIG_FILE_NAME);

        if (configFile.exists() && configFile.isFile() && configFile.canRead())
        {
            JsonElement element = JsonUtils.parseJsonFile(configFile);

            if (element != null && element.isJsonObject())
            {
                JsonObject root = element.getAsJsonObject();

                ConfigUtils.readConfigBase(root, "Colors", OPTIONS);

            }
        }



    }

    public static void saveToFile()
    {
        File dir = FileUtils.getConfigDirectory();

        if ((dir.exists() && dir.isDirectory()) || dir.mkdirs())
        {
            JsonObject root = new JsonObject();

            ConfigUtils.writeConfigBase(root, "Colors", OPTIONS);

            JsonUtils.writeJsonToFile(root, new File(dir, CONFIG_FILE_NAME));
        }
    }

    @Override
    public void load()
    {
        loadFromFile();
    }

    @Override
    public void save()
    {
        saveToFile();
    }
}
