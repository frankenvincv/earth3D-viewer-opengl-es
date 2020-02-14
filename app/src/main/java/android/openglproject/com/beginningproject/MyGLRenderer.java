package android.openglproject.com.beginningproject;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.content.Context;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private Context m_Context;

    private double time = 0;

    private PointLight m_PointLight;
    private Camera m_Camera;

    private int m_ViewPortWidth;
    private int m_ViewPortHeight;


    private ObjLoader m_ObjLoader;


    private float yaw;
    private float pitch;
    private float scaleOffset;
    private float minScale = 0.005f;
    private float maxScale = 0.035f;


    private Vector3 earthScale = new Vector3(0.03f, 0.03f, 0.03f);
    private Vector3 MoonScale = new Vector3(0.008f, 0.008f, 0.008f);

    private boolean isCreate = false;

    public boolean isCreate() {
        return isCreate;
    }

    public void setCreate(boolean create) {
        isCreate = create;
    }


    List<Object3d> listObj = new ArrayList<Object3d>();
    private boolean object1 = false;
    private boolean object2 = false;
    private boolean object3 = false;


    private Vector3 m_CubePositionDelta = new Vector3(0.1f, 0, 0.1f);

    public MyGLRenderer(Context context) {
        m_Context = context;
    }


    private Object3d createMoon(Context iContext) {
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
        Texture TexAndroid = new Texture(iContext, R.raw._moon);

        Texture[] CubeTex = new Texture[1];
        CubeTex[0] = TexAndroid;

        Material1.SetSpecularShininess(5f);

        Object3d moon = new Cube(iContext,
                CubeMesh,
                CubeTex,
                Material1,
                Shader);

        float random = (float) Math.random() * 360;

        float posX = (float) Math.cos(Math.toRadians(random)) * 25;
        float posZ = (float) Math.sin(Math.toRadians(random)) * 25;

        Log.v("pos", Float.toString(posX) + " ; " + Float.toString(posZ));
        Vector3 Position = new Vector3(posX, 0, posZ);

        moon.getOrientation().SetPosition(Position);
        moon.getOrientation().SetRotationAxis(new Vector3(1f, 1f, 1f));
        moon.getOrientation().SetScale(MoonScale);

        return moon;
    }

    private Object3d createEarth(Context iContext) {
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


        Object3d object = new Object3d(iContext,
                CubeMesh,
                CubeTex,
                Material1,
                Shader);

        // Set Intial Position and Orientation

        Vector3 Axis = new Vector3(1, 1, 1);
        Vector3 Position = new Vector3((float) Math.random() * 50 - 25, (float) Math.random() * 20 - 10, (float) Math.random() * 50 - 25);

        object.getOrientation().SetPosition(new Vector3(0,0,0));
        object.getOrientation().SetRotationAxis(Axis);
        object.getOrientation().SetScale(earthScale);

        return object;
    }


    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.83529f, 0.96078f, 0.89019f, 1.0f);
        m_PointLight = new PointLight(m_Context);
        SetupLights();
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


        // Camera and light move
        cameraAndLightUpdate(m_Camera, m_PointLight);


        // Generate object

        if (isObject1()) {
            listObj.add(createEarth(m_Context));
            setObject1(false);
        }
        if (isObject2()) {
            Object3d temp = createMoon(m_Context);
            temp.setMoon(true);
            listObj.add(temp);
            setObject2(false);
        }

        if (isObject3()) {
            setObject3(false);
            listObj.clear();
        }


        //drawing object
        for (Object3d object : listObj) {
            long time = SystemClock.uptimeMillis() % 10000L;
            float angle = 0.0360f * ((int) time);
            if (angle > 360.0f) {
                angle = 360.0f;
            }
            object.DrawObject(m_Camera, m_PointLight,angle,minScale,maxScale,this.getScaleOffset());

        }
        Log.v("list", String.valueOf(listObj.size()));
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

        if (pitch > 89.0f)
            this.pitch = 89.0f;
        if (pitch < -89.0f)
            this.pitch = -89.0f;
    }


    public float getScaleOffset() {
        return scaleOffset;
    }

    public void setScaleOffset(float _scaleOffset) {
        this.scaleOffset = _scaleOffset;
    }


    public boolean isObject1() {
        return object1;
    }

    public void setObject1(boolean object1) {
        this.object1 = object1;
    }

    public boolean isObject2() {
        return object2;
    }

    public void setObject2(boolean object2) {
        this.object2 = object2;
    }

    public boolean isObject3() {
        return object3;
    }

    public void setObject3(boolean object3) {
        this.object3 = object3;
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
        float Projfar = 200; //100;

        m_Camera = new Camera(m_Context,
                Eye,
                Center,
                Up,
                Projleft, Projright,
                Projbottom, Projtop,
                Projnear, Projfar);
    }

    void cameraAndLightUpdate(Camera camera, PointLight pointLight) {

        final float radius = 80.0f;

        float camX = (float) (Math.cos(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)) * radius);
        float camY = (float) (Math.sin(Math.toRadians(this.pitch)) * radius);
        float camZ = (float) (Math.sin(Math.toRadians(this.yaw)) * Math.cos(Math.toRadians(this.pitch)) * radius);


        Vector3 Position = camera.GetOrientation().GetPosition();
        Position.Set(new Vector3(camX, camY, camZ));

        pointLight.GetPosition().Set(new Vector3(camX * 3, camY * 2, camZ * 3));

        camera.UpdateCamera();
    }


}

