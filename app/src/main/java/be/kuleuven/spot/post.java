package be.kuleuven.spot;

public class post {
    private int id;
    private String content;
    private int date;
    private int distance;
    private String image;

    public post(int id, String content, int date, int distance, String image) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.distance = distance;
        this.image = image;
    }

    @Override
    public String toString() {
        return "post{" +
                "id=" + id +
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDistance() {
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

    private void fillpostlist(){

    }
}
