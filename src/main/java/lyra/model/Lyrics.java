package lyra.model;

public class Lyrics {
    private int songId;
    private String content;

    public Lyrics(int songId) {
        this.songId = songId;
    }

    public Lyrics(int songId, String content) {
        this.songId = songId;
        this.content = content;
    }

    public int getSongId() { return songId; }
    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    @Override
    public String toString() {
        return "Lyrics{songId=" + songId + ", content=" +
                (content == null ? "null" : (content.length() > 40 ? content.substring(0, 40) + "..." : content)) + "}";
    }
}
