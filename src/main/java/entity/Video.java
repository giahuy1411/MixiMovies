package entity;

import javax.persistence.*;

@Entity
@Table(name = "Videos")
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Description")
    private String description;

    @Column(name = "Views")
    private Integer views = 0;

    @Column(name = "Active")
    private Boolean active = true;

    @Column(name = "VideoUrl", nullable = false)
    private String videoUrl;

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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Transient
    public String getYoutubeThumbnail() {
        if (videoUrl == null || videoUrl.isEmpty()) return "";

        String videoId = "";

        if (videoUrl.contains("embed/")) {
            videoId = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        } else if (videoUrl.contains("v=")) {
            videoId = videoUrl.substring(videoUrl.indexOf("v=") + 2);
        } else if (videoUrl.contains("youtu.be/")) {
            videoId = videoUrl.substring(videoUrl.lastIndexOf("/") + 1);
        }

        // Cắt ? & (si, t, list...)
        if (videoId.contains("?")) {
            videoId = videoId.substring(0, videoId.indexOf("?"));
        }
        if (videoId.contains("&")) {
            videoId = videoId.substring(0, videoId.indexOf("&"));
        }

        return "https://img.youtube.com/vi/" + videoId + "/hqdefault.jpg";
    }
}