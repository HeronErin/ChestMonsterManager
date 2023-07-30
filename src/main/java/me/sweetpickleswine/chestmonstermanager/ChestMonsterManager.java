package me.sweetpickleswine.chestmonstermanager;

import fi.dy.masa.malilib.event.InitializationHandler;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChestMonsterManager implements ModInitializer {
    public static final String ModId = "ChestMonsterManager";
    public static final Logger LOGGER = LoggerFactory.getLogger(ModId);

    @Override
    public void onInitialize() {

        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

    }
}
