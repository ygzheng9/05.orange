package com.metis.survey;

import lombok.Data;

import java.util.List;


public class Section {
    private String section;
    private List<Question> questions;

    public Section() {
    }

    public Section(String section, List<Question> questions) {
        this.section = section;
        this.questions = questions;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
