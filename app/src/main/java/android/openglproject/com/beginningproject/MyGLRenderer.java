package android.openglproject.com.beginningproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Context m_Context;

    private double time = 0;

    private PointLight m_PointLight;
    private Camera m_Camera;

    private int m_ViewPortWidth;
    private int m_ViewPortHeight;

    private Cube m_Cube;
    private ObjLoader m_ObjLoader;



    private float yaw;
    private float pitch;
    private float scaleOffset;
    private float minScale = 0.005f;
    private float maxScale = 0.055f;
    private float errorVale = 0.000000001f;

    private Vector3 scaleV = new Vector3(0.025f, 0.025f,0.025f);



    private Vector3 m_CubePositionDelta = new Vector3(0.1f, 0, 0.1f);

    public MyGLRenderer(Context context) {
        m_Context = context;
    }

    void SetupLights() {
        // Set Light Characteristics
        Vector3 LightPosition = new Vector3(0, 20, 100);

        float[] AmbientColor = new float[3];
        AmbientColor[0] = 0.1f;
        AmbientColor[1] = 0.1f;
        AmbientColor[2] = 0.1f;

        float[] DiffuseColor = new float[3];
        DiffuseColor[0] = 1.0f;
        DiffuseColor[1] = 1.0f;
        DiffuseColor[2] = 1.0f;

        float[] SpecularColor = new float[3];
        SpecularColor[0] = 1.0f;
        SpecularColor[1] = 1.0f;
        SpecularColor[2] = 1.0f;

        m_PointLight.SetPosition(LightPosition);
        m_PointLight.SetAmbientColor(AmbientColor);
        m_PointLight.SetDiffuseColor(DiffuseColor);
        m_PointLight.SetSpecularColor(SpecularColor);
    }

    void SetupCamera() {
        // Set Camera View
        Vector3 Eye = new Vector3(0, 0, 8);
        Vector3 Center = new Vector3(0, 0, -1);
        Vector3 Up = new Vector3(0, 1, 0);

        float ratio = (float) m_ViewPortWidth / m_ViewPortHeight;
        float Projleft = -ratio;
        float Projright = ratio;
        float Projbottom = -1;
        float Projtop = 1;
        float Projnear = 3;
        float Projfar = 100; //100;

        m_Camera = new Camera(m_Context,
                Eye,
                Center,
                Up,
                Projleft, Projright,
                Projbottom, Projtop,
                Projnear, Projfar);
    }

    void CreateCube(Context iContext) {
        //Create Cube Shader

        m_ObjLoader = new ObjLoader(iContext, "earth/earth.obj");

        Shader Shader = new Shader(iContext, R.raw.light_vertex_shader, R.raw.light_fragment_shader);    // ok

        //MeshEx(int CoordsPerVertex,
        //		int MeshVerticesDataPosOffset,
        //		int MeshVerticesUVOffset ,
        //		int MeshVerticesNormalOffset,
        //		float[] Vertices,
        //		short[] DrawOrder
        MeshEx CubeMesh = new MeshEx(8, 0, 3, 5, m_ObjLoader.VertexCoordinate, Cube.CubeDrawOrder);

        // Create Material for this object
        Material Material1 = new Material();
        //Material1.SetEmissive(0, 0.4f, 0.4f);


        // Create Texture
        Texture TexAndroid = new Texture(iContext, R.raw._4096_earth);

        Texture[] CubeTex = new Texture[1];
        CubeTex[0] = TexAndroid;


        m_Cube = new Cube(iContext,
                CubeMesh,
                CubeTex,
                Material1,
                Shader);

        // Set Intial Position and Orientation
        Vector3 Axis = new Vector3(1, 1, 1);
        Vector3 Position = new Vector3(0.0f, 0.0f, 0.0f);
        Vector3 Scale = new Vector3(1.0f, 1.0f, 1.0f);

        m_Cube.m_Orientation.SetPosition(Position);
        m_Cube.m_Orientation.SetRotationAxis(Axis);
        m_Cube.m_Orientation.SetScale(Scale);

        //m_Cube.m_Orientation.AddRotationZ(60);

    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.83529f, 0.96078f, 0.89019f, 1.0f);
        m_PointLight = new PointLight(m_Context);
        SetupLights();

        CreateCube(m_Context);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Ignore the passed-in GL10 interface, and use the GLES20
        // class's static methods instead.
        GLES20.glViewport(0, 0, width, height);

        m_ViewPortWidth = width;
        m_ViewPortHeight = height;

        SetupCamera();
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);


        // Camera Move demo

        final float radius = 30.0f;

        float camX = (float) (Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch))     * radius);
        float camY = (float) (Math.sin(Math.toRadians(this.pitch))                                          * radius);
        float camZ = (float) (Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch))     * radius);


        Vector3 Position = m_Camera.GetOrientation().GetPosition();
        Position.Set(new Vector3(camX, camY , camZ));

        m_PointLight.GetPosition().Set(new Vector3(camX * 3, camY * 2, camZ * 3));

        m_Camera.UpdateCamera();


        //m_Cube.m_Orientation.AddRotationY(-this.angleY);
        //m_Cube.m_Orientation.AddRotationX(-this.angleX);




        if (scaleV.inRange(minScale, maxScale)){
            scaleV.AddScale(this.getScaleOffset());
            m_Cube.m_Orientation.SetScale(scaleV);
        }else{
            if (Math.abs(maxScale - scaleV.Length()) > Math.abs(minScale - scaleV.Length())){
                scaleV.Set(minScale + errorVale,minScale + errorVale,minScale + errorVale);
            }
            else{
                scaleV.Set(maxScale - errorVale,maxScale - errorVale,maxScale - errorVale);
            }
        }

/*      Vector3 Position = m_Cube.m_Orientation.GetPosition();
        if (Position.z > 4 || Position.z < -4) {
            m_CubePositionDelta.Negate();
        }

        Vector3 NewPosition = Vector3.Add(Position, m_CubePositionDelta);

        Position.Set(NewPosition.x, NewPosition.y, NewPosition.z);*/
        m_Cube.DrawObject(m_Camera, m_PointLight);

        this.setScaleOffset(0.0f);

    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float _yaw) {
        this.yaw += _yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float _pitch) {

        this.pitch += _pitch;

        if(pitch > 89.0f)
            this.pitch =  89.0f;
        if(pitch < -89.0f)
            this.pitch = -89.0f;
    }


    public float getScaleOffset() {
        return scaleOffset;
    }

    public void setScaleOffset(float _scaleOffset) {
        this.scaleOffset = _scaleOffset;
    }

}

