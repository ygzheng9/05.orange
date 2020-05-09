package com.metis.survey;

// html 中 select 的 option
public class SelectOption {
    private String value;
    private String display;

    public SelectOption() {
    }

    public SelectOption(String value, String display) {
        this.value = value;
        this.display = display;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }
}
