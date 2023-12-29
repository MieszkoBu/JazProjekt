package org.example.Model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private Boolean isAnimationStudio;
    private String siteUrl;
    public String getSiteUrl() {
        return siteUrl;
    }

    public void setSiteUrl(String siteUrl) {
        this.siteUrl = siteUrl;
    }

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
    public Boolean getAnimationStudio() {
        return isAnimationStudio;
    }

    public void setAnimationStudio(Boolean animationStudio) {
        isAnimationStudio = animationStudio;
    }
}
