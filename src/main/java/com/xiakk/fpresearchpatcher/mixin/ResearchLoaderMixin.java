package com.xiakk.fpresearchpatcher.mixin;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.xiakk.fpresearchpatcher.FPResearchPatcher;
import com.xiakk.fpresearchpatcher.handler.DatapackLoader;

import futurepack.common.research.ResearchLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

@Mixin(value = ResearchLoader.class, remap = false)
public class ResearchLoaderMixin {
    
    @Shadow
    private Gson gson;
    
    @Unique
    private static int currentPageIndex = 0;
    
    @Unique
    private static final String[] PAGE_NAMES = {
        "main", "story", "production", "energy", 
        "logistic", "chips", "deco", "space", "tools"
    };
    
    @Inject(method = "init", at = @At("HEAD"))
    private void resetPageIndex(CallbackInfo ci) {
        currentPageIndex = 0;
        FPResearchPatcher.LOGGER.info("Resetting page index for research loading");
    }
    
    @Redirect(
        method = "addResearchesFromReader",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/gson/Gson;fromJson(Lcom/google/gson/stream/JsonReader;Ljava/lang/reflect/Type;)Ljava/lang/Object;"
        )
    )
    private Object redirectFromJson(Gson gson, JsonReader reader, java.lang.reflect.Type type, String modID, Reader read) {
        // 读取原始数据
        JsonArray original = gson.fromJson(reader, JsonArray.class);
        
        // 获取当前页面名称
        String pageName = getCurrentPageName();
        FPResearchPatcher.LOGGER.info("Processing research page: {} (index: {})", pageName, currentPageIndex);
        
        // 获取覆盖数据
        JsonArray customArray = DatapackLoader.getOverridesForPage(pageName);
        
        if (customArray != null) {
            FPResearchPatcher.LOGGER.info("Applying {} overrides for page: {}", customArray.size(), pageName);
            JsonArray merged = mergeResearchArrays(original, customArray);
            currentPageIndex++;
            return merged;
        }
        
        currentPageIndex++;
        return original;
    }
    
    @Unique
    private String getCurrentPageName() {
        if (currentPageIndex >= 0 && currentPageIndex < PAGE_NAMES.length) {
            return PAGE_NAMES[currentPageIndex];
        }
        return "unknown_" + currentPageIndex;
    }
    
    private JsonArray mergeResearchArrays(JsonArray original, JsonArray custom) {
        JsonArray result = new JsonArray();
        Map<String, JsonObject> researchMap = new HashMap<>();
        
        // 先添加原始研究
        FPResearchPatcher.LOGGER.info("Original array has {} elements", original.size());
        for (JsonElement element : original) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("id")) {
                    String id = obj.get("id").getAsString();
                    researchMap.put(id, obj);
                }
            }
        }
        
        // 覆盖或添加自定义研究
        FPResearchPatcher.LOGGER.info("Custom array has {} elements", custom.size());
        for (JsonElement element : custom) {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                if (obj.has("id")) {
                    String id = obj.get("id").getAsString();
                    researchMap.put(id, obj);
                    FPResearchPatcher.LOGGER.info("Overriding/Adding research: {}", id);
                }
            }
        }
        
        // 转换回JsonArray
        for (JsonObject obj : researchMap.values()) {
            result.add(obj);
        }
        
        FPResearchPatcher.LOGGER.info("Merged array has {} elements", result.size());
        return result;
    }
}