package hu.progmatic.spotilive.tag;

import lombok.Data;
import lombok.Value;

import java.util.List;

public enum TagKategoria {

    MUFAJ("Műfaj"),
    TEMPO("Tempó"),
    HANGULAT("Hangulat");

     private final String tagKategoriaString;

    TagKategoria(String tagKategoriaNev){
        tagKategoriaString = tagKategoriaNev;
    }

    public String getTagKategoriaString(){
        return tagKategoriaString;
    }

}
