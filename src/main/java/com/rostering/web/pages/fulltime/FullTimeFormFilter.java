package com.rostering.web.pages.fulltime;

import cl.upla.memoria.sam.model.Driver;
import com.rostering.model.Maquinista;
import com.rostering.common.FormFilter;

import java.util.List;

public class FullTimeFormFilter extends FormFilter {

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
