package android.openglproject.com.beginningproject;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

public class MyGLSurfaceView extends GLSurfaceView {

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320.0f;

    private float firstDistance;
    private float previousX;
    private float previousY;


    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context);

        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        int index = e.getActionIndex();


        if (e.getPointerCount() > 1) {
            float[] xPos = new float[2];
            float[] yPos = new float[2];

            xPos[0] = e.getX();
            yPos[0] = e.getY();
            float distance =0;

            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN: {
                    xPos[1] = e.getX(index);
                    yPos[1] = e.getY(index);
                    firstDistance = (float) Math.sqrt( Math.abs(xPos[1] - xPos[0]) + Math.abs(yPos[1] - yPos[0]));

                    break;
                }
                case MotionEvent.ACTION_MOVE: { // a pointer was moved
                    for (int i = 0; i < e.getPointerCount(); i++) {
                        xPos[i] = e.getX(e.getPointerId(i));
                        yPos[i] = e.getY(e.getPointerId(i));
                    }
                    distance = (float) Math.sqrt(Math.abs(xPos[1] - xPos[0]) + Math.abs(yPos[1] - yPos[0]));
                    if (distance > firstDistance){
                        distance -= firstDistance;
                    }
                    else {
                        distance = (firstDistance - distance) * -1;
                    }


                    renderer.setScaleOffset(distance * 0.000090f);


                    requestRender();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL: {
                    firstDistance = 0;
                    distance = 0;
                    break;
                }
            }
            //Log.v("Offset", Float.toString((float) distance * 0.00050f));

        } else {
            float x = e.getX();
            float y = e.getY();



            switch (e.getActionMasked()) {
                case MotionEvent.ACTION_MOVE: {

                    float dx = (x - previousX);
                    float dy = (y - previousY);

//                if (y > getHeight() /2){
//                    dx = dx * -1;
//                }
//
//                if (x < getWidth() /2){
//                    dy = dy * -1;
//                }


                    renderer.setYaw((dx) * TOUCH_SCALE_FACTOR);
                    renderer.setPitch((dy) * TOUCH_SCALE_FACTOR);



                /*mRenderer.setmMotionX(
                        mRenderer.getmMotionX() +
                                (dx) * TOUCH_SCALE_FACTOR
                );
                mRenderer.setmMotionY(
                        mRenderer.getmMotionY() +
                                (dy) * TOUCH_SCALE_FACTOR
                );*/

//                    Log.v("Touch Point", Float.toString(dx) + " ; " + Float.toString(dy));
                    requestRender();
                    break;
                }
            }

            previousX = x;
            previousY = y;
        }

        invalidate();

        return true;
    }
}
