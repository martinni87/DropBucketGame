package com.badlogic.drop.views;

import static java.lang.Thread.sleep;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen implements Screen {
    Drop game;

    //Variables to load assets
    private Texture backgroundGame;
    private Texture dropletBlue, dropletRed;
    private Texture bucketImage;
    public BitmapFont gameText;
    private Sound dropSound;

    //Camera and spritebatch
    private OrthographicCamera camera;
    private SpriteBatch batch;

    //Rectangles to position Textures
    private Rectangle bucket;
    //List of rectangles for raindrops (Class array from libgdx, less garbage)
    private Array<Rectangle> blueDropsArray;
    private Array<Rectangle> redDropsArray;
    //Last time we spawned a drop, to keep track of it
    private long lastBlueDrop, lastRedDrop; //Nanoseconds
    //Counter of total drops gathered, lives left, and good and bad streaks
    //10 good streaks adds one life, 3 bad streaks takes a life. On 0 lives left, game over.
    int totalDropsGathered, playerLives, goodStreak, badStreak;
    //To add difficult, every life earned makes level +1, increasing speed.
    int level = 1;

    public Music music;
    public Music rain;

    //Positioning vector for the bucket when screen is touched
    Vector3 touchPos;

    //Because this class doesn't extends from Game class, we don't have create() method.
    //So we use a Constructor

    public GameScreen (Drop game){
        //Instantiate the game from Drop Class, new batch and camera
        this.game = game;

        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //Loading new fonts
        gameText = new BitmapFont();

        // load background for game
        backgroundGame = new Texture(Gdx.files.internal("img/background_game.png"));

        // load the images for the droplet and the bucket, 64x64 pixels each
        dropletBlue   = new Texture(Gdx.files.internal("img/droplet_blue.png"));
        dropletRed   = new Texture(Gdx.files.internal("img/droplet_red.png"));
        bucketImage   = new Texture(Gdx.files.internal("img/bucket.png"));

        // load the drop sound effect and the rain background "music"
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        rain = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));
        dropSound  = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));

        //Creating the camera of 800x480 resolution
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        //Creating instance of rectangles and setting parameters
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20; //In libgdx y-axis is pointing upwards, so it's 20 units from bottom.
        bucket.width = 64;
        bucket.height = 64;

        //Instantiating raindrops array and spawning drops on screen
        blueDropsArray = new Array<Rectangle>();
        redDropsArray = new Array<Rectangle>();
        //Here we spawn the first time the raindrops
        spawnDrops();

        //Lives to begin the match
        playerLives = 1;

    }

    /*
    Override Methods
    */
    @Override
    public void render (float delta) {
        //Render screen clear with dark blue color
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        //Set camera render its matrix
        batch.setProjectionMatrix(camera.combined);

        //Begin to render calling the batch
        //Spritebatch to use, start...
        batch.begin();
        //Draw bucketImage in coordinates x y defined in bucket rectangle.
        batch.draw(backgroundGame,0,0);
        //Draw the background picture
        batch.draw(bucketImage, bucket.x, bucket.y);
        //Every time a drops get collected, we show the number of drops at the left-top corner
        //For every drop not collected, the player loses 1 life
        gameText.setColor(Color.BLACK);
        gameText.getData().setScale(1.3f);
        gameText.draw(batch, "Gotas recogidas: " + totalDropsGathered, 40, 440);
        gameText.draw(batch, "Vidas: " + playerLives + " Nivel: " + level, 630,440);
        //Draw raindrops
        for (Rectangle raindrop: blueDropsArray){
            batch.draw(dropletBlue, raindrop.x, raindrop.y);
        }
        //End batch
        batch.end();

        /*
        USER TOUCH INPUTS
         */
        actionForESCKey();
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
        USER KEYUBOARD INPUTS
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
        //Lets increase the difficulty in each level increase!!
        int levelCase = Math.min(level, 5);
        switch (levelCase){
            case 5:
                if (TimeUtils.nanoTime() - lastBlueDrop > 200000000) {
                    spawnDrops();
                }
                break;
            case 4:
                if (TimeUtils.nanoTime() - lastBlueDrop > 400000000) {
                    spawnDrops();
                }
                break;
            case 3:
                if (TimeUtils.nanoTime() - lastBlueDrop > 600000000) {
                    spawnDrops();
                }
                break;
            case 2:
                if (TimeUtils.nanoTime() - lastBlueDrop > 800000000) {
                    spawnDrops();
                }
                break;
            default:
                if (TimeUtils.nanoTime() - lastBlueDrop > 1000000000) {
                    spawnDrops();
                }
        }


        for (Iterator<Rectangle> iter = blueDropsArray.iterator(); iter.hasNext();) {
            Rectangle raindrop = iter.next();
            //Making raindrops movement at 200pps. We can add 5pps eachtime we reach 15 extra drops
            raindrop.y -= 200 * Gdx.graphics.getDeltaTime() + level*2;
            //If the raindrop reaches the bottom of the screen, then remove it
            if(raindrop.y + 64 < 0){
                iter.remove();
                badStreak++;
                goodStreak = 0;
            }
            //If the raindrop hits the bucket, it should get into it. So we remove it and play the drop sound.
            if(raindrop.overlaps(bucket)) {
                totalDropsGathered++;
                goodStreak++;
                badStreak = 0;
                dropSound.play();
                iter.remove();
            }
            if (goodStreak >= 10){
                playerLives++;
                level++;
                goodStreak = 0;
            }
            if (badStreak >= 3){
                playerLives--;
                badStreak = 0;
            }
            if (playerLives <= 0){
                gameIsOver();
            }
        }
    }

    @Override
    public void show() {
        // Starts the playback of the background music when the screen is shown
        if (!music.isPlaying() && !rain.isPlaying()){
            music.play();
            rain.play();
            music.setVolume(0.3f);
            rain.setVolume(0.3f);
            music.setLooping(true);
            rain.setLooping(true);
        }
        else{
            music.stop();
            rain.stop();
        }
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
        batch.dispose();
    }

    /*
	OWN METHODS
	 */
    /**
     * spawnRaindrop() is a method to facilitate the creation of raindrops.
     * Instantiates a Rectangle object, and sets a random position at top edge of the screen
     * Then it adds the drop to the raindrops array
     */
    private void spawnDrops() {
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64); //random X position between 0 and "800-64"
        raindrop.y = 480; //Always starts at 480 Y
        raindrop.width = 64;
        raindrop.height = 64;
        blueDropsArray.add(raindrop);
        lastBlueDrop = TimeUtils.nanoTime();
    }

    public void actionForESCKey(){
        //Creating an inputProcessor to handle back key actions
        InputProcessor backProcessor = new InputAdapter(){
            @Override
            public boolean keyDown(int keycode){
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    Gdx.app.log("MARTIN DEBUG", "GO BACK");
                    music.stop();
                    rain.stop();
                    music.dispose();
                    rain.dispose();
                    game.setScreen(new MainMenu(game));
                    return false;
                }
                return false;
            }
        };
        //Adding it to a multiplexer
        InputMultiplexer multiplexer = new InputMultiplexer(backProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        //Catch back key press to avoid bad exiting the app
        Gdx.input.setCatchKey(Input.Keys.BACK, true);
    }

    public void gameIsOver(){
        Gdx.app.log("MARTIN DEBUG", "Game is over");
        music.stop();
        rain.stop();
        music.dispose();
        rain.dispose();
        game.setScreen(new GameOverView(game,this));
    }


}
