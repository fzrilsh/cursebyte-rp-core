package com.cursebyte.plugin.modules.report;

import java.util.UUID;

public class ReportService {

    public static void submit(UUID reporter, UUID target, String crime, String desc){
        ReportManager.create(reporter, target, crime, desc);
    }
}