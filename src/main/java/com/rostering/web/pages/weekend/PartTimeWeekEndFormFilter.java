package com.rostering.web.pages.weekend;

import com.rostering.model.Driver;
import com.rostering.common.FormFilter;

import java.util.List;

public class PartTimeWeekEndFormFilter extends FormFilter {

    private List<Driver> maquinistaDataView;
    private List<Driver> maquinistaGroup;
    private String nameToSaveField;

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
