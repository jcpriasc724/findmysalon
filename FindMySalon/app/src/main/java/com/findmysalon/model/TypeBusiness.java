package com.findmysalon.model;

public class TypeBusiness {

    private String typeBusinessID;
    private String typeBusiness;
    private boolean selected;

    public TypeBusiness(String typeBusinessID, boolean selected) {
        this.typeBusinessID = typeBusinessID;
        this.selected = selected;
    }

    public String getTypeBusinessID() {
        return typeBusinessID;
    }

    public void setTypeBusinessID(String typeBusinessID) {
        this.typeBusinessID = typeBusinessID;
    }

    public String getTypeBusiness() {
        return typeBusiness;
    }

    public void setTypeBusiness(String typeBusiness) {
        this.typeBusiness = typeBusiness;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
