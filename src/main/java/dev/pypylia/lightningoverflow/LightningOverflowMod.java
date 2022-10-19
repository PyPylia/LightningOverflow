package dev.pypylia.lightningoverflow;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class LightningOverflowMod implements ClientModInitializer {

    public static Logger LOGGER = LogManager.getLogger();
    public static Config CONFIG = Config.load();

    public static final String MOD_ID = "lightningoverflow";
    public static final String MOD_NAME = "LightningOverflow";

    public static UUID lastId;

    @Override
    public void onInitializeClient() {

    }
}