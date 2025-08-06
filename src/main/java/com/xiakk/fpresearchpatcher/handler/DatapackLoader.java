package com.xiakk.fpresearchpatcher.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.xiakk.fpresearchpatcher.FPResearchPatcher;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = FPResearchPatcher.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DatapackLoader {
    
    private static final Map<String, JsonArray> datapackOverrides = new HashMap<>();
    
    public static Map<String, JsonArray> getDatapackOverrides() {
        return datapackOverrides;
    }
    
    public static JsonArray getOverridesForPage(String pageName) {
        FPResearchPatcher.LOGGER.info("Checking for overrides for page: {}", pageName);
        JsonArray result = datapackOverrides.get(pageName);
        if (result != null) {
            FPResearchPatcher.LOGGER.info("Found {} overrides for page {}", result.size(), pageName);
        }
        return result;
    }
    
    public static void clearOverrides() {
        datapackOverrides.clear();
    }
    
    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        FPResearchPatcher.LOGGER.info("Registering datapack reload listener");
        event.addListener(new ResearchDatapackReloadListener());
    }
    
    public static class ResearchDatapackReloadListener extends SimpleJsonResourceReloadListener {
        private static final Gson GSON = new Gson();
        
        public ResearchDatapackReloadListener() {
            super(GSON, "futurepack_research");
            FPResearchPatcher.LOGGER.info("ResearchDatapackReloadListener created");
        }
        
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager manager, 
                           ProfilerFiller profiler) {
            FPResearchPatcher.LOGGER.info("=== Starting datapack reload ===");
            FPResearchPatcher.LOGGER.info("Found {} resource files", resources.size());
            
            datapackOverrides.clear();
            
            for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
                ResourceLocation location = entry.getKey();
                JsonElement element = entry.getValue();
                
                String path = location.getPath();
                String pageName = path.replace(".json", "");
                
                FPResearchPatcher.LOGGER.info("Loading research overrides from: {} -> page: {}", location, pageName);
                
                if (element.isJsonArray()) {
                    JsonArray array = element.getAsJsonArray();
                    datapackOverrides.put(pageName, array);
                    FPResearchPatcher.LOGGER.info("Loaded {} researches for page {}", array.size(), pageName);
                } else if (element.isJsonObject()) {
                    JsonArray array = new JsonArray();
                    array.add(element);
                    datapackOverrides.put(pageName, array);
                    FPResearchPatcher.LOGGER.info("Loaded 1 research for page {}", pageName);
                }
            }
            
            FPResearchPatcher.LOGGER.info("=== Datapack reload complete: {} pages loaded ===", datapackOverrides.size());
            
            // 不要在这里自动重载研究系统
            // tryReloadResearch();
        }
    }
}