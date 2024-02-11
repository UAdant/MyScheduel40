package com.example.myschedule30;

public class ExamItem {
    private String examName;
    private double examGrade;

    public ExamItem(String examName, double examGrade) {
        this.examName = examName;
        this.examGrade = examGrade;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public double getExamGrade() {
        return examGrade;
    }

    public void setExamGrade(double examGrade) {
        this.examGrade = examGrade;
    }
}
