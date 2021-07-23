package com.luoyingmm.entity;

public class TranslationData {
    private String translation;
    private String result;

    public TranslationData(String translation, String result) {
        this.translation = translation;
        this.result = result;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "TranslationData{" +
                "translation='" + translation + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
