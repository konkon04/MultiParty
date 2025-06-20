package com.skyfox.multiParty.gui;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PartyScreen extends Screen {

    private EditBox teamNameInput;
    private Button joinButton, leaveButton, listTeamsButton, membersButton;

    public PartyScreen() {
        super(Component.literal("Team Management"));
    }

    @Override
    protected void init() {
        
        int midX = this.width / 2;
        int midY = this.height / 2;

        teamNameInput = new EditBox(this.font, midX - 100, midY - 50, 200, 20, Component.literal("Team Name"));
        addRenderableWidget(teamNameInput);

        joinButton = addRenderableWidget(new Button(midX - 100, midY - 20, 98, 20, Component.literal("Join"), b -> {
            String team = teamNameInput.getValue();
            Minecraft.getInstance().player.sendChatMessage("/team join " + team, null);
        }));

        leaveButton = addRenderableWidget(new Button(midX + 2, midY - 20, 98, 20, Component.literal("Leave"), b -> {
            Minecraft.getInstance().player.sendChatMessage("/team leave", null);
        }));

        listTeamsButton = addRenderableWidget(new Button(midX - 100, midY + 10, 98, 20, Component.literal("List Teams"), b -> {
            Minecraft.getInstance().player.sendChatMessage("/team list", null);
        }));

        membersButton = addRenderableWidget(new Button(midX + 2, midY + 10, 98, 20, Component.literal("My Members"), b -> {
            Minecraft.getInstance().player.sendChatMessage("/team members", null);
        }));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {

        this.renderBackground(poseStack);
        drawCenteredString(poseStack, this.font, "Team Management", this.width / 2, 20, 0xFFFFFF);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {

        return false;
    }
}
