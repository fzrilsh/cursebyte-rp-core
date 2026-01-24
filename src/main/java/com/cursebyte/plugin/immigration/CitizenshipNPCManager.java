package com.cursebyte.plugin.immigration;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import com.cursebyte.plugin.CursebyteCore;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.VillagerProfession;

import org.bukkit.Location;

public class CitizenshipNPCManager {
    public void spawnNPC() {
        Location loc = CursebyteCore.getInstance().getConfig().getLocation("immigration.npc.location");
        if (loc == null) {
            CursebyteCore.getInstance().getLogger()
                    .warning("Lokasi NPC Imigrasi belum diset! Gunakan /setupimmigration npc");
            return;
        }

        String npcName = CursebyteCore.getInstance().getConfig().getString("immigration.npc.name", "Petugas Imigrasi");

        int savedId = CursebyteCore.getInstance().getConfig().getInt("immigration.npc.id", -1);
        NPC existingNPC = null;

        if (savedId >= 0) {
            existingNPC = CitizensAPI.getNPCRegistry().getById(savedId);
        }

        if (existingNPC != null) {
            existingNPC.teleport(loc, org.bukkit.event.player.PlayerTeleportEvent.TeleportCause.PLUGIN);
            existingNPC.setName(npcName);
            CursebyteCore.getInstance().getLogger().info("NPC Imigrasi dimuat ulang (ID: " + savedId + ")");
        } else {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, npcName);
            npc.spawn(loc);

            npc.getOrAddTrait(LookClose.class).lookClose(true);

            VillagerProfession profTrait = npc.getOrAddTrait(VillagerProfession.class);
            profTrait.setProfession(Villager.Profession.LIBRARIAN);

            if (npc.getEntity() instanceof Villager villager) {
                villager.setVillagerType(Villager.Type.PLAINS);
            }

            CursebyteCore.getInstance().getConfig().set("immigration.npc.id", npc.getId());
            CursebyteCore.getInstance().saveConfig();

            CursebyteCore.getInstance().getLogger().info("NPC Imigrasi baru berhasil dibuat (ID: " + npc.getId() + ")");
        }
    }
}
