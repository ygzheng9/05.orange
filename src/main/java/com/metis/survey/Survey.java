package com.metis.survey;

import cn.hutool.core.util.StrUtil;
import com.google.common.base.Splitter;
import com.metis.common.model.ZTSurvey;

import java.util.List;

public class Survey {
    private String title;
    private List<Section> sections;

    public Survey() {
    }

    public Survey(String title, List<Section> sections) {
        this.title = title;
        this.sections = sections;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public void splitQuestions() {
        String defaultOptions = "1 2 3 4 5";

        Splitter splitter = Splitter.on(" ");

        for(Section s : sections) {
            for(Question q: s.getQuestions()) {
                String o = q.getOptions();
                if (StrUtil.isBlank(o)) {
                    o = defaultOptions;
                }
                List<String> fields = splitter.splitToList(o);
                q.setOptionsList(fields);

                // 默认的选项
                q.setAnswer("-1");
            }
        }
    }

}
