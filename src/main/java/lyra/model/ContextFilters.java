package lyra.model;

public class ContextFilters {
    private int songId;
    private boolean party;
    private boolean study;
    private boolean relaxation;
    private boolean exercise;
    private boolean running;
    private boolean yoga;
    private boolean driving;
    private boolean social;
    private boolean morning;

    public ContextFilters(int songId) {
        this.songId = songId;
    }

    public ContextFilters(int songId, boolean party, boolean study, boolean relaxation, boolean exercise,
                          boolean running, boolean yoga, boolean driving, boolean social, boolean morning) {
        this.songId = songId;
        this.party = party;
        this.study = study;
        this.relaxation = relaxation;
        this.exercise = exercise;
        this.running = running;
        this.yoga = yoga;
        this.driving = driving;
        this.social = social;
        this.morning = morning;
    }

    public int getSongId() { return songId; }
    public boolean isParty() { return party; }
    public boolean isStudy() { return study; }
    public boolean isRelaxation() { return relaxation; }
    public boolean isExercise() { return exercise; }
    public boolean isRunning() { return running; }
    public boolean isYoga() { return yoga; }
    public boolean isDriving() { return driving; }
    public boolean isSocial() { return social; }
    public boolean isMorning() { return morning; }

    public void setParty(boolean party) { this.party = party; }
    public void setStudy(boolean study) { this.study = study; }
    public void setRelaxation(boolean relaxation) { this.relaxation = relaxation; }
    public void setExercise(boolean exercise) { this.exercise = exercise; }
    public void setRunning(boolean running) { this.running = running; }
    public void setYoga(boolean yoga) { this.yoga = yoga; }
    public void setDriving(boolean driving) { this.driving = driving; }
    public void setSocial(boolean social) { this.social = social; }
    public void setMorning(boolean morning) { this.morning = morning; }

    @Override
    public String toString() {
        return "ContextFilters{songId=" + songId +
                ", party=" + party +
                ", study=" + study +
                ", relaxation=" + relaxation +
                ", exercise=" + exercise +
                ", running=" + running +
                ", yoga=" + yoga +
                ", driving=" + driving +
                ", social=" + social +
                ", morning=" + morning + "}";
    }
}
