package com.example.individualassignment;

import java.io.Serializable;

public class Bill implements Serializable {
    public String id, month;
    public double unitUsed, rebatePercent, totalCharge, finalCost;

    public Bill() {} // Required by Firebase

    public Bill(String id, String month, double unitUsed, double rebatePercent, double totalCharge, double finalCost) {
        this.id = id;
        this.month = month;
        this.unitUsed = unitUsed;
        this.rebatePercent = rebatePercent;
        this.totalCharge = totalCharge;
        this.finalCost = finalCost;
    }
}
