package com.cursebyte.plugin.modules.report;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportSessionManager {

    public enum Step {
        NONE,
        SELECT_TARGET,
        SELECT_CRIME,
        WRITE_DESCRIPTION,
        CONFIRMATION
    }

    public static class ReportSession {
        public UUID reporter;
        public UUID target;
        public String crimeType;
        public String description;
        public Step step;

        public ReportSession(UUID reporter) {
            this.reporter = reporter;
            this.step = Step.SELECT_TARGET;
        }
    }

    private static final Map<UUID, ReportSession> sessions = new HashMap<>();

    public static void start(UUID reporter) {
        sessions.put(reporter, new ReportSession(reporter));
    }

    public static boolean hasSession(UUID reporter) {
        return sessions.containsKey(reporter);
    }

    public static ReportSession get(UUID reporter) {
        return sessions.get(reporter);
    }

    public static void end(UUID reporter) {
        sessions.remove(reporter);
    }

    public static void setTarget(UUID reporter, UUID target) {
        ReportSession s = sessions.get(reporter);
        s.target = target;
        s.step = Step.SELECT_CRIME;
    }

    public static void setCrime(UUID reporter, String crime) {
        ReportSession s = sessions.get(reporter);
        s.crimeType = crime;
        s.step = Step.WRITE_DESCRIPTION;
    }

    public static void setDescription(UUID reporter, String desc) {
        ReportSession s = sessions.get(reporter);
        s.description = desc;
        s.step = Step.CONFIRMATION;
    }

    public static Step getStep(UUID reporter) {
        ReportSession s = sessions.get(reporter);
        return s == null ? Step.NONE : s.step;
    }
}