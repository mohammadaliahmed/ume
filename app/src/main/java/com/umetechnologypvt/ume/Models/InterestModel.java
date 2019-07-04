package com.umetechnologypvt.ume.Models;

public class InterestModel {
    String interest;
    boolean section;

    public InterestModel(String interest, boolean section) {
        this.interest = interest;
        this.section = section;
    }

    public InterestModel(String interest) {
        this.interest = interest;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public boolean isSection() {
        return section;
    }

    public void setSection(boolean section) {
        this.section = section;
    }
}
