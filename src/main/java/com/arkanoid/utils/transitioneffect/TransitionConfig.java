package com.arkanoid.utils.transitioneffect;

import com.arkanoid.utils.SceneType;

import java.util.HashMap;
import java.util.Map;

public class TransitionConfig {

    private static TransitionConfig instance;
    private final Map<String, SceneTransition.TransitionType> config = new HashMap<>();
    private SceneTransition.TransitionType defaultTransition = SceneTransition.TransitionType.DARK_FADE;

    private TransitionConfig() {
        setupDefaults();
    }

    public static TransitionConfig getInstance() {
        if (instance == null) {
            instance = new TransitionConfig();
        }
        return instance;
    }


    private void setupDefaults() {

        // Menu -> Other Scenes
        set(SceneType.Menu, SceneType.LevelSelection, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Menu, SceneType.Store, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Menu, SceneType.HighScore, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Menu, SceneType.Option, SceneTransition.TransitionType.DARK_FADE);

        // Back to Menu
        set(SceneType.LevelSelection, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Store, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.HighScore, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Option, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Game_Over, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);



        set(SceneType.LevelSelection, SceneType.GamePlay, SceneTransition.TransitionType.DARK_FADE);


        set(SceneType.GamePlay, SceneType.Menu, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.GamePlay, SceneType.Game_Over, SceneTransition.TransitionType.DARK_FADE);

        set(SceneType.Game_Over, SceneType.LevelSelection, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.Game_Over, SceneType.GamePlay, SceneTransition.TransitionType.DARK_FADE);

        set(SceneType.Store, SceneType.HighScore, SceneTransition.TransitionType.DARK_FADE);
        set(SceneType.HighScore, SceneType.Store, SceneTransition.TransitionType.DARK_FADE);
    }


    public void set(SceneType from, SceneType to, SceneTransition.TransitionType type) {
        String key = makeKey(from, to);
        config.put(key, type);
    }

    public SceneTransition.TransitionType get(SceneType from, SceneType to) {
        if (from == null || to == null) {
            return defaultTransition;
        }

        String key = makeKey(from, to);
        return config.getOrDefault(key, defaultTransition);
    }


    public void setDefaultTransition(SceneTransition.TransitionType type) {
        this.defaultTransition = type;
    }

    public SceneTransition.TransitionType getDefaultTransition() {
        return defaultTransition;
    }


    public void clear() {
        config.clear();
    }


    public void reset() {
        clear();
        setupDefaults();
    }


    private String makeKey(SceneType from, SceneType to) {
        return from.name() + "->" + to.name();
    }


}