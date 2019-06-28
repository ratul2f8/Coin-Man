package com.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] manFrame  = new Texture[4];
	private int manState = 0;
	private int pause = 0;
	private float velocity = 0.2f;
	private float gravity = 0;
	private int manY;
	int gameState = 0;
	Texture diz;
	private ArrayList<Integer> xPos;
	private ArrayList<Integer> yPos;
	private ArrayList<Integer> bxPos;
	private ArrayList<Integer> byPos;
	private ArrayList<Rectangle> coinRec = new ArrayList<Rectangle>();
	private ArrayList<Rectangle> bombRec  = new ArrayList<Rectangle>();
	BitmapFont font;
	private int score = 0;
	Rectangle manRec;
	private int coinCount = 0;
	private int bombCount = 0;
	Random random;
	Random random2;
	Texture coin;
	Texture bomb;
	@Override
	public void create () {
		batch = new SpriteBatch();
		diz = new Texture("dizzy.png");
		background = new Texture("bg.png");
		manFrame[0] = new Texture("frame-1.png");
		manFrame[1] = new Texture("frame-2.png");
		manFrame[2] = new Texture("frame-3.png");
		manFrame[3] = new Texture("frame-4.png");
		manY = Gdx.graphics.getHeight()/2;
		xPos = new ArrayList<Integer>();
		yPos = new ArrayList<Integer>();
		bxPos = new ArrayList<Integer>();
		byPos = new ArrayList<Integer>();
		random = new Random();
		random2 = new Random();
		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(8);
	}
	public void makeCoin(){
		float  num = random.nextFloat()*(Gdx.graphics.getHeight()-3);
		yPos.add((int)num);
		xPos.add(Gdx.graphics.getWidth());


	}
	public void makeBomb(){
		float num2 = random2.nextFloat()*(Gdx.graphics.getHeight()-3);
		byPos.add((int)num2);
		bxPos.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render () {

		batch.begin();
         //Gaming
		if(gameState == 1){
			if(Gdx.input.justTouched()){
				gravity = -10;
			}
			if(pause < 5){
				pause++;
			}else{
				pause = 0;
				if(manState < 3){
					manState++;
				}else{
					manState = 0;
				}
			}
			gravity += velocity;
			manY -= gravity;

			if(manY <= 0){
				manY = 5;
			}

			if(coinCount < 100){
				coinCount++;
			}else{
				coinCount = 0;
				makeCoin();
			}

		}else if(gameState == 0){
			if(Gdx.input.justTouched()){
				gameState = 1;
			}
		}else if(gameState == 2){
               if(Gdx.input.justTouched()){
               	gameState = 1;
               	xPos.clear();
               	yPos.clear();
               	coinRec.clear();
               	bombRec.clear();
               	bxPos.clear();
               	byPos.clear();
               	manY = Gdx.graphics.getHeight()/2;
               	score = 0;
               	gravity = 0;
               	bombCount = 0;
			   }
		}
		 //Gaming
		batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		coinRec.clear();
		for(int i = 0 ;i < xPos.size();i++){
			batch.draw(coin,xPos.get(i),yPos.get(i));
			xPos.set(i,xPos.get(i)-4);
		    coinRec.add(new Rectangle(xPos.get(i),yPos.get(i),coin.getWidth(),coin.getHeight()));
		}
		if(bombCount < 250){
			bombCount++;
		}else{
			bombCount = 0;
			makeBomb();
		}
		bombRec.clear();
		for(int i = 0 ;i < bxPos.size() ;i++){
			batch.draw(bomb,bxPos.get(i),byPos.get(i));
			bxPos.set(i,bxPos.get(i) - 4);
			bombRec.add(new Rectangle(bxPos.get(i),byPos.get(i),bomb.getWidth(),bomb.getHeight()));
		}
			batch.draw(manFrame[manState],Gdx.graphics.getWidth()/2 - manFrame[manState].getWidth()/2,manY);



        manRec = new Rectangle(Gdx.graphics.getWidth()/2 - manFrame[manState].getWidth()/2,manY,manFrame[manState].getWidth(),manFrame[manState].getHeight());
		for(int i = 0 ;i < coinRec.size();i++){
			if(Intersector.overlaps(manRec,coinRec.get(i))){
				Gdx.app.log("Coin","Collison!");
				score++;
				xPos.remove(i);
				yPos.remove(i);
				break;
			}
		}
		for(int i = 0 ;i < bombRec.size();i++){
			if(Intersector.overlaps(manRec,bombRec.get(i))){
				Gdx.app.log("Bomb","Killed!");
				gameState = 2;
				batch.draw(diz,Gdx.graphics.getWidth()/2 - manFrame[manState].getWidth()/2,manY);
			}
		}
		font.draw(batch,String.valueOf(score),50,220);
        batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
