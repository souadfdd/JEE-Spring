package com.example.spring_rest_app.core.web;


//Classe utilisée pour présenter une exeception
//les objets de cette classe seront sérialisés en JSON par Spring
public class PersonError {
    private int codeStatut;
    private String description;
    public PersonError(){}

    public PersonError(int codeStatut, String description) {
        this.codeStatut = codeStatut;
        this.description = description;
    }

    public int getCodeStatut() {
        return codeStatut;
    }

    public void setCodeStatut(int codeStatut) {
        this.codeStatut = codeStatut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
