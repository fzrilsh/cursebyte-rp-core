package com.cursebyte.plugin.modules.report;

import java.util.UUID;

public class ReportManager {

    public static void create(UUID reporter, UUID target, String crime, String desc){
        ReportRepository.insert(reporter, target, crime, desc);
    }
}