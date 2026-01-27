package com.cursebyte.plugin.modules.citizen;

import java.util.UUID;

public class CitizenProfile {

    private UUID uuid;
    private String nik;
    private String fullName;
    private long joinDate;
    private boolean registered;

    public CitizenProfile(UUID uuid, String nik, String fullName, long joinDate, boolean registered) {
        this.uuid = uuid;
        this.nik = nik;
        this.fullName = fullName;
        this.joinDate = joinDate;
        this.registered = registered;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getNik() {
        return nik;
    }

    public String getFullName() {
        return fullName;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }
}