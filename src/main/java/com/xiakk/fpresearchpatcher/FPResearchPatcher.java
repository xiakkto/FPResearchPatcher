package com.xiakk.fpresearchpatcher;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fpresearchpatcher")
public class FPResearchPatcher {
    public static final String MOD_ID = "fpresearchpatcher";
    public static final Logger LOGGER = LogManager.getLogger();

    public FPResearchPatcher() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("FuturePack Research Patcher initialized!");
    }
}