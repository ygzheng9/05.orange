package com.metis.common.model.mock;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import cn.hutool.core.io.resource.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

public class MockData {
    private String name;
    private List<Category> data;

    public MockData() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Category> getData() {
        return data;
    }

    public void setData(List<Category> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MockData{" +
            "name='" + name + '\'' +
            ", data=" + data +
            '}';
    }

    public void refreshAll() {
        for(Category c : data) {
            c.refreshItems();
        }
    }

    public List<String> getCategory(String cate) {
        // 根据 categoryName 获取对应的 列表值
        for (Category c : data) {
            if (c.getCategory().compareToIgnoreCase(cate) == 0) {
                return c.getItems();
            }
        }

        System.err.println("NO Category: " + cate);
        return new ArrayList<String>();
    }

    public static MockData loadMockData() {
        // 从文件中加载 mock data
        InputStream steam = new ClassPathResource("/local/mock.yaml").getStream();

        Yaml yaml = new Yaml();
        MockData ret = yaml.loadAs(steam, MockData.class);

        ret.refreshAll();

        return ret;
    }
}
