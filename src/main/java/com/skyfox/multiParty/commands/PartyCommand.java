package com.skyfox.multiParty.commands;

import com.mojang.brigadier.arguments.StringArgumentType;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

public class PartyCommand {
    
    public static void register(com.mojang.brigadier.CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("team")
            .then(Commands.literal("list")
                .executes(ctx -> {
                    Scoreboard board = ctx.getSource().getServer().getScoreboard();
                    String teams = String.join(", ", board.getTeams().stream().map(Team::getName).toList());
                    ctx.getSource().sendSuccess(new TextComponent("Teams: " + teams), false);
                    return 1;
                })
            )
            .then(Commands.literal("join")
                .then(Commands.argument("teamName", StringArgumentType.word())
                    .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        String teamName = StringArgumentType.getString(ctx, "teamName");
                        Scoreboard board = player.getServer().getScoreboard();
                        Team team = board.getOrCreateTeam(teamName);
                        board.addPlayerToTeam(player.getScoreboardName(), team);
                        ctx.getSource().sendSuccess(new TextComponent("Joined team " + teamName), false);
                        return 1;
                    })
                )
            )
            .then(Commands.literal("leave")
                .executes(ctx -> {
                    ServerPlayer player = ctx.getSource().getPlayerOrException();
                    Scoreboard board = player.getServer().getScoreboard();
                    Team team = player.getTeam();
                    if (team != null) {
                        board.removePlayerFromTeam(player.getScoreboardName(), team);
                        ctx.getSource().sendSuccess(new TextComponent("Left team " + team.getName()), false);
                    } else {
                        ctx.getSource().sendSuccess(new TextComponent("You are not on a team."), false);
                    }
                    return 1;
                })
            )
            .then(Commands.literal("members")
                .executes(ctx -> {
                    Scoreboard board = ctx.getSource().getServer().getScoreboard();
                    Team team = board.getPlayersTeam(ctx.getSource().getPlayerOrException().getScoreboardName());
                    if (team != null) {
                        String members = String.join(", ", team.getPlayers());
                        ctx.getSource().sendSuccess(new TextComponent("Members of " + team.getName() + ": " + members), false);
                    } else {
                        ctx.getSource().sendSuccess(new TextComponent("You are not on a team."), false);
                    }
                    return 1;
                })
            )
        );
    }
}
