package entity;

import javax.persistence.*;
@Entity
@Table(name = "Episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SeriesId", nullable = false)
    private Series series;

    @Column(name = "SeasonNumber")
    private Integer seasonNumber;

    @Column(name = "EpisodeNumber")
    private Integer episodeNumber;

    @Column(name = "Title", columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(name = "VideoUrl", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String videoUrl;

    @Column(name = "SubtitleUrl", columnDefinition = "NVARCHAR(MAX)")
    private String subtitleUrl;

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public void setSeasonNumber(Integer seasonNumber) {
        this.seasonNumber = seasonNumber;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSubtitleUrl() {
        return subtitleUrl;
    }

    public void setSubtitleUrl(String subtitleUrl) {
        this.subtitleUrl = subtitleUrl;
    }
}