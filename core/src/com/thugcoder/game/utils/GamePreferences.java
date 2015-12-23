package com.thugcoder.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by Landolsi on 22/10/2015.
 */
public class GamePreferences {

    public static final GamePreferences instance =
            new GamePreferences();
    public boolean sound;
    public boolean music;
    public float volSound;
    public float volMusic;
    public int charSkin;
    public boolean showFpsCounter;
    public int score1, score2, score3, score4, score5;
    public String player1, player2, player3, player4, player5;


    private Preferences prefs;
    // singleton: prevent instantiation from other classes
    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load() {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f),
                0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f),
                0.0f, 1.0f);
        charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0),
                0, 2);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);
        score1 = prefs.getInteger("score1", 0);
        score2 = prefs.getInteger("score2", 0);
        score3 = prefs.getInteger("score3", 0);
        score4 = prefs.getInteger("score4", 0);
        score5 = prefs.getInteger("score5", 0);

        player1 = prefs.getString("player1", "Player_Name");
        player2 = prefs.getString("player2", "Player_Name");
        player3 = prefs.getString("player3", "Player_Name");
        player4 = prefs.getString("player4", "Player_Name");
        player5 = prefs.getString("player5", "Player_Name");


    }


    public String[] getPlayerNames() {
        String[] playersNames = new String[5];
        playersNames[0] = prefs.getString("player5", "Player_Name");
        playersNames[1] = prefs.getString("player4", "Player_Name");
        playersNames[2] = prefs.getString("player3", "Player_Name");
        playersNames[3] = prefs.getString("player2", "Player_Name");
        playersNames[4] = prefs.getString("player1", "Player_Name");

        return playersNames;
    }


    public void savePlayerNameAtPosition(int position, String playerName) {

        switch (position) {
            case 0:
                prefs.putString("player5", playerName);
                prefs.flush();
                break;
            case 1:
                prefs.putString("player4", playerName);
                prefs.flush();
                break;
            case 2:
                prefs.putString("player3", playerName);
                prefs.flush();
                break;
            case 3:
                prefs.putString("player2", playerName);
                prefs.flush();
                break;
            case 4:
                prefs.putString("player1", playerName);
                prefs.flush();
                break;

        }
    }

    public void save() {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);
        prefs.flush();
    }


    public void saveScores(int[] scores) {
        prefs.putInteger("score1", scores[4]);
        prefs.putInteger("score2", scores[3]);
        prefs.putInteger("score3", scores[2]);
        prefs.putInteger("score4", scores[1]);
        prefs.putInteger("score5", scores[0]);
        prefs.flush();
    }


    public void saveNames(String[] names) {

        prefs.putString("player1", names[4]);
        prefs.putString("player2", names[3]);
        prefs.putString("player3", names[2]);
        prefs.putString("player4", names[1]);
        prefs.putString("player5", names[0]);
        prefs.flush();
    }


    public int[] insertNewHighScore(int currentScore) {
        int[] scores = getScores();
        String[] names = getPlayerNames();
        scores[0] = currentScore;
        names[0] = "New High Score";
        for (int i = 0; i <= (scores.length - 2); i++) {
            for (int j = (scores.length - 1); i < j; j--) {
                if (scores[j] < scores[j - 1]) {
                    int x = scores[j - 1];
                    String y = names[j - 1];

                    //We sort both of the list of scores and player names!!
                    scores[j - 1] = scores[j];
                    scores[j] = x;
                    names[j - 1] = names[j];
                    names[j] = y;
                }
            }

        }


        saveNames(names);
        return scores;
    }


    public int[] getScores() {
        int[] scoresStored;
        scoresStored = new int[5];
        scoresStored[0] = prefs.getInteger("score5", 0);
        scoresStored[1] = prefs.getInteger("score4", 0);
        scoresStored[2] = prefs.getInteger("score3", 0);
        scoresStored[3] = prefs.getInteger("score2", 0);
        scoresStored[4] = prefs.getInteger("score1", 0);
        return scoresStored;
    }
}
