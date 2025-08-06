package com.xiakk.fpresearchpatcher.command;

import com.mojang.brigadier.CommandDispatcher;
import com.xiakk.fpresearchpatcher.FPResearchPatcher;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FPResearchPatcher.MOD_ID)
public class ReloadCommand {
    
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        
        dispatcher.register(
            Commands.literal("fpreload")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    try {
                        futurepack.common.research.ResearchLoader.instance.init();
                        
                        context.getSource().sendSuccess(
                            new TextComponent("§aFuturePack research reloaded successfully!"),
                            true
                        );
                        return 1;
                    } catch (Exception e) {
                        context.getSource().sendFailure(
                            new TextComponent("§cFailed to reload research: " + e.getMessage())
                        );
                        return 0;
                    }
                })
        );
    }
}
