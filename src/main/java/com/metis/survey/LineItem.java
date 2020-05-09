package com.metis.survey;

import java.math.BigDecimal;

// 指标填报，对应 excel 中的每一行

public class LineItem {
    private String type;
    private String code;
    private String name;
    private BigDecimal value;
    private String unit;
    private String definition;
    private String comment;

    public LineItem() {
        this.value = BigDecimal.ZERO;
        this.comment = "";

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
