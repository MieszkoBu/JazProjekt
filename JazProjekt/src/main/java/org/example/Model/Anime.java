package org.example.Model;

import jakarta.persistence.*;
import org.example.Enum.MediaFormat;
import org.example.Enum.MediaSource;
import org.example.Enum.MediaStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Anime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private MediaFormat format;
    private int episodes;
    private int episodes_duration;
    private MediaStatus status;
    private LocalDate start_date;
    private LocalDate end_date;
    private int average_score;
    private int popularity;
    private int favourites;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "anime_studio",
            joinColumns = @JoinColumn(name = "anime_id"),
            inverseJoinColumns = @JoinColumn(name = "studio_id")
    )
    private List<Studio> studios = new ArrayList<>();
    private MediaSource Source;
    @ElementCollection
    @CollectionTable(name = "anime_genres", joinColumns = @JoinColumn(name = "anime_id"))
    @Column(name = "genre")
    private List<String> genres = new ArrayList<>();
    private String romaji;
    private String english;
    private String original;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public MediaFormat getFormat() {
        return format;
    }

    public void setFormat(MediaFormat format) {
        this.format = format;
    }

    public int getEpisodes() {
        return episodes;
    }

    public void setEpisodes(int episodes) {
        this.episodes = episodes;
    }

    public int getEpisodes_duration() {
        return episodes_duration;
    }

    public void setEpisodes_duration(int episodes_duration) {
        this.episodes_duration = episodes_duration;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(MediaStatus status) {
        this.status = status;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public int getAverage_score() {
        return average_score;
    }

    public void setAverage_score(int average_score) {
        this.average_score = average_score;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getFavourites() {
        return favourites;
    }

    public void setFavourites(int favourites) {
        this.favourites = favourites;
    }

    public List<Studio> getStudios() {
        return studios;
    }

    public void setStudios(List<Studio> studios) {
        this.studios = studios;
    }

    public MediaSource getSource() {
        return Source;
    }

    public void setSource(MediaSource source) {
        Source = source;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getRomaji() {
        return romaji;
    }

    public void setRomaji(String romaji) {
        this.romaji = romaji;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

}
