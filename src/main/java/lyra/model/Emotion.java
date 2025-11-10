package lyra.model;

/**
 * Emotion represents information about a song's emotional category (Sad, Happy, etc.)
 */
public class Emotion {
    /** Attributes */
    private int emotionId;
    private String name;

    /** Emotion Constructor */
    public Emotion(int emotionId, String name) {
        this.emotionId = emotionId;
        this.name = name;
    }

    /** Emotion constructor without emotion id */
    public Emotion(String name) {
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

    /** Override the to string method to print out an emotion */
    @Override
    public String toString() {
        return "Emotion{id: " + emotionId + ", name: '" + name + "'}";
    }
}
