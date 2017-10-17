package com.example.stefano.lomux_pro.model;

import java.util.List;

/**
 * Created by Stefano on 16/10/2017.
 */

public class Pintype {

    public static final int     VENUE   = 1;
    public static final int     STUDIO  = 2;
    public static final int     WORK    = 3;
    public static final int     PRIVATE = 4;
    public static final int     MONUMENT= 5;

    private int  idPinType;
    private String type;
    private List<Pin> pinDTOList;


    public int getIdPinType() {
        return idPinType;
    }

    public void setIdPinType(int idPinType) {
        this.idPinType = idPinType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Pin> getPinDTOList() {
        return pinDTOList;
    }

    public void setPinDTOList(List<Pin> pinDTOList) {
        this.pinDTOList = pinDTOList;
    }
}
