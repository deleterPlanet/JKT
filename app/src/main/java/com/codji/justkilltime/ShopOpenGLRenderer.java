package com.codji.justkilltime;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.widget.Button;

import androidx.viewpager.widget.ViewPager;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static com.codji.justkilltime.ShoopActivity.playerColor;

public class ShopOpenGLRenderer implements GLSurfaceView.Renderer {

    public static int frameNum = 0;
    private float playerSpeed = 0.01f,
            rad = 0.08f,
            screeWidth = 1.6f;
    private final String TAG = "MyTag";
    ByteBuffer byteBuffer;
    FloatBuffer vertexBufferPlayer;
    ViewPager viewPager;

    ShopOpenGLRenderer(ViewPager viewPager){
        this.viewPager = viewPager;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.08f,0.09f,0.1f,1.0f);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glScalef(1.0f,ShoopActivity.scaleYShop,1.0f); //сжатие оси oy

        spanPlayerZone(gl); //отрисовка игровой зоны
        drawPlayer(gl); //отрисовка игрока

        gl.glLoadIdentity();

        checkViewPagerPos();
    }

    void drawPlayer(GL10 gl){
        float theta;
        float pi = (float)Math.PI;
        float step = 6.0f;
        float verticesPlayer[] = new float[Math.round(360/step*3)+3];
        verticesPlayer[0] = playerSpeed*frameNum; verticesPlayer[1] = 0.0f; verticesPlayer[2] = 0.0f;
        int k = 3;
        for(float a = 0.0f; a < 360.0f; a += step) {
            theta = 2.0f * pi * a / 180.0f;

            verticesPlayer[k] = rad*(float)Math.cos(theta) + playerSpeed*frameNum;
            verticesPlayer[k+1] = rad*(float)Math.sin(theta);
            verticesPlayer[k+2] = 0.0f;
            k += 3;
        }
        byteBuffer = ByteBuffer.allocateDirect(verticesPlayer.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBufferPlayer = byteBuffer.asFloatBuffer();
        vertexBufferPlayer.put(verticesPlayer);
        vertexBufferPlayer.position(0);

        gl.glColor4f(playerColor[0],playerColor[1],playerColor[2],playerColor[3]);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBufferPlayer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,verticesPlayer.length/3);

        if (playerSpeed*frameNum > screeWidth/2){
            ShoopActivity.direction = -1;
        }
        if (playerSpeed*frameNum < -screeWidth/2){
            ShoopActivity.direction = 1;
        }
        frameNum += ShoopActivity.direction;
    }

    void spanPlayerZone(GL10 gl){
        gl.glColor4f(0.5f,0.5f,0.5f,1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        float theta;
        float pi = (float)Math.PI;
        float step = 6.0f;

        float verticesPlayerZoneLeft[] = new float[Math.round(360/step*3)+3];
        verticesPlayerZoneLeft[0] = -screeWidth/2; verticesPlayerZoneLeft[1] = 0.0f; verticesPlayerZoneLeft[2] = 0.0f;

        float verticesPlayerZoneRight[] = new float[Math.round(360/step*3)+3];
        verticesPlayerZoneRight[0] = screeWidth/2; verticesPlayerZoneRight[1] = 0.0f; verticesPlayerZoneRight[2] = 0.0f;
        int k = 3;
        for(float a = 0.0f; a < 360.0f; a += step) {
            theta = 2.0f * pi * a / 180.0f;

            verticesPlayerZoneLeft[k] = rad*(float)Math.cos(theta) - screeWidth/2 - playerSpeed;
            verticesPlayerZoneLeft[k+1] = rad*(float)Math.sin(theta);
            verticesPlayerZoneLeft[k+2] = 0.0f;

            verticesPlayerZoneRight[k] = rad*(float)Math.cos(theta) + screeWidth/2 + playerSpeed;
            verticesPlayerZoneRight[k+1] = rad*(float)Math.sin(theta);
            verticesPlayerZoneRight[k+2] = 0.0f;

            k += 3;
        }

        //отрисовка левого края игровой зоны
        byteBuffer = ByteBuffer.allocateDirect(verticesPlayerZoneLeft.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBufferPlayer = byteBuffer.asFloatBuffer();
        vertexBufferPlayer.put(verticesPlayerZoneLeft);
        vertexBufferPlayer.position(0);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBufferPlayer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,verticesPlayerZoneLeft.length/3);

        //отрисовка правого края игровой зоны
        byteBuffer = ByteBuffer.allocateDirect(verticesPlayerZoneRight.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBufferPlayer = byteBuffer.asFloatBuffer();
        vertexBufferPlayer.put(verticesPlayerZoneRight);
        vertexBufferPlayer.position(0);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBufferPlayer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN,0,verticesPlayerZoneRight.length/3);

        //отрисовка центральной части игровой зоны
        float verticesPlayerZoneCenter[] = new float[]{
                -screeWidth/2 - playerSpeed, rad, 0.0f,
                screeWidth/2 + playerSpeed, rad, 0.0f,
                screeWidth/2 + playerSpeed, -rad, 0.0f,
                -screeWidth/2 - playerSpeed, -rad, 0.0f,
                -screeWidth/2 - playerSpeed, rad, 0.0f
        };

        byteBuffer = ByteBuffer.allocateDirect(verticesPlayerZoneCenter.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBufferPlayer = byteBuffer.asFloatBuffer();
        vertexBufferPlayer.put(verticesPlayerZoneCenter);
        vertexBufferPlayer.position(0);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBufferPlayer);
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,0,verticesPlayerZoneCenter.length/3);
    }

    void checkViewPagerPos(){
        ShoopActivity.handler.sendEmptyMessage(viewPager.getCurrentItem());
    }
}
