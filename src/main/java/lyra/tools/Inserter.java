package lyra.tools;

import lyra.dal.*;
import lyra.model.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Inserter {
	public static void main(String[] args) throws SQLException {
		// DAO Instances
		EmotionDao emotionDao = EmotionDao.getInstance();
		
		// CREATE
		// create emotions
		Emotion happy = emotionDao.create(new Emotion("happy"));
		Emotion sad = emotionDao.create(new Emotion("sad"));
		Emotion angry = emotionDao.create(new Emotion("angry"));
		
		
		// CRUD (Create, Read, Update, Delete)
		/////////////////////////////////////////
        // Emotions
		/////////////////////////////////////////
        // CREATE
        Emotion emotionExample = new Emotion("scared");
        emotionExample= emotionDao.create(emotionExample);
        // READ
        Emotion emotionRead = emotionDao.getEmotionById(emotionExample.getEmotionId());
        System.out.println("Emotion Read: " + emotionRead.getName());
        // UPDATE
        Emotion emotionUpdate = emotionDao.updateEmotionName(emotionExample, "SCARED");
        System.out.println("Emotion Update: " + emotionUpdate.getName());
        // DELETE
        List<Emotion> currentEmotions = emotionDao.getAllEmotions();
        System.out.println("Emotions Before Delete: " + Arrays.toString(currentEmotions.toArray()));
        emotionDao.delete(emotionExample);
        currentEmotions = emotionDao.getAllEmotions();
        System.out.println("Emotions After Delete:  " + Arrays.toString(currentEmotions.toArray()));        
	}
}