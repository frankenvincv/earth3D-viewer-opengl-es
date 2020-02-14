package android.openglproject.com.beginningproject;



import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyGLSurfaceView V = (MyGLSurfaceView)this.findViewById (R.id.gl_SurfaceView);
        mGLView = V;

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGLView.renderer.isObject1()) {
                    mGLView.renderer.setObject1(true);
                }


            }
        });
        findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mGLView.renderer.isObject2()){
                    mGLView.renderer.setObject2(true);

                }

            }
        });
        findViewById(R.id.button_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGLView.renderer.isObject3()){
                    mGLView.renderer.setObject3(!mGLView.renderer.isObject3());
                }else{
                    mGLView.renderer.setObject3(!mGLView.renderer.isObject3());
                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //mGLView.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
       // mGLView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main,menu);
        return true;
    }

    ///////////////////////////////////////////////////////////////
}
