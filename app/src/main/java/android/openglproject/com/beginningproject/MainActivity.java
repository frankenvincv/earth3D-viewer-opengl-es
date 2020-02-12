package android.openglproject.com.beginningproject;



import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;

public class MainActivity extends Activity {

    private MyGLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
