package org.example.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Studio {
    @Id
    private int id;
    private String name;
//    @ManyToMany(mappedBy = "studios")
//    private List<Anime> animes = new ArrayList<>();
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
//    public List<Anime> getAnimes() {
//        return animes;
//    }
//
//    public void setAnimes(List<Anime> animes) {
//        this.animes = animes;
//    }
}
