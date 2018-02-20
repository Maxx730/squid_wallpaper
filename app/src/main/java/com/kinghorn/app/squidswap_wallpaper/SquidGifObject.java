package com.kinghorn.app.squidswap_wallpaper;

import android.util.Log;

import java.util.Random;

public class SquidGifObject {
    //Dinstance will be 1,2 or 3 as in the layers.
    public int x_pos,y_pos,swim_speed,rotation,distance;
    private int swidth,sheight;
    public float scale;

    public SquidGifObject(int screen_width,int screen_height,int layer){
        this.swidth = screen_width;
        this.sheight = screen_height;
        this.generate_random_rotation();
        this.generate_random_x();
        this.generate_random_y();
        this.distance = layer;

        //Determine the speed of the squid based on the distance.
        switch(this.distance){
            case 1:
                this.swim_speed = 10;
                break;
            case 2:
                this.swim_speed = 5;
                break;
            case 3:
                this.swim_speed = 2;
                break;
        }

        switch(this.distance){
            case 1:
                this.scale = 1;
                break;
            case 2:
                this.scale = .5f;
                break;
            case 3:
                this.scale = .25f;
                break;
        }
    }

    public void generate_random_x(){
        this.x_pos = Math.round((float) Math.random() * swidth);
    }

    public void generate_random_y(){
        this.y_pos = Math.round((float) Math.random() * sheight) - 200;
    }

    public void generate_random_rotation(){
        this.rotation = Math.round((float) Math.random() * 360);
    }
}
