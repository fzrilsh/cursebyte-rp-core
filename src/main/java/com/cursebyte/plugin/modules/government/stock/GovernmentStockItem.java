package com.cursebyte.plugin.modules.government.stock;

public class GovernmentStockItem {
    public final String skuHash;
    public final String category;
    public final String itemB64;
    public final int stockQty;
    public final double emaUnitCost;
    public final double lastUnitCost;

    public GovernmentStockItem(String skuHash, String category, String itemB64, int stockQty, double emaUnitCost, double lastUnitCost) {
        this.skuHash = skuHash;
        this.category = category;
        this.itemB64 = itemB64;
        this.stockQty = stockQty;
        this.emaUnitCost = emaUnitCost;
        this.lastUnitCost = lastUnitCost;
    }
}
