package data_mapping;

public class BlogExt<T> extends Blog {
    private int rating;
    // WIP
    private T wd;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public T getWd() {
        return wd;
    }

    public void setWd(T wd) {
        this.wd = wd;
    }

    @Override
    public String toString() {
        return "BlogExt{" +
                "rating=" + rating +
                ", wd=" + wd +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
