package com.cursebyte.plugin.modules.citizen;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.cursebyte.plugin.modules.reputation.ReputationEvent;
import com.cursebyte.plugin.modules.reputation.ReputationEventBus;
import com.cursebyte.plugin.modules.reputation.ReputationEventType;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;

import java.time.Duration;
import java.util.Map;

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

        ReputationEventBus.emit(new ReputationEvent(
            player.getUniqueId(),
            ReputationEventType.PUBLIC_SERVICE,
            Map.of()
        ));

        CitizenSessionManager.end(player);
    }
}