package lyra.model;

/**
 * Emotion represents information about a song's emotional category (Sad, Happy, etc.)
 */
public class Emotion {
    /** Attributes */
    private int emotionId;
    private String name;

    /** Constructor */
    public Emotion(int emotionId, String name) {
        this.emotionId = emotionId;
        this.name = name;
    }

    /** Getters and setters */
    public int getEmotionId() {
        return emotionId;
    }

    public void setEmotionId(int emotionId) {
        this.emotionId = emotionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
