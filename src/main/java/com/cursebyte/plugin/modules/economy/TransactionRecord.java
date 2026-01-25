package com.cursebyte.plugin.modules.economy;

public class TransactionRecord {

    private final String sender;
    private final String receiver;
    private final double amount;
    private final long time;
    private final String description;

    public TransactionRecord(String sender, String receiver, double amount, long time, String description) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.time = time;
        this.description = description;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public double getAmount() {
        return amount;
    }

    public long getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }
}