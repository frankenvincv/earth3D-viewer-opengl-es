package android.openglproject.com.beginningproject;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import android.content.Context;
import android.opengl.GLU;


public class Object3d 
{


	private Orientation	m_Orientation = null;
	
	private Context		m_Context	= null;
	//private Mesh 		m_Mesh 		= null;
	private MeshEx		m_MeshEx	= null;
	private Texture[] 	m_Textures 	= null;
	private Material 	m_Material 	= null;
	private Shader		m_Shader	= null;
	
	private float[] m_MVPMatrix = new float[16];
	private float[] m_ModelMatrix = new float[16];
	private float[] m_ModelViewMatrix = new float[16];
	private float[] m_NormalMatrix = new float[16];
	private float[] m_NormalMatrixInvert = new float[16];
	
	private int m_PositionHandle;   
	private int m_TextureHandle;
	private int m_NormalHandle;
	   
	private int m_ActiveTexture = -1;
	
	// Texture Animation Control
	private boolean m_AnimateTextures 	= false;
	private int 	m_StartTexAnimNum 	= 0;
	private int 	m_StopTexAnimNum 	= 0;
	private float 	m_TexAnimDelay 		= 0; // in seconds
	private float 	m_TargetTime 		= 0;
	private float 	m_Counter 			= 0;


	//check visible
    private boolean m_Visible = true;


    //type
	private boolean isMoon = false;

	public boolean isMoon() {
		return isMoon;
	}

	public void setMoon(boolean moon) {
		isMoon = moon;
	}

	//scale error
	private float errorVale = 0.000000001f;


    Object3d(Context iContext,
             //Mesh iMesh,
             MeshEx iMeshEx,
             Texture[] iTextures,
             Material iMaterial,
             Shader iShader)
	{	
		m_Context	= iContext;
		//m_Mesh 		= iMesh;
		m_MeshEx	= iMeshEx;
		m_Textures 	= iTextures;
		m_Material 	= iMaterial;
		m_Shader	= iShader;
		
		if (m_Textures == null)
		{
			m_ActiveTexture = -1;
		}
		else
		{
			m_ActiveTexture = 0;
		}
		
		m_Orientation = new Orientation(m_Context);	
	}

	public Object3d(Object3d another) {
		this(another.m_Context,another.m_MeshEx,another.m_Textures,another.m_Material,another.m_Shader);

	}

	public void CheckGLError(String glOperation)
	{
		int error;
	    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) 
	    {
	    	Log.e("class Object3d :", glOperation + " IN CHECKGLERROR() : glError " + GLU.gluErrorString(error));
	        throw new RuntimeException(glOperation + ": glError " + error);
	    }
	}
	     
	boolean SetTexture(int TextureNumber)
	{
		if (TextureNumber < m_Textures.length)
		{
			m_ActiveTexture = TextureNumber;
			return true;
		}
		
		return false;
	}
	
	Texture GetTexture(int TextureNumber)
	{
		if (TextureNumber < m_Textures.length)
		{
			return m_Textures[TextureNumber];
		}
		else
		{
			return null;
		}
	}
	
	void ActivateTexture()
	{
		if (m_ActiveTexture >= 0)
		{	 
			// Activate Texture Unit
			Texture.SetActiveTextureUnit(GLES20.GL_TEXTURE0);
			CheckGLError("glActiveTexture - SetActiveTexture Unit Failed");  
			 
			if ((m_AnimateTextures)&& (m_Counter >= m_TargetTime))
			{
				m_ActiveTexture++;
				if (m_ActiveTexture > m_StopTexAnimNum)
				{
					m_ActiveTexture = m_StartTexAnimNum; 
				}
				m_TargetTime = m_Counter + m_TexAnimDelay;
			}
			m_Counter = SystemClock.uptimeMillis() * 1/1000.0f; // Convert to seconds
		
			
			// Activates texture for this object
			m_Textures[m_ActiveTexture].ActivateTexture();
		}
	}
	 
	void GetVertexAttribInfo()
	{ 	
		// Set up Vertex Position Data
        m_PositionHandle = m_Shader.GetShaderVertexAttributeVariableLocation("aPosition");
        CheckGLError("glGetAttribLocation - aPosition");     
        
        // Set up Vertex Texture Data
        m_TextureHandle = m_Shader.GetShaderVertexAttributeVariableLocation("aTextureCoord");
        CheckGLError("glGetAttribLocation - aTextureCoord");     
        
        // Get Handle to Normal Vertex Attribute Variable
    	m_NormalHandle = m_Shader.GetShaderVertexAttributeVariableLocation("aNormal");	
    	CheckGLError("glGetAttribLocation - aNormal");     
	}

	
	 void SetLighting(Camera Cam, 
			 		  PointLight light, 
				      float[] ModelMatrix, 
				      float[] ViewMatrix, 
				      float[] ModelViewMatrix,
				      float[] NormalMatrix)
	 {
		 float[] AmbientColor		= light.GetAmbientColor();
		 float[] DiffuseColor 		= light.GetDiffuseColor();
		 float[] SpecularColor 		= light.GetSpecularColor();
		 float	SpecularShininess 	= light.GetSpecularShininess();

		 Vector3 EyePos = Cam.GetCameraEye();

		 m_Shader.SetShaderUniformVariableValue("uLightAmbient", AmbientColor);	
		 m_Shader.SetShaderUniformVariableValue("uLightDiffuse", DiffuseColor);   	
		 m_Shader.SetShaderUniformVariableValue("uLightSpecular", SpecularColor);    	
		 m_Shader.SetShaderUniformVariableValue("uLightShininess", SpecularShininess);  	
		 m_Shader.SetShaderUniformVariableValue("uWorldLightPos", light.GetPosition());  	
		 
		 m_Shader.SetShaderUniformVariableValue("uEyePosition", EyePos);

		 m_Shader.SetShaderVariableValueFloatMatrix4Array("NormalMatrix", 1, false, NormalMatrix, 0);	
		 m_Shader.SetShaderVariableValueFloatMatrix4Array("uModelMatrix", 1, false, ModelMatrix, 0);	
		 m_Shader.SetShaderVariableValueFloatMatrix4Array("uViewMatrix", 1, false, ViewMatrix, 0);	
		 m_Shader.SetShaderVariableValueFloatMatrix4Array("uModelViewMatrix", 1, false, ModelViewMatrix, 0);	 
 
		 // Set object lighting Material Properties in shader
		 if (m_Material != null)
		 {
			 m_Shader.SetShaderUniformVariableValue("uMatEmissive", m_Material.GetEmissive());
			 m_Shader.SetShaderUniformVariableValue("uMatAmbient", m_Material.GetAmbient());
			 m_Shader.SetShaderUniformVariableValue("uMatDiffuse", m_Material.GetDiffuse());
			 m_Shader.SetShaderUniformVariableValue("uMatSpecular", m_Material.GetSpecular());
			 m_Shader.SetShaderUniformVariableValue("uMatAlpha", m_Material.GetAlpha());
		 }
	 }
	  
	 void GenerateMatrices(Camera  	Cam, 
			 			   Vector3 	iPosition,
			 			   Vector3  iRotationAxis,
			 			   Vector3 	iScale)
	 {
		 // Initialize Model Matrix
	     Matrix.setIdentityM(m_ModelMatrix, 0);  
		 
		 m_Orientation.SetPosition(iPosition);
	     m_Orientation.SetRotationAxis(iRotationAxis);
	     m_Orientation.SetScale(iScale);
	    
	     m_ModelMatrix = m_Orientation.UpdateOrientation();
	          
	     // Create Model View Matrix
	     Matrix.multiplyMM(m_ModelViewMatrix, 0, Cam.GetViewMatrix(), 0, m_ModelMatrix, 0);

	     // Create Normal Matrix for lighting	      
	     Matrix.multiplyMM(m_NormalMatrix, 0, Cam.GetViewMatrix(), 0, m_ModelMatrix, 0);
	     Matrix.invertM(m_NormalMatrixInvert, 0, m_NormalMatrix, 0);
	     Matrix.transposeM(m_NormalMatrix, 0, m_NormalMatrixInvert, 0);
	       
	     // Create Model View Projection Matrix
	     Matrix.multiplyMM(m_MVPMatrix, 0, Cam.GetProjectionMatrix(), 0, m_ModelViewMatrix, 0);      
	 }
	 
	void DrawObject(Camera 		Cam,
					PointLight 	light,
					Vector3 	iPosition,
		 			Vector3  	iRotationAxis,
		 			Vector3 	iScale)
	{
		// Activate and set up the Shader and Draw Object's mesh
	   
		// Generate Needed Matrices for Object
		GenerateMatrices(Cam, 
	 			   		 iPosition,
	 			   		 iRotationAxis,
	 			   		 iScale);
		
	    // Add program to OpenGL environment
		m_Shader.ActivateShader(); 
	    	
	    // Get Vertex Attribute Info in preparation for drawing the mesh
		GetVertexAttribInfo();
	    
	    // Sets up the lighting parameters for this object
	    SetLighting(Cam, light, m_ModelMatrix, Cam.GetViewMatrix(), m_ModelViewMatrix, m_NormalMatrix);
	              
	    // Apply the projection and view transformation matrix to the shader
	    m_Shader.SetShaderVariableValueFloatMatrix4Array("uMVPMatrix", 1, false, m_MVPMatrix, 0); 
	                
	    // Activates texture for this object
	    ActivateTexture();
	    
	    // Enable Hidden surface removal
	    GLES20.glEnable(GLES20.GL_DEPTH_TEST);	
	     
	    if (m_MeshEx != null)
	    {
	    	m_MeshEx.DrawMesh(m_PositionHandle, m_TextureHandle, m_NormalHandle);
	    } 
	    else
	    {
	    	Log.d("class Object3d :", "No MESH in Object3d");
	    }
	  	  
	}


	void DrawObject(Camera Cam, PointLight light,float angle, float minScale,float maxScale, float scaleOffset)
	{

	    if (this.isMoon()){
	        this.moonBehavior(angle);
        }
	    else{
	        this.earthBehavior(minScale,maxScale,scaleOffset);
        }

		//Log.d("TEST :", (m_Orientation.GetPosition().toString()));


        if (isVisible()){
            DrawObject(Cam,
                    light,
                    m_Orientation.GetPosition(),
                    m_Orientation.GetRotationAxis(),
                    m_Orientation.GetScale());
        }


	}


    public boolean isVisible() {
        return m_Visible;
    }

    public void setVisible(boolean m_Visible) {
        this.m_Visible = m_Visible;
    }

	void earthBehavior(float minScale, float maxScale, float scaleOffset){

        this.m_Orientation.AddRotationY(0.6f);

    	/*Vector3 scaleV = this.m_Orientation.GetScale();
		if (scaleV.inRange(minScale, maxScale)) {
			scaleV.AddScale(scaleOffset);
			this.m_Orientation.SetScale(scaleV);
		} else {
			if (Math.abs(maxScale - scaleV.Length()) > Math.abs(minScale - scaleV.Length())) {
				scaleV.Set(minScale + errorVale, minScale + errorVale, minScale + errorVale);
			} else {
				scaleV.Set(maxScale - errorVale * 2, maxScale - errorVale * 2, maxScale - errorVale * 2);
			}
		}*/
	}

	void moonBehavior(float angle){

		float posX = (float) (Math.cos(Math.toRadians(angle)) * 20f);
		float posZ = (float) (Math.sin(Math.toRadians(angle)) * 20f);
		this.getOrientation().GetPosition().Set(posX, 0, posZ);
		this.getOrientation().AddRotationY(0.3f);
	}


	public Orientation getOrientation() {
		return m_Orientation;
	}

	public void setOrientation(Orientation m_Orientation) {
		this.m_Orientation = m_Orientation;
	}
	
}
