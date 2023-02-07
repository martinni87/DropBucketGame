package com.badlogic.drop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    final Drop game;

    //Variables to load assets
    private Image backgroundGame;
    private Image backgroundMenu;
    private Texture dropletBlue;
    private Texture dropletGrey;
    private Texture dropletYellow;
    private Texture dropletRed;
    private Texture bucketImage;
    private Sound dropSound;
    private Sound shootSound;
    private Music loopingMusic1;
    private Music loopingMusic2;
    private Music rainMusic;

    //Camera and spritebatch
    private OrthographicCamera camera;
//    private SpriteBatch batch;

    //Rectangles to position Textures
    private Rectangle bucket;
    //List of rectangles for raindrops (Class array from libgdx, less garbage)
    private Array<Rectangle> raindrops;
    //Last time we spawned a drop, to keep track of it
    private long lastDropTime; //Nanoseconds
    int dropsGathered;

    //Positioning vector for the bucket when screen is touched
    Vector3 touchPos;

    //Because this class doesn't extends from Game class, we don't have create() method.
    //So we use a Constructor
    public GameScreen (final Drop game){
        //Instantiate the game from Drop Class.
        this.game = game;

        // load big pictures
//		backgroundGame = new Image((Drawable) Gdx.files.internal("img/background.png"));
//		backgroundMenu = new Image((Drawable) Gdx.files.internal("img/background_menu.png"));

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropletBlue   = new Texture(Gdx.files.internal("img/droplet_blue.png"));
        dropletGrey   = new Texture(Gdx.files.internal("img/droplet_grey.png"));
        dropletYellow = new Texture(Gdx.files.internal("img/droplet_yellow.png"));
        dropletRed    = new Texture(Gdx.files.internal("img/droplet_red.png"));
        bucketImage   = new Texture(Gdx.files.internal("img/bucket.png"));

        // load the drop sound effect and the rain background "music"
        dropSound 	  = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));
        shootSound 	  = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        loopingMusic1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_calm.mp3"));
        loopingMusic2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_play.mp3"));
        rainMusic 	  = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));

        // SETS the playback of the background music
        loopingMusic1.setLooping(true);
        rainMusic.setLooping(true);


        //Creating the camera of 800x480 resolution
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //Now spritebatch comes from Game class, use game.batch instead
        //batch = game.batch;

        //Creating instance of rectangles and setting parameters
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20; //In libgdx y-axis is pointing upwards, so it's 20 units from bottom.
        bucket.width = 64;
        bucket.height = 64;

        //Instantiating raindrops array and spawning drops on screen
        raindrops = new Array<Rectangle>();
        spawnRaindrop(); //Here we spawn the first time the raindrops

    }

    /*
	OWN METHODS
	 */

    /**
     * spawnRaindrop() is a method to facilitate the creation of raindrops.
     * Instantiates a Rectangle object, and sets a random position at top edge of the screen
     * Then it adds the drop to the raindrops array
     */
    private void spawnRaindrop() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64); //random number between 0 and "800-64"
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    /*
    Override Methods
    */
    @Override
    public void render (float delta) {
        //Render screen clear with dark blue color
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();

        //Set camera render its matrix
        game.batch.setProjectionMatrix(camera.combined);

        //Begin to render calling the batch
        //Spritebatch to use, start...
        game.batch.begin();
        //Every time a drops get collected, we show the number of drops at the left-top corner
        game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, 480);
        //Draw bucketImage in coordinates x y defined in bucket rectangle.
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        //Draw raindrops
        for (Rectangle raindrop: raindrops){
            game.batch.draw(dropletBlue, raindrop.x, raindrop.y);
        }
        //End batch
        game.batch.end();

        //After loading Textures, we set the position of the bucket onto the users clicks:
        //If there's a touch or click, we enter the if statement
        if(Gdx.input.isTouched()) {
            //Initialize vector for touch position
            touchPos = new Vector3();
            //We get the position touched
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            //Transforming previous coordinates to camera coord.
            camera.unproject(touchPos);
            //We set the new position of the bucket for the coordinates we clicked before
            bucket.x = touchPos.x - 64 / 2;
        }

        /*
        USER INPUTS
         */
        //We also set the movement for the desktop and web environment with left and right keys pressed
        //Speed at 200 units per second
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){ //This condition is for the LEFT key pressed
            //With getDeltaTime() we know the time that passed between the last and current rendering frame
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){ //This condition is for the RIGHT key pressed
            //With getDeltaTime() we know the time that passed between the last and current rendering frame
            bucket.x += 200 * Gdx.graphics.getDeltaTime();
        }

        //We also make sure our bucket stays within the screen limits
        if(bucket.x < 0){
            bucket.x = 0;
        }
        if(bucket.x > 800 - 64){
            bucket.x = 800 - 64;
        }

        //After spawning the first time, we check time passed and spawn again
        if(TimeUtils.nanoTime() - lastDropTime > 1000000000){
            spawnRaindrop();
        }

        //Making raindrops movement at 200pps. If the raindrop reaches the bottom of the screen, then remove it
        for (Iterator<Rectangle> iter = raindrops.iterator(); iter.hasNext();) {
            Rectangle raindrop = iter.next();
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
            //If the raindrop reaches the bottom of the screen, then remove it
            if(raindrop.y + 64 < 0){
                iter.remove();
            }
            //If the raindrop hits the bucket, it should get into it. So we remove it and play the drop sound.
            if(raindrop.overlaps(bucket)) {
                dropsGathered++;
                dropSound.play();
                iter.remove();
            }
        }
    }

    @Override
    public void show() {
        // Starts the playback of the background music when the screen is shown
        loopingMusic1.play();
        rainMusic.play();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose () {
        //Cleaning up. Disposables don't get erased by Java Garbage Collector
        dropletBlue.dispose();
        bucketImage.dispose();
        dropSound.dispose();
        rainMusic.dispose();
//        batch.dispose();
    }


}
