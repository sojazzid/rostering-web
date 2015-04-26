package com.rostering.web.pages.preferences.fulltime;

import cl.upla.memoria.sam.model.Driver;
import cl.upla.memoria.sam.model.Shift;
import com.rostering.common.FormFilter;

import java.util.List;

public class FullPreferencesFormFilter extends FormFilter {

    private List<Driver> maquinistaDataView;
    private List<Driver> maquinistaGroup;
    private String nameToSaveField;
    private Driver driverField;
    private Shift shiftField;


    public Shift getShiftField() {
        return shiftField;
    }

    public void setShiftField(Shift shiftField) {
        this.shiftField = shiftField;
    }

    public Driver getDriverField() {
        return driverField;
    }

    public void setDriverField(Driver driverField) {
        this.driverField = driverField;
    }

    public String getNameToSaveField() {
        return nameToSaveField;
    }

    public void setNameToSaveField(String nameToSaveField) {
        this.nameToSaveField = nameToSaveField;
    }


    public List<Driver> getMaquinistaGroup() {
        return maquinistaGroup;
    }

    public void setMaquinistaGroup(List<Driver> maquinistaGroup) {
        this.maquinistaGroup = maquinistaGroup;
    }

    public List<Driver> getMaquinistaDataView() {
        return maquinistaDataView;
    }

    public void setMaquinistaDataView(List<Driver> maquinistaDataView) {
        this.maquinistaDataView = maquinistaDataView;
    }
}
