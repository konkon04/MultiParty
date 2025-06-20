package com.skyfox.multiParty.hud;

import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Team;

public class PartyHudOverlay {

    public static void render(PoseStack matrixStack) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || player.getTeam() == null) return;

        Team team = player.getTeam();
        List<String> members = (List<String>) team.getPlayers();

        int x = 10;
        int y = 10;
        Component drawText = Component.literal("PT: " + team.getName());
        mc.font.draw(matrixStack, drawText, x, y, 0xFFFFFF);
        
        y += 10;

        for (String name : members) {
            Player mate = mc.level.getPlayerByName(name);
            if (mate != null) {
                String display = name + ": " + (int) mate.getHealth() + "/" + (int) mate.getMaxHealth();
                mc.font.draw(matrixStack, display, x, y, 0x00FF00);
                y += 10;
            }
        }
    }
}
