package com.kinghorn.app.squidswap_wallpaper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.service.wallpaper.WallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SquidWallpaperService extends WallpaperService {

    private boolean SquidVisible;
    Canvas SquidCan;

    @Override
    public Engine onCreateEngine() {
        final Bitmap squid_bit;
        final Bitmap back_bit;
        final Bitmap scaled_back;
        final Bitmap squid_group;

        squid_bit = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.small_group,null);
        back_bit = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.good_back,null);
        scaled_back = scale_background(Resources.getSystem().getDisplayMetrics().heightPixels,back_bit);
        squid_group = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.small_group_tiny,null);

        return new SquidWall(squid_bit,scaled_back,squid_group);
    }

    private Bitmap scale_background(int screen_width,Bitmap orig){
        Matrix m = new Matrix();
        float scale = (screen_width / orig.getHeight()) + .5f;
        Log.d("SCALED UP: ",String.valueOf(scale));
        m.setScale(scale,scale);
        Bitmap final_b = Bitmap.createBitmap(orig,0,0,orig.getWidth(),orig.getHeight(),m,true);
        return final_b;
    }

    public class SquidWall extends Engine{
        final Handler SquidHandler = new Handler();
        final Bitmap SquidBitmap;
        final Bitmap SquidBack;
        final Bitmap BackgroundSquids;
        final ArrayList<SquidGifObject> squids;
        final ArrayList<SquidBackSwimmer> back_swimmers;
        final int height,width;

        public SquidWall(Bitmap b,Bitmap c,Bitmap d){
            SquidBitmap = b;
            SquidBack = c;
            BackgroundSquids = d;

            //Determine how much to scale the background up based on the screensize.


            squids = new ArrayList<SquidGifObject>();
            back_swimmers = new ArrayList<SquidBackSwimmer>();

            width = Resources.getSystem().getDisplayMetrics().widthPixels;
            height = Resources.getSystem().getDisplayMetrics().heightPixels;

            squids.add(new SquidGifObject(width,height,1));
            squids.add(new SquidGifObject(width,height,1));
            squids.add(new SquidGifObject(width,height,1));

            squids.add(new SquidGifObject(width,height,2));
            squids.add(new SquidGifObject(width,height,2));
            squids.add(new SquidGifObject(width,height,2));
            squids.add(new SquidGifObject(width,height,2));

            squids.add(new SquidGifObject(width,height,3));
            squids.add(new SquidGifObject(width,height,3));
            squids.add(new SquidGifObject(width,height,3));
            squids.add(new SquidGifObject(width,height,3));
            squids.add(new SquidGifObject(width,height,3));

            back_swimmers.add(new SquidBackSwimmer(width,height,2));
            back_swimmers.add(new SquidBackSwimmer(width,height,2));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,2));
            back_swimmers.add(new SquidBackSwimmer(width,height,2));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,2));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
            back_swimmers.add(new SquidBackSwimmer(width,height,3));
    }

        private final Runnable SquidRunnable = new Runnable() {
            @Override
            public void run() {
                SquidDraw();
            }
        };

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            SquidDraw();
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            SquidVisible = false;
            SquidHandler.removeCallbacks(SquidRunnable);
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            SquidHandler.removeCallbacks(SquidRunnable);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            SquidVisible = visible;

            if(visible){
                SquidDraw();
            }else{
                SquidHandler.removeCallbacks(SquidRunnable);
            }
        }

        public void SquidDraw(){
            final SurfaceHolder SquidTempHold = getSurfaceHolder();
            SquidCan = null;

            try{
                SquidCan = SquidTempHold.lockCanvas();

                if(SquidCan != null){
                    Matrix m = new Matrix();
                    SquidCan.drawColor(Color.parseColor("#281856"));

                    SquidCan.drawBitmap(return_background(),0,0,null);
                    Paint p = new Paint();
                    p.setStyle(Paint.Style.FILL);

                    for(int i = 0;i < back_swimmers.size();i++){
                        //Draw the background squids swimming.
                        SquidCan.drawBitmap(return_distance_scale(back_swimmers.get(i)),back_swimmers.get(i).x_pos,back_swimmers.get(i).y_pos,null);

                        back_swimmers.get(i).x_pos += back_swimmers.get(i).swim_speed;
                        back_swimmers.get(i).y_pos -= 1;

                        if(back_swimmers.get(i).x_pos > width + 500){
                            back_swimmers.get(i).x_pos = -500;
                        }

                        if(back_swimmers.get(i).y_pos < -100){
                            back_swimmers.get(i).generate_random_y();
                        }
                    }

                    //Grab all three lists of squids.
                    ArrayList<SquidGifObject> front = return_squids(1);
                    ArrayList<SquidGifObject> mid = return_squids(2);
                    ArrayList<SquidGifObject> back = return_squids(3);

                    //draw_squid(SquidCan,back);
                    //draw_squid(SquidCan,mid);
                    //draw_squid(SquidCan,front);
                }
            } finally {
                if(SquidCan != null){
                    SquidTempHold.unlockCanvasAndPost(SquidCan);
                }
            }

            SquidHandler.removeCallbacks(SquidRunnable);

            if(SquidVisible){
                SquidHandler.postDelayed(SquidRunnable,3);
            }
        }

        private Bitmap return_background(){
            LinearGradient r = new LinearGradient(0,0,0,height,Color.parseColor("#633cd6"),Color.parseColor("#281856"), Shader.TileMode.CLAMP);
            Paint p = new Paint();
            p.setDither(true);
            p.setShader(r);
            Bitmap b = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            c.drawRect(new Rect(0,0,width,height),p);

            return b;
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        private ArrayList<SquidGifObject> return_squids(int layer){
            ArrayList<SquidGifObject> final_squids = new ArrayList<SquidGifObject>();

            for(int i = 0;i < squids.size();i++){
                if(squids.get(i).distance == layer){
                    final_squids.add(squids.get(i));
                }
            }

            return final_squids;
        }

        //Takes in the canvas as draws the given array
        private void draw_squid(Canvas c,ArrayList<SquidGifObject> squids){
            //Draw each layer based on how far away or close they are.
            for(int i = 0;i < squids.size();i++){
                //Move the squid to the bottom if they have gone too far.
                if(squids.get(i).y_pos < -500){
                    squids.get(i).y_pos = height + 500;
                    squids.get(i).generate_random_x();
                }

                c.drawBitmap(return_distance_scale(squids.get(i)),squids.get(i).x_pos,squids.get(i).y_pos,null);
                squids.get(i).y_pos -= squids.get(i).swim_speed;
            }
        }

        private Bitmap return_distance_scale(SquidGifObject o){
            Matrix m = new Matrix();
            m.setScale(o.scale,o.scale);

            Bitmap b = Bitmap.createBitmap(BackgroundSquids,0,0,BackgroundSquids.getWidth(),BackgroundSquids.getHeight(),m,true);

            return b;
        }

        private Bitmap return_eyeris(){

            return null;
        }
    }
}