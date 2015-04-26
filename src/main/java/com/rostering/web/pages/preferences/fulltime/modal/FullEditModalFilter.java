package com.rostering.web.pages.preferences.fulltime.modal;

import com.rostering.common.FormFilter;

public class FullEditModalFilter extends FormFilter {

    private String driverField;
    private String shiftField;
    private Integer preferenceField;

    public Integer getPreferenceField() {
        return preferenceField;
    }

    public void setPreferenceField(Integer preferenceField) {
        this.preferenceField = preferenceField;
    }

    public String getDriverField() {
        return driverField;
    }

    public void setDriverField(String driverField) {
        this.driverField = driverField;
    }

    public String getShiftField() {
        return shiftField;
    }

    public void setShiftField(String shiftField) {
        this.shiftField = shiftField;
    }
}
