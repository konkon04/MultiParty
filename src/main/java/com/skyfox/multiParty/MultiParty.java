package com.skyfox.multiParty;

import com.skyfox.multiParty.gui.TeamScreen;
import com.skyfox.multiParty.hud.TeamHudOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.server.commands.TeamCommand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@Mod("multiparty")
public class MultiParty {

    public static boolean hudEnabled = true;

    public TeamPvPMod() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // サーバーサイドの初期化
    }

    @OnlyIn(Dist.CLIENT)
    private void doClientStuff(final FMLClientSetupEvent event) {

        // クライアントサイドの初期化
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {

        TeamCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;
        if (!(event.getSource().getEntity() instanceof Player attacker)) return;
        if (victim.getTeam() != null && attacker.getTeam() != null &&
            victim.getTeam().getName().equals(attacker.getTeam().getName())) {
            event.setCanceled(true);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRenderHUD(RenderGameOverlayEvent.Post event) {

        if (hudEnabled && event.getType() == RenderGameOverlayEvent.ElementType.ALL) {

            TeamHudOverlay.render(event.getMatrixStack());
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {

        Minecraft mc = Minecraft.getInstance();
        // 例: TキーでチームGUIを開く
        if (mc.screen == null && event.getKey() == org.lwjgl.glfw.GLFW.GLFW_KEY_T) {
            
            mc.setScreen(new TeamScreen());
        }
    }
}
