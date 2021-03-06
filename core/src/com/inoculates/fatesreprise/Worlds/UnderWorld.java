package com.inoculates.fatesreprise.Worlds;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.inoculates.fatesreprise.Characters.Druni;
import com.inoculates.fatesreprise.Screens.GameScreen;
import com.inoculates.fatesreprise.Storage.Storage;

import java.util.ArrayList;

// Largely the same as the Houses class.
public class UnderWorld extends World {

    // Same as portals in and portals out.
    private ArrayList<Rectangle> exits = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> entrances = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> downstairs = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> upstairs = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> teleporters1 = new ArrayList<Rectangle>();
    private ArrayList<Rectangle> teleporters2 = new ArrayList<Rectangle>();

    public UnderWorld(Storage storage, OrthographicCamera camera, TiledMap map, GameScreen screen) {
        super(storage, camera, map, screen);
        setAccess();
        setStairs();
        setPortals();
        createCharacters();
    }

    protected void createCharacters() {

    }

    // Adds all the exits and entrances.
    private void setAccess() {
        for (MapObject object : map.getLayers().get("Access").getObjects())
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();

                if (object.getProperties().containsKey("entrance"))
                    entrances.add(rect);
                if (object.getProperties().containsKey("exit"))
                    exits.add(rect);
            }
    }

    // Adds all the stairs in the game.
    private void setStairs() {
        for (MapObject object : map.getLayers().get("Level").getObjects())
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();

                if (object.getProperties().containsKey("up"))
                    upstairs.add(rect);
                if (object.getProperties().containsKey("down"))
                    downstairs.add(rect);
            }
    }

    // Adds all the teleports in the game.
    private void setPortals() {
        for (MapObject object : map.getLayers().get("Teleporters").getObjects())
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                if (object.getProperties().containsKey("TP1"))
                    teleporters1.add(rect);
                if (object.getProperties().containsKey("TP2"))
                    teleporters2.add(rect);
            }
    }

    // Same as for upperworld.
    protected void setShaderTransitions() {
        Vector2 vec;
        // For Faron Out (i.e. removes the faron wood shader).
        vec = new Vector2(1, 15);
        shaderCells.get("fwout").add(vec);
    }

    // Plays music if Daur enters a specific cell (and no music is playing previously). This method simply adds the cells
    // to the arraylist.
    protected void setMusicCells() {
        Vector2 vec;
        // For the Great Hollow Dungeon. Uses multiple for loops to carve up the nearly-rectangular dungeon.
        for (int i = 1; i <= 9; i++) {
            vec = new Vector2(i, 15);
            musicCells.get("greathollow").add(vec);
        }
        for (int y = 14; y >= 13; y--)
            for (int x = 0; x <= 9; x++) {
                vec = new Vector2(x, y);
                musicCells.get("greathollow").add(vec);
            }
        // Adds the last five cells.
        for (int i = 3; i <= 7; i ++) {
            vec = new Vector2(i, 12);
            musicCells.get("greathollow").add(vec);
        }
    }

    public Rectangle getAccess(int p, boolean in) {
        if (in)
            return entrances.get(p);
        else
            return exits.get(p);
    }

    public Rectangle getStairs(int s, boolean up) {
        if (up)
            return upstairs.get(s);
        else
            return downstairs.get(s);
    }

    public Rectangle getTeleporter(int t, boolean one) {
        if (one)
            return teleporters1.get(t);
        else
            return teleporters2.get(t);
    }

    public int getAccessSize() {
        return entrances.size();
    }

    public int getStairsSize() {
        return upstairs.size();
    }

    public int getTeleporterSize() {
        return teleporters1.size();
    }

    public void setQuestEvents() {
        // Spawns Druni if not obtained.
        for (MapObject object : map.getLayers().get("Spawns").getObjects())
            if (object instanceof RectangleMapObject) {
                RectangleMapObject rectObject = (RectangleMapObject) object;
                Rectangle rect = rectObject.getRectangle();
                float x = (int) (rect.getX() / layer.getTileWidth()) * layer.getTileWidth() + layer.getTileWidth() / 2;
                float y = (int) (rect.getY() / layer.getTileHeight()) * layer.getTileHeight() + layer.getTileHeight() / 2;
                // If the first sage has not been rescued, spawns him accordingly.
                if (object.getProperties().containsKey("drunispawn") && !storage.sages[0]) {
                    Druni druni = new Druni(screen, map, screen.characterAtlases.get(0));
                    druni.setPosition(x - druni.getWidth() / 2, y - druni.getHeight() / 2);
                    screen.characters2.add(druni);
                }
            }
    }
}
