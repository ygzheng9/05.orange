package com.metis.common.model.mock;

import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private String category;
    private List<String> items;

    public Category() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Category{" +
            "category='" + category + '\'' +
            ", items=" + items +
            ", count=" + items.size() +
            '}';
    }

    public void refreshItems() {
        List<String> results = new ArrayList<>();
        Splitter splitter = Splitter.on(" ");

        for (String line : items) {
            List<String> fields = splitter.splitToList(line);
            results.addAll(fields);
        }

        this.items = results;
    }
}
