package com.cursebyte.plugin.modules.government.api;

import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public interface GovernmentSupplyApi {
    void submitStock(UUID sellerUuid, String category, ItemStack item, int quantity, double unitCost);
}
