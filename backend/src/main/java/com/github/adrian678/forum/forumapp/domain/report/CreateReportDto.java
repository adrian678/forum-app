package com.github.adrian678.forum.forumapp.domain.report;

public class CreateReportDto {

    String type;

    String reportCategory;

    String description;

    public CreateReportDto(String type, String reportCategory, String description){
        this.type = type;
        this.reportCategory = reportCategory;
        this.description = description;
    }

    public String getType(){
        return type;
    }

    public String getReportCategory(){
        return reportCategory;
    }

    public String getDescription(){
        return description;
    }

}
