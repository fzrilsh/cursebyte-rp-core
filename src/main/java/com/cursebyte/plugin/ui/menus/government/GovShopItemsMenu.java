package com.cursebyte.plugin.ui.menus.government;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.modules.government.stock.GovernmentStockItem;
import com.cursebyte.plugin.modules.government.stock.GovernmentStockService;
import com.cursebyte.plugin.modules.government.util.ItemStackSerializer;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class GovShopItemsMenu implements Menu {

    private static final int PAGE_SIZE = 36; // 4 rows

    @Override
    public String id() {
        return "gov.shop.items";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        String category = safe(ctx.get("category", String.class));
        Integer pageObj = ctx.get("page", Integer.class);
        int page = pageObj == null ? 0 : Math.max(0, pageObj);

        Inventory inv = Bukkit.createInventory(null, 54,
                Component.text("TOKO PEMERINTAH - " + title(category)));

        ItemStack gray = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack black = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 54; i++)
            inv.setItem(i, gray);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, black);
        for (int i = 45; i < 54; i++)
            inv.setItem(i, black);

        List<GovernmentStockItem> items = GovernmentStockService.get().listItems(category, page, PAGE_SIZE);
        List<String> skus = new ArrayList<>();

        int slot = 9;
        for (GovernmentStockItem it : items) {
            if (slot >= 45)
                break;
            ItemStack display = ItemStackSerializer.fromBase64(it.itemB64).clone();
            display.setAmount(1);

            double unitPrice = GovernmentStockService.get().getUnitSellPrice(category, it.emaUnitCost);

            ItemMeta meta = display.getItemMeta();
            meta.lore(List.of(
                    Component.text("#SKU:" + it.skuHash).color(TextColor.color(0, 0, 0))
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text("Stok: " + it.stockQty).color(TextColor.color(220, 220, 220)),
                    Component.text("Harga/unit: ⛁ " + fmt(unitPrice)).color(TextColor.color(120, 255, 120))
                            .decorate(TextDecoration.BOLD),
                    Component.text(""),
                    Component.text("Klik: beli 1").color(TextColor.color(255, 255, 255)),
                    Component.text("Shift+Klik: beli 16").color(TextColor.color(255, 255, 255))));
            display.setItemMeta(meta);

            inv.setItem(slot, display);
            skus.add(it.skuHash);
            slot++;
            if ((slot + 1) % 9 == 0)
                slot++; // skip last column
        }

        ctx.set("skus", skus);
        ctx.set("page", page);
        ctx.set("category", category);

        inv.setItem(45, backItem());
        inv.setItem(49, pageItem(page));
        inv.setItem(53, nextItem());
        inv.setItem(51, prevItem());

        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        String category = safe(ctx.get("category", String.class));
        Integer pageObj = ctx.get("page", Integer.class);
        int page = pageObj == null ? 0 : Math.max(0, pageObj);

        if (slot == 45) {
            MenuRouter.open(p, "gov.shop");
            return;
        }

        if (slot == 53) {
            MenuContext nctx = new MenuContext();
            nctx.set("category", category);
            nctx.set("page", page + 1);
            MenuRouter.open(p, "gov.shop.items", nctx);
            return;
        }

        if (slot == 51) {
            MenuContext nctx = new MenuContext();
            nctx.set("category", category);
            nctx.set("page", Math.max(0, page - 1));
            MenuRouter.open(p, "gov.shop.items", nctx);
            return;
        }

        if (slot < 0 || slot >= 45)
            return;

        ItemStack clicked = p.getOpenInventory().getTopInventory().getItem(slot);
        if (clicked == null || clicked.getType().isAir())
            return;

        ItemMeta meta = clicked.getItemMeta();
        if (meta == null || meta.lore() == null)
            return;

        String sku = null;
        for (Component line : meta.lore()) {
            String plain = plain(line);
            if (plain.startsWith("#SKU:")) {
                sku = plain.substring("#SKU:".length());
                break;
            }
        }
        if (sku == null)
            return;

        // Determine qty from click type
        int qty = p.isSneaking() ? 16 : 1;

        double unitPrice = parseUnitPrice(meta.lore());
        if (unitPrice <= 0) {
            // fallback: reopen to refresh
            MenuContext nctx = new MenuContext();
            nctx.set("category", category);
            nctx.set("page", page);
            MenuRouter.open(p, "gov.shop.items", nctx);
            return;
        }

        var res = GovernmentStockService.get().purchase(p, sku, qty, unitPrice);
        switch (res) {
            case OK -> p.sendMessage("§aBerhasil membeli! (" + qty + ")");
            case NO_MONEY -> p.sendMessage("§cSaldo tidak cukup.");
            case NO_STOCK -> p.sendMessage("§cStok tidak cukup.");
            default -> p.sendMessage("§cTerjadi kesalahan.");
        }

        // refresh menu
        MenuContext nctx = new MenuContext();
        nctx.set("category", category);
        nctx.set("page", page);
        MenuRouter.open(p, "gov.shop.items", nctx);
    }

    private static double parseUnitPrice(List<Component> lore) {
        if (lore == null)
            return -1;
        for (Component c : lore) {
            String s = plain(c);
            if (s.startsWith("Harga/unit:")) {
                // example: "Harga/unit: ⛁ 12.34"
                int idx = s.lastIndexOf(' ');
                if (idx < 0 || idx == s.length() - 1)
                    return -1;
                try {
                    return Double.parseDouble(s.substring(idx + 1));
                } catch (NumberFormatException ignored) {
                    return -1;
                }
            }
        }
        return -1;
    }

    private static ItemStack backItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                Component.text("← Kembali").color(TextColor.color(255, 200, 120)).decorate(TextDecoration.BOLD));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack nextItem() {
        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                Component.text("Berikutnya →").color(TextColor.color(180, 255, 180)).decorate(TextDecoration.BOLD));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack prevItem() {
        ItemStack item = new ItemStack(Material.RED_DYE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                Component.text("← Sebelumnya").color(TextColor.color(255, 180, 180)).decorate(TextDecoration.BOLD));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack pageItem(int page) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("Halaman: " + (page + 1)).color(TextColor.color(220, 220, 255))
                .decorate(TextDecoration.BOLD));
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack glass(Material material) {
        ItemStack glass = new ItemStack(material);
        ItemMeta meta = glass.getItemMeta();
        meta.displayName(Component.text(" "));
        glass.setItemMeta(meta);
        return glass;
    }

    private static String safe(String s) {
        return s == null ? "lainnya" : s;
    }

    private static String title(String category) {
        if (category == null || category.isBlank())
            return "Lainnya";
        return category.substring(0, 1).toUpperCase() + category.substring(1);
    }

    private static String fmt(double val) {
        return String.format("%.2f", val);
    }

    private static String plain(Component c) {
        return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(c);
    }
}
