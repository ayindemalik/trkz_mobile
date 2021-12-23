package com.example.trkz_mobile;

public class HandlingUnitModel {
    String qty, handlingUnitTypeID, tanim, kategory, width, depth;

    public HandlingUnitModel(String qty, String handlingUnitTypeID, String tanim, String kategory, String width, String depth) {
        this.qty = qty;
        this.handlingUnitTypeID = handlingUnitTypeID;
        this.tanim = tanim;
        this.kategory = kategory;
        this.width = width;
        this.depth = depth;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getHandlingUnitTypeID() {
        return handlingUnitTypeID;
    }

    public void setHandlingUnitTypeID(String handlingUnitTypeID) {
        this.handlingUnitTypeID = handlingUnitTypeID;
    }

    public String getTanim() {
        return tanim;
    }

    public void setTanim(String tanim) {
        this.tanim = tanim;
    }

    public String getKategory() {
        return kategory;
    }

    public void setKategory(String kategory) {
        this.kategory = kategory;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }
}
