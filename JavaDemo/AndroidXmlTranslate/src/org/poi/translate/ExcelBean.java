package org.poi.translate;

public class ExcelBean {

    private Integer id;

    private String english;
    private String spanish;
    private String Portugal;

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getSpanish() {
        return spanish;
    }

    public void setSpanish(String spanish) {
        this.spanish = spanish;
    }

    public String getPortugal() {
        return Portugal;
    }

    public void setPortugal(String portugal) {
        Portugal = portugal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ExcelBean [id=" + id + ", english=" + english + ", spanish="
                + spanish + ", Portugal=" + Portugal + "]";
    }

}
