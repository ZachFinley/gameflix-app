package psu.edu.GameFlix.Models;

public class GameSummary {
    private Long id;
    private String slug;
    private String title;
    private String esrbRating;
    private String releaseDate;
    private String status;
    private Double msrp;

    public GameSummary() {}

    public GameSummary(Long id, String slug, String title, String esrbRating, String releaseDate, String status, Double msrp) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.esrbRating = esrbRating;
        this.releaseDate = releaseDate;
        this.status = status;
        this.msrp = msrp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getMsrp() {
        return msrp;
    }

    public void setMsrp(Double msrp) {
        this.msrp = msrp;
    }
}