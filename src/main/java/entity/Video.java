package entity;

import javax.persistence.*;
import utils.VidSrcUtil;

@Entity
@Table(name = "Videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Poster")
    private String poster;

    @Column(name = "Description")
    private String description;

    @Column(name = "Views")
    private Integer views = 0;

    @Column(name = "Active")
    private Boolean active = true;

    // Các cột mới thêm từ OMDb
    @Column(name = "ImdbId", unique = true)
    private String imdbId;

    @Column(name = "Year")
    private Integer year;

    @Column(name = "Director")
    private String director;

    @Column(name = "Actors")
    private String actors;

    @Column(name = "Genre")
    private String genre;

    @Column(name = "ImdbRating")
    private Double imdbRating;

    public Video() {
    }

    // Getters & Setters (giữ lại hết, trừ videoUrl)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getViews() {
        return views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Double getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Double imdbRating) {
        this.imdbRating = imdbRating;
    }

    // Thêm phương thức lấy embed URL động
    @Transient
    public String getEmbedUrl() {
        return VidSrcUtil.getEmbedUrl(this.imdbId);
    }
}