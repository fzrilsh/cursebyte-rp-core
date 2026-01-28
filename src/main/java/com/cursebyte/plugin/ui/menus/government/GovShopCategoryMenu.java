package com.cursebyte.plugin.ui.menus.government;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cursebyte.plugin.modules.government.stock.GovernmentStockRepository;
import com.cursebyte.plugin.modules.government.stock.GovernmentStockService;
import com.cursebyte.plugin.ui.core.Menu;
import com.cursebyte.plugin.ui.core.MenuContext;
import com.cursebyte.plugin.ui.core.MenuRouter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class GovShopCategoryMenu implements Menu {

    @Override
    public String id() {
        return "gov.shop";
    }

    @Override
    public Inventory build(Player p, MenuContext ctx) {
        Inventory inv = Bukkit.createInventory(null, 45, Component.text("TOKO PEMERINTAH"));

        ItemStack gray = glass(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack black = glass(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 45; i++)
            inv.setItem(i, gray);
        for (int i = 0; i < 9; i++)
            inv.setItem(i, black);
        for (int i = 36; i < 45; i++)
            inv.setItem(i, black);

        List<GovernmentStockRepository.CategorySummary> cats = GovernmentStockService.get().listCategories();

        int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34 };
        int idx = 0;
        for (GovernmentStockRepository.CategorySummary c : cats) {
            if (idx >= slots.length)
                break;
            inv.setItem(slots[idx++], categoryItem(c.category, c.totalQty, c.totalSkus));
        }

        inv.setItem(40, backItem());
        return inv;
    }

    @Override
    public void onClick(Player p, int slot, MenuContext ctx) {
        if (slot == 40) {
            MenuRouter.open(p, "main");
            return;
        }

        ItemStack clicked = p.getOpenInventory().getTopInventory().getItem(slot);
        if (clicked == null || clicked.getType().isAir())
            return;
        ItemMeta meta = clicked.getItemMeta();
        if (meta == null || meta.displayName() == null)
            return;

        // category stored in persistent? We keep it in lore line 0 prefix "#CAT:"
        List<Component> lore = meta.lore();
        if (lore == null)
            return;
        String cat = null;
        for (Component line : lore) {
            String plain = plain(line);
            if (plain.startsWith("#CAT:")) {
                cat = plain.substring("#CAT:".length());
                break;
            }
        }
        if (cat == null)
            return;

        MenuContext nctx = new MenuContext();
        nctx.set("category", cat);
        nctx.set("page", 0);
        MenuRouter.open(p, "gov.shop.items", nctx);
    }

    private static ItemStack categoryItem(String category, int totalQty, int totalSkus) {
        Material icon = CATEGORY_ICON.getOrDefault(category, Material.CHEST);
        ItemStack item = new ItemStack(icon);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("📦 " + title(category))
                .color(TextColor.color(120, 220, 255))
                .decorate(TextDecoration.BOLD));

        meta.lore(List.of(
                Component.text("#CAT:" + category).color(TextColor.color(0, 0, 0)).decoration(TextDecoration.ITALIC,
                        false),
                Component.text("Total stok: " + totalQty).color(TextColor.color(220, 220, 220)),
                Component.text("Jenis barang: " + totalSkus).color(TextColor.color(220, 220, 220)),
                Component.text(""),
                Component.text("Klik untuk membuka").color(TextColor.color(255, 255, 255))
                        .decorate(TextDecoration.BOLD)));

        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack backItem() {
        ItemStack item = new ItemStack(Material.ARROW);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text("← Kembali")
                .color(TextColor.color(255, 200, 120))
                .decorate(TextDecoration.BOLD));

        meta.lore(List.of(Component.text("Kembali ke menu utama").color(TextColor.color(180, 180, 180))));

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

    private static String title(String category) {
        if (category == null || category.isBlank())
            return "Lainnya";
        return category.substring(0, 1).toUpperCase() + category.substring(1);
    }

    private static String plain(Component c) {
        return net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(c);
    }

    private static final Map<String, Material> CATEGORY_ICON = new HashMap<>();
    static {
        CATEGORY_ICON.put("pangan", Material.BREAD);
        CATEGORY_ICON.put("makanan", Material.COOKED_BEEF);
        CATEGORY_ICON.put("minuman", Material.POTION);
        CATEGORY_ICON.put("bahan", Material.IRON_INGOT);
        CATEGORY_ICON.put("material", Material.OAK_PLANKS);
        CATEGORY_ICON.put("pertanian", Material.WHEAT);
        CATEGORY_ICON.put("tambang", Material.DIAMOND_PICKAXE);
        CATEGORY_ICON.put("peralatan", Material.IRON_AXE);
        CATEGORY_ICON.put("lainnya", Material.CHEST);
    }
}
