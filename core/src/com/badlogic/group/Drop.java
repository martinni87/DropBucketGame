package com.badlogic.group;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ScreenUtils;

public class Drop extends ApplicationAdapter {
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
	private SpriteBatch batch;

	//Rectangles to position Textures
	private Rectangle bucket;
	private Rectangle drop;

	//Positioning vector for the bucket when screen is touched
	Vector3 touchPos;

	@Override
	public void create () {
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
		dropSound 	 = Gdx.audio.newSound(Gdx.files.internal("sounds/drop.wav"));
		shootSound 	 = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
		loopingMusic1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_calm.mp3"));
		loopingMusic2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/looping_play.mp3"));
		rainMusic 	 = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));

		// start the playback of the background music immediately
		loopingMusic1.setLooping(true);
		rainMusic.setLooping(true);
		loopingMusic1.play();
		rainMusic.play();

		//Creating the camera of 800x480 resolution
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		//Creating the spritebatch
		batch = new SpriteBatch();

		//Creating instance of rectangles and setting parameters
		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20; //In libgdx y-axis is pointing upwards, so it's 20 units from bottom.
		bucket.width = 64;
		bucket.height = 64;

	}

	@Override
	public void render () {
		//Render screen clear with dark blue color
		ScreenUtils.clear(0, 0, 0.2f, 1);
		camera.update();

		//Spritebatch to use, start, draw bucketImage in coordinates x y defined in bucket rectangle,end.
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bucketImage, bucket.x, bucket.y);
		batch.end();

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
	}
	
	@Override
	public void dispose () {

	}
}
