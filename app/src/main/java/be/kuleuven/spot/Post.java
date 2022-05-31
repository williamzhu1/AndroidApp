package be.kuleuven.spot;


import androidx.annotation.NonNull;

public class Post {
    private int id;
    private final String username;
    private final String content;
    private final String date;
    private final double distance;
    private final String image;

    public Post(int id, String username, String content, String date, double distance, String image) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.date = date;
        this.distance = distance;
        this.image = image;
        System.out.println("success" + image);
    }

    @NonNull
    @Override
    public String toString() {
        return "post{" +
                "id=" + id +
                ", username=" + username + '\'' +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", distance=" + distance +
                ", image='" + image + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public double getDistance() {
        return distance;
    }

    public String getImage() {
        return image;
    }

    public String getUsername() {
        return username;
    }

}
