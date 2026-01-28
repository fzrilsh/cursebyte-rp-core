package com.cursebyte.plugin.modules.government.stock;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.cursebyte.plugin.database.DatabaseManager;

public final class GovernmentStockRepository {

    private GovernmentStockRepository() {
    }

    public static void init() {
        try (Statement stmt = DatabaseManager.getConnection().createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS gov_stock (" +
                            "sku_hash TEXT PRIMARY KEY," +
                            "category TEXT NOT NULL," +
                            "item_b64 TEXT NOT NULL," +
                            "stock_qty INTEGER NOT NULL," +
                            "last_unit_cost REAL NOT NULL," +
                            "ema_unit_cost REAL NOT NULL," +
                            "ema_abs_dev REAL NOT NULL," +
                            "sample_count INTEGER NOT NULL," +
                            "updated_at INTEGER NOT NULL" +
                            ")");

            stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_gov_stock_category ON gov_stock(category)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static StockRow getRow(String skuHash) {
        String sql = "SELECT sku_hash, category, item_b64, stock_qty, last_unit_cost, ema_unit_cost, ema_abs_dev, sample_count, updated_at FROM gov_stock WHERE sku_hash=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, skuHash);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return null;
                return new StockRow(
                        rs.getString("sku_hash"),
                        rs.getString("category"),
                        rs.getString("item_b64"),
                        rs.getInt("stock_qty"),
                        rs.getDouble("last_unit_cost"),
                        rs.getDouble("ema_unit_cost"),
                        rs.getDouble("ema_abs_dev"),
                        rs.getInt("sample_count"),
                        rs.getLong("updated_at"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void upsertRow(StockRow row) {
        String sql = "INSERT INTO gov_stock (sku_hash, category, item_b64, stock_qty, last_unit_cost, ema_unit_cost, ema_abs_dev, sample_count, updated_at) "
                +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                "ON CONFLICT(sku_hash) DO UPDATE SET " +
                "category=excluded.category, " +
                "item_b64=excluded.item_b64, " +
                "stock_qty=excluded.stock_qty, " +
                "last_unit_cost=excluded.last_unit_cost, " +
                "ema_unit_cost=excluded.ema_unit_cost, " +
                "ema_abs_dev=excluded.ema_abs_dev, " +
                "sample_count=excluded.sample_count, " +
                "updated_at=excluded.updated_at";

        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, row.skuHash);
            ps.setString(2, row.category);
            ps.setString(3, row.itemB64);
            ps.setInt(4, row.stockQty);
            ps.setDouble(5, row.lastUnitCost);
            ps.setDouble(6, row.emaUnitCost);
            ps.setDouble(7, row.emaAbsDev);
            ps.setInt(8, row.sampleCount);
            ps.setLong(9, row.updatedAt);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean decrementStockIfEnough(String skuHash, int qty) {
        String sql = "UPDATE gov_stock SET stock_qty = stock_qty - ?, updated_at=? WHERE sku_hash=? AND stock_qty >= ?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, System.currentTimeMillis());
            ps.setString(3, skuHash);
            ps.setInt(4, qty);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void incrementStock(String skuHash, int qty) {
        String sql = "UPDATE gov_stock SET stock_qty = stock_qty + ?, updated_at=? WHERE sku_hash=?";
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, System.currentTimeMillis());
            ps.setString(3, skuHash);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<CategorySummary> listCategories() {
        String sql = "SELECT category, SUM(stock_qty) AS total_qty, COUNT(*) AS total_skus FROM gov_stock WHERE stock_qty > 0 GROUP BY category ORDER BY category";
        List<CategorySummary> out = new ArrayList<>();
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new CategorySummary(
                        rs.getString("category"),
                        rs.getInt("total_qty"),
                        rs.getInt("total_skus")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static List<GovernmentStockItem> listByCategory(String category, int offset, int limit) {
        String sql = "SELECT sku_hash, category, item_b64, stock_qty, ema_unit_cost, last_unit_cost FROM gov_stock WHERE category=? AND stock_qty > 0 ORDER BY sku_hash LIMIT ? OFFSET ?";
        List<GovernmentStockItem> out = new ArrayList<>();
        try (PreparedStatement ps = DatabaseManager.getConnection().prepareStatement(sql)) {
            ps.setString(1, category);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new GovernmentStockItem(
                            rs.getString("sku_hash"),
                            rs.getString("category"),
                            rs.getString("item_b64"),
                            rs.getInt("stock_qty"),
                            rs.getDouble("ema_unit_cost"),
                            rs.getDouble("last_unit_cost")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }

    public static final class StockRow {
        public final String skuHash;
        public final String category;
        public final String itemB64;
        public final int stockQty;
        public final double lastUnitCost;
        public final double emaUnitCost;
        public final double emaAbsDev;
        public final int sampleCount;
        public final long updatedAt;

        public StockRow(String skuHash, String category, String itemB64, int stockQty, double lastUnitCost,
                double emaUnitCost, double emaAbsDev, int sampleCount, long updatedAt) {
            this.skuHash = skuHash;
            this.category = category;
            this.itemB64 = itemB64;
            this.stockQty = stockQty;
            this.lastUnitCost = lastUnitCost;
            this.emaUnitCost = emaUnitCost;
            this.emaAbsDev = emaAbsDev;
            this.sampleCount = sampleCount;
            this.updatedAt = updatedAt;
        }
    }

    public static final class CategorySummary {
        public final String category;
        public final int totalQty;
        public final int totalSkus;

        public CategorySummary(String category, int totalQty, int totalSkus) {
            this.category = category;
            this.totalQty = totalQty;
            this.totalSkus = totalSkus;
        }
    }
}
