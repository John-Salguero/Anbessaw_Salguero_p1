package orm_playground.models;

import java.math.BigDecimal;

public class Movie {

    int m_id;
    private String title;
    private BigDecimal price;
    private boolean available;
    private long return_date;
    private Integer director_id;
    private Integer genre_id;

    public Movie(String title, BigDecimal price, boolean available, long return_date, Integer director_id, Integer genre_id) {
        this.title = title;
        this.price = price;
        this.available = available;
        this.return_date = return_date;
        this.director_id = director_id;
        this.genre_id = genre_id;
    }

    public Movie() {
    }

    @Override
    public String toString() {
        return "Movie{" +
                "m_id=" + m_id +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", available=" + available +
                ", return_date=" + return_date +
                ", director_id=" + director_id +
                ", genre_id=" + genre_id +
                '}';
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public long getReturn_date() {
        return return_date;
    }

    public void setReturn_date(long return_date) {
        this.return_date = return_date;
    }

    public Integer getDirector_id() {
        return director_id;
    }

    public void setDirector_id(Integer director_id) {
        this.director_id = director_id;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }
}
