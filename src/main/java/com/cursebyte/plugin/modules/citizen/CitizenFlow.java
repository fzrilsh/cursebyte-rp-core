package com.cursebyte.plugin.modules.citizen;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class CitizenFlow {

    public static void completeRegistration(Player player, String realName, Location spawn) {

        CitizenService.register(player.getUniqueId(), realName);

        player.teleport(spawn);
        player.setGameMode(GameMode.SURVIVAL);

        CitizenHandbookService.give(player);

        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);

        player.showTitle(Title.title(
                Component.text("RESMI LEGAL!").color(TextColor.color(0, 255, 180)),
                Component.text("Selamat menikmati kota!"),
                Title.Times.times(
                        Duration.ofMillis(500),
                        Duration.ofMillis(3000),
                        Duration.ofMillis(500)
                )
        ));

        CitizenSessionManager.end(player);
    }
}