package com.cursebyte.plugin.modules.government.stock;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.cursebyte.plugin.CursebyteCore;
import com.cursebyte.plugin.modules.economy.EconomyService;
import com.cursebyte.plugin.modules.economy.transaction.TransactionService;
import com.cursebyte.plugin.modules.government.api.GovernmentSupplyApi;
import com.cursebyte.plugin.modules.government.pricing.RobustEmaModel;
import com.cursebyte.plugin.modules.government.util.ItemStackSerializer;
import com.cursebyte.plugin.modules.government.util.SkuHasher;

public final class GovernmentStockService implements GovernmentSupplyApi {

    private static final RobustEmaModel EMA = new RobustEmaModel(0.08, 0.10, 3.0);

    private static final double DEFAULT_SUBSIDY_RATE = 0.15; // 15% subsidi
    private static final double DEFAULT_OVERHEAD = 0.05; // 5% biaya operasional

    private GovernmentStockService() {
    }

    private static final GovernmentStockService INSTANCE = new GovernmentStockService();

    public enum GovernmentStockCategory {
        PANGAN("pangan"),
        MAKANAN("makanan"),
        MINUMAN("minuman"),
        MATERIAL("material"),
        PERTANIAN("pertanian"),
        TAMBANG("tambang"),
        PERALATAN("peralatan"),
        LAINNYA("lainnya");

        private final String id;

        GovernmentStockCategory(String id) {
            this.id = id;
        }

        public String id() {
            return id;
        }
    }

    public static GovernmentStockService get() {
        return INSTANCE;
    }

    public static void init() {
        GovernmentStockRepository.init();
    }

    public static void registerApi(CursebyteCore plugin) {
        Bukkit.getServicesManager().register(GovernmentSupplyApi.class, INSTANCE, plugin,
                org.bukkit.plugin.ServicePriority.Normal);
    }

    @Override
    public void submitStock(UUID sellerUuid, String category, ItemStack item, int quantity, double unitCost) {
        if (item == null || quantity <= 0)
            return;
        if (unitCost < 0)
            unitCost = 0;

        String cat = normalizeCategory(category);
        String sku = SkuHasher.skuHash(item);
        String b64 = ItemStackSerializer.toBase64(item.asOne());

        var row = GovernmentStockRepository.getRow(sku);
        long now = System.currentTimeMillis();

        if (row == null) {
            GovernmentStockRepository.upsertRow(new GovernmentStockRepository.StockRow(
                    sku,
                    cat,
                    b64,
                    quantity,
                    unitCost,
                    unitCost,
                    1e-9,
                    1,
                    now));
            return;
        }

        // robust EMA update
        double ema = row.emaUnitCost;
        double dev = row.emaAbsDev;
        var updated = EMA.update(ema, dev, unitCost);

        int newQty = row.stockQty + quantity;
        int newCount = row.sampleCount + 1;

        GovernmentStockRepository.upsertRow(new GovernmentStockRepository.StockRow(
                row.skuHash,
                cat,
                b64,
                newQty,
                unitCost,
                updated.ema,
                updated.dev,
                newCount,
                now));
    }

    public List<GovernmentStockRepository.CategorySummary> listCategories() {
        return GovernmentStockRepository.listCategories();
    }

    public List<GovernmentStockItem> listItems(String category, int page, int pageSize) {
        int safePage = Math.max(0, page);
        int offset = safePage * pageSize;
        return GovernmentStockRepository.listByCategory(normalizeCategory(category), offset, pageSize);
    }

    public double getUnitSellPrice(String category, double emaUnitCost) {
        double base = Math.max(0, emaUnitCost);
        double price = base * (1.0 + DEFAULT_OVERHEAD) * (1.0 - DEFAULT_SUBSIDY_RATE);
        return Math.round(price * 100.0) / 100.0;
    }

    public PurchaseResult purchase(Player buyer, String skuHash, int qty, double unitPrice) {
        if (buyer == null)
            return PurchaseResult.FAIL;
        if (qty <= 0)
            return PurchaseResult.FAIL;

        UUID buyerId = buyer.getUniqueId();
        double total = unitPrice * qty;

        if (!EconomyService.hasEnough(buyerId, total)) {
            return PurchaseResult.NO_MONEY;
        }

        boolean stockOk = GovernmentStockRepository.decrementStockIfEnough(skuHash, qty);
        if (!stockOk) {
            return PurchaseResult.NO_STOCK;
        }

        boolean charged = EconomyService.remove(buyerId, total);
        if (!charged) {
            GovernmentStockRepository.incrementStock(skuHash, qty);
            return PurchaseResult.NO_MONEY;
        }

        var row = GovernmentStockRepository.getRow(skuHash);
        if (row == null) {
            EconomyService.add(buyerId, total);
            GovernmentStockRepository.incrementStock(skuHash, qty);
            return PurchaseResult.FAIL;
        }

        ItemStack sample = ItemStackSerializer.fromBase64(row.itemB64);
        giveItemStacks(buyer, sample, qty);

        TransactionService.log(buyerId, buyerId, total, "GOV SHOP BUY: ");

        return PurchaseResult.OK;
    }

    private static void giveItemStacks(Player p, ItemStack sample, int qty) {
        if (sample == null)
            return;
        int max = sample.getMaxStackSize();
        int remaining = qty;
        while (remaining > 0) {
            int give = Math.min(max, remaining);
            ItemStack stack = sample.clone();
            stack.setAmount(give);
            p.getInventory().addItem(stack);
            remaining -= give;
        }
    }

    private static String normalizeCategory(String category) {
        if (category == null)
            return "lainnya";
        String c = category.trim();
        if (c.isEmpty())
            return "lainnya";
        return c.toLowerCase(Locale.ROOT);
    }

    public enum PurchaseResult {
        OK,
        NO_MONEY,
        NO_STOCK,
        FAIL
    }
}
