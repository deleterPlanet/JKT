package com.codji.justkilltime;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Date;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class OpenGLRenderer implements GLSurfaceView.Renderer {

    private long startGodMod, startSecondChanceDialog, randDegTime, maxRandDegTime = 5000;
    private int triangleCount = 6,
            frameNum = 0,
            score = 0;
    private float bottomLine = -0.25f,
            screenHeight = 1.3f,
            step = 0.5f,
            speedY = 0.01f,
            playerSpeed = 0.01f,
            rad = 0.08f,
            maxAngle = 0.008f,
            tellestTrianglePos,
            minSw = 0.001f,
            screeWidth = 1.6f,
            angleMap = 0.0f,
            newAngleMap,
            playerVertices;
    private float vertices[][],
            centers[][];
    private float verticesGreenTriangle[],
            speedX[],
            angles[],
            playerColor[] = new float[4],
            emptyTrianglesColor[] = new float[4],
            sw[];
    private boolean ISGreenTriangle = false,
            ISGetGreenTriangle = false,
            ISDeath = false,
            IS0_360 = false,
            ISRandDeg = false;
    private final String TAG = "MyTag";
    ByteBuffer byteBuffer;
    FloatBuffer vertexBuffer,
            vertexBufferPlayer;

    OpenGLRenderer(float[] pColors, float angleMap, float[] trianglesColor, float playerVertices){
        playerColor[0] = pColors[0];
        playerColor[1] = pColors[1];
        playerColor[2] = pColors[2];
        playerColor[3] = pColors[3];
        this.angleMap = angleMap;

        startGodMod = new Date().getTime() - 4000;

        emptyTrianglesColor[0] = trianglesColor[0];
        emptyTrianglesColor[1] = trianglesColor[1];
        emptyTrianglesColor[2] = trianglesColor[2];
        emptyTrianglesColor[3] = trianglesColor[3];

        this.playerVertices = playerVertices;

        IS0_360 = angleMap == 360.0f;
        ISRandDeg = angleMap == -1.0f;
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glClearColor(0.08f,0.09f,0.1f,1.0f);

        if (ISRandDeg){
            randDegTime = new Date().getTime();
            angleMap = newAngleMap = 0.0f;
        }

        speedX = new float[triangleCount + 1];
        angles = new float[triangleCount + 1];
        centers = new float[triangleCount + 1][];
        sw = new float[triangleCount + 1];

        vertices = new float[triangleCount][];
        for (int i = 0; i < triangleCount; i++){
            centers[i] = (i == 0)? newTriangle(screenHeight - step, i) :  newTriangle(centers[i-1][1], i);
            vertices[i] = TurnTriangle(centers[i], angles[i], i);
            sw[i] = 0.1f;
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        setAngleMap(gl);

        if(ISDeath){
            death();
            rad -= 0.002;
            if (rad < minSw){
                if(PlayActivity.ISSecondChance){PlayActivity.endGameHandler.sendEmptyMessage(PlayActivity.END_GAME_ID);}
                return;
            }
        }

        spanPlayerZone(gl); //отрисовка игровой зоны
        drawPlayer(gl); //отрисовка игрока
        spanGreenTriangle(); //инициализация зелённого треугольника (если его нет и если повезёт)
        DrawGreenTriangle(gl); //отрисовка зелённого треугольника если он инициализирован
        DrawEmptyTriangles(gl); //отрисовка вражеских треугольников

        PlayActivity.timerHandler.sendEmptyMessage((int)(new Date().getTime() - startGodMod));

        gl.glLoadIdentity();
    }

    void drawTriangle(GL10 gl, float coord[], float color[]){
        byteBuffer = ByteBuffer.allocateDirect(coord.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuffer.asFloatBuffer();
        vertexBuffer.put(coord);
        vertexBuffer.position(0);

        gl.glColor4f(color[0], color[1], color[2], color[3]);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertexBuffer);
        gl.glDrawArrays(GL10.GL_TRIANGLES,0,coord.length/2);
    }

    float[] newTriangle(float lastY, int pos){
        lastY = (lastY >= screenHeight)? lastY : screenHeight;
        float randX = (float)(Math.random()*screeWidth - screeWidth/2);
        float randAngle = (float)Math.random()*maxAngle - maxAngle/screeWidth*(randX + screeWidth/2);
        tellestTrianglePos = lastY + step;
        speedX[pos] = randAngle;
        angles[pos] = 0.0f;
        sw[pos] = 0.1f;
        return new float[]{
                randX, lastY + step, 0.0f
        };
    }

    void spanGreenTriangle(){
        if (!ISGreenTriangle){
            if (Math.random() < 2.0/triangleCount) {
                float randX = (float)(Math.random()*1.5f - 0.75f);
                float randAngle = (float)Math.random()*maxAngle - maxAngle/screeWidth*(randX + screeWidth/2);
                speedX[speedX.length-1] = randAngle;
                angles[angles.length-1] = 0.0f;
                sw[sw.length-1] = 0.1f;

                centers[centers.length-1] = new float[]{
                        randX, tellestTrianglePos + step, 0.0f
                };
                ISGreenTriangle = true;
            }
        }
    }

    void drawPlayer(GL10 gl){
        float theta;
        float pi = (float)Math.PI;
        float step = 360.0f/(playerVertices*2);
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
            PlayActivity.direction = -1;
        }
        if (playerSpeed*frameNum < -screeWidth/2){
            PlayActivity.direction = 1;
        }
        frameNum += PlayActivity.direction;
    }

    void checkCollTriangle(float coord[], boolean ISItGreenTriangle){
        float newCoord[] = new float[]{ // Центры сторон тр-ка
                (coord[0]+coord[3])/2, (coord[1]+coord[4])/2,
                (coord[0]+coord[6])/2, (coord[1]+coord[7])/2,
                (coord[3]+coord[6])/2, (coord[4]+coord[7])/2
        };
        if (((coord[0] < playerSpeed*frameNum+rad*0.75 && coord[0] > playerSpeed*frameNum-rad*0.5 && coord[1] > -rad*0.5 && coord[1] < rad*0.5)
        || (coord[3] < playerSpeed*frameNum+rad*0.75 && coord[3] > playerSpeed*frameNum-rad*0.5 && coord[4] > -rad*0.5 && coord[4] < rad*0.5)
        || (coord[6] < playerSpeed*frameNum+rad*0.75 && coord[6] > playerSpeed*frameNum-rad*0.5 && coord[7] > -rad*0.5 && coord[7] < rad*0.5)
        || (newCoord[0] < playerSpeed*frameNum+rad*0.75 && newCoord[0] > playerSpeed*frameNum-rad*0.5 && newCoord[1] > -rad*0.5 && newCoord[1] < rad*0.5)
        || (newCoord[2] < playerSpeed*frameNum+rad*0.75 && newCoord[2] > playerSpeed*frameNum-rad*0.5 && newCoord[3] > -rad*0.5 && newCoord[3] < rad*0.5)
        || (newCoord[4] < playerSpeed*frameNum+rad*0.75 && newCoord[4] > playerSpeed*frameNum-rad*0.5 && newCoord[5] > -rad*0.5 && newCoord[5] < rad*0.5))
        && new Date().getTime() - startGodMod > 3000){
            if(ISItGreenTriangle){
                if(!ISGetGreenTriangle){
                    ISGetGreenTriangle = true;
                    score++;
                    PlayActivity.handler.sendEmptyMessage(score);

                    startGodMod = new Date().getTime();
                }
            }else{
                death();
            }
        }
    }

    float[] TurnTriangle(float coord[], float angle, int pos){
        float theta;
        float pi = (float)Math.PI;
        float step = 360.0f/3;
        int k = 0;
        float vertices[] = new float[9];
        for(float a = angle; a < 360.0f + angle; a += step) {
            theta = 2.0f * pi * a / 180.0f;

            vertices[k] = coord[0] + sw[pos]*(float)Math.cos(theta);
            vertices[k+1] = coord[1] + sw[pos]*(float)Math.sin(theta);
            vertices[k+2] = coord[2] + 0.0f;
            k += 3;
        }
        return vertices;
    }

    void toSmalTriangle(int pos){
        sw[pos] -= 0.005f;
        if (sw[pos] < minSw){
            if (pos == sw.length-1){
                if (ISGetGreenTriangle){
                    ISGetGreenTriangle = false;
                }
                verticesGreenTriangle = new float[]{};
                ISGreenTriangle = false;
            }else{
                if (ISDeath){centers[pos] = null; vertices[pos] = new float[]{};}
                else {
                    centers[pos] = (pos == 0) ? newTriangle(screenHeight - step, pos) : newTriangle(centers[pos - 1][1], pos);
                    vertices[pos] = TurnTriangle(centers[pos], angles[pos], pos);
                }
            }
        }
    }

    void DrawGreenTriangle(GL10 gl){
        if (ISGreenTriangle){
            verticesGreenTriangle = TurnTriangle(centers[centers.length-1], angles[angles.length-1], sw.length-1);
            drawTriangle(gl, verticesGreenTriangle, new float[]{0.0f, 1.0f, 0.0f, 1.0f});

            if (speedX[speedX.length-1] > 0.0f){
                angles[angles.length-1] -= 0.5f;
            }else{
                angles[angles.length-1] += 0.5f;
            }

            checkCollTriangle(verticesGreenTriangle, true);

            if (ISGetGreenTriangle){
                toSmalTriangle(sw.length-1);
            }
            centers[centers.length-1][1] -= speedY;
            centers[centers.length-1][0] += speedX[speedX.length-1];
            if (centers[centers.length-1][1] < bottomLine || ISDeath){
                toSmalTriangle(sw.length-1);
            }
        }
    }

    void DrawEmptyTriangles(GL10 gl){
        for (int i = 0; i < vertices.length; i++){
            if (centers[i] == null){continue;}
            vertices[i] = TurnTriangle(centers[i], angles[i], i);
            checkCollTriangle(vertices[i], false);
            drawTriangle(gl, vertices[i], new float[]{emptyTrianglesColor[0], emptyTrianglesColor[1], emptyTrianglesColor[2], emptyTrianglesColor[3]} );
            if (speedX[i] > 0.0f){
                angles[i] -= 0.5f;
            }else{
                angles[i] += 0.5f;
            }
            centers[i][1] -= speedY;
            centers[i][0] += speedX[i];
            if (vertices[i][1] < bottomLine || ISDeath){
                toSmalTriangle(i);
            }
        }
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

    void setAngleMap(GL10 gl){
        gl.glScalef(1.0f,PlayActivity.scaleY,1.0f);
        gl.glRotatef(angleMap, 0.0f, 0.0f, 1.0f);

        if (IS0_360){
            angleMap += 0.5f;
            return;
        }

        if(ISRandDeg){
            if(new Date().getTime() - randDegTime >= maxRandDegTime){
                newAngleMap = Math.round(Math.random()*3)*90.0f;
                randDegTime = new Date().getTime();
            }else{
                if (Math.sqrt((newAngleMap - angleMap)*(newAngleMap - angleMap)) < 5.0f){
                    angleMap = newAngleMap;
                    return;
                }
                angleMap += (newAngleMap < angleMap)? -5.0f : 5.0f;
            }
        }

        if ((angleMap == 90.0f || angleMap == 270.0f) && !ISRandDeg){
            gl.glScalef(PlayActivity.scaleY,PlayActivity.scaleY,1.0f);
            screeWidth = 3.0f;
            triangleCount = 15;
        }
    }

    void death(){
        if(!ISDeath) {
            if (!PlayActivity.ISSecondChance){
                PlayActivity.endGameHandler.sendEmptyMessage(PlayActivity.SHOW_DIALOG);
            }
            startSecondChanceDialog = new Date().getTime();
            ISDeath = true;
        }
        if (new Date().getTime() - startSecondChanceDialog > 5000){PlayActivity.endGameHandler.sendEmptyMessage(PlayActivity.END_GAME_ID);}
    }
}
