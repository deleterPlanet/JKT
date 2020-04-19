package com.codji.justkilltime;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MoneyOpenGLRenderer implements GLSurfaceView.Renderer {

    private int k;

    private float theta,
            pi = (float)Math.PI,
            step = 360.0f/3,
            sw = 0.8f,
            angle = 0.0f;

    private float vertices[];

    private final String TAG = "MyTag";
    ByteBuffer byteBuffer;
    FloatBuffer vertexBuffer;


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.08f,0.09f,0.1f,1.0f);
        vertices = new float[9];
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glColor4f(0.0f, 1.0f, 0.0f, 1.0f);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        k = 0;
        for(float a = angle; a < 360.0f + angle; a += step) {
            theta = 2.0f * pi * a / 180.0f;
            vertices[k] = sw*(float)Math.cos(theta);
            vertices[k+1] = sw*(float)Math.sin(theta);
            vertices[k+2] = 0.0f;
            k += 3;
        }

        byteBuffer = ByteBuffer.allocateDirect(vertices.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES,0,vertices.length/2);

        angle++;

        gl.glLoadIdentity();
    }
}
