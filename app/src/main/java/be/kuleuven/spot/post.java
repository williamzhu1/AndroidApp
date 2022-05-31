package be.kuleuven.spot;

import java.text.SimpleDateFormat;

public class post {
    private int id;
    private String username;
    private String content;
    private String date;
    private double distance;
    private String image;

    public post(int id, String username, String content, String date, double distance, String image) {
        this.id = id;
        this.username = username;
        this.content = content;
        this.date = date;
        this.distance = distance;
        this.image = image;
    }

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

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
