package android.openglproject.com.beginningproject;


public class Material 
{
	private float[] m_Emissive	= new float[3];
	private float[] m_Ambient 	= new float[3];
	private float[]	m_Diffuse 	= new float[3];
	private float[]	m_Specular 	= new float[3];

	private float m_Specular_Shininess = 5.0f;
	private float m_Alpha = 1.0f;
	
	
	public Material()
	{
		m_Emissive[0] = 0.1f;
		m_Emissive[1] = 0.1f;
		m_Emissive[2] = 0.1f;
		
		m_Ambient[0] = 1f;
		m_Ambient[1] = 1f;
		m_Ambient[2] = 1f;
		
		m_Diffuse[0] = 0.5f;
		m_Diffuse[1] = 0.5f;
		m_Diffuse[2] = 0.5f;
				
		m_Specular[0]= 1.0f;
		m_Specular[1]= 1.0f;
		m_Specular[2]= 1.0f;

		m_Specular_Shininess = 2.0f;
		m_Alpha = 1;
	}
	
	public Material(float[] iEmissive, 
					float[] iAmbient, 
					float[] iDiffuse, 
					float[] iSpecular,
					float iSpecShininess, 
					float iAlpha)
	{
		
		m_Emissive[0] = iEmissive[0];
		m_Emissive[1] = iEmissive[1];
		m_Emissive[2] = iEmissive[2];
		
		m_Ambient[0] = iAmbient[0];
		m_Ambient[1] = iAmbient[1];
		m_Ambient[2] = iAmbient[2];
		
		m_Diffuse[0] = iDiffuse[0];
		m_Diffuse[1] = iDiffuse[1];
		m_Diffuse[2] = iDiffuse[2];
				
		m_Specular[0]= iSpecular[0];
		m_Specular[1]= iSpecular[1];
		m_Specular[2]= iSpecular[2];

		m_Specular_Shininess = iSpecShininess;
		m_Alpha = iAlpha;
		
	}
	
	void SetEmissive(float r, float g, float b)
	{
		// values are 0 through 1 inclusive
		m_Emissive[0] = r;
		m_Emissive[1] = g;
		m_Emissive[2] = b;
	}
	
	void SetAmbient(float r, float g, float b)
	{
		// values are 0 through 1 inclusive
		m_Ambient[0] = r;
		m_Ambient[1] = g;
		m_Ambient[2] = b;
	}
	
	void SetDiffuse(float r, float g, float b)
	{
		// values are 0 through 1 inclusive
		m_Diffuse[0] = r;
		m_Diffuse[1] = g;
		m_Diffuse[2] = b;
	}
	
	void SetSpecular(float r, float g, float b)
	{
		// values are 0 through 1 inclusive
		m_Specular[0] = r;
		m_Specular[1] = g;
		m_Specular[2] = b;
	}
	
	void SetSpecularShininess(float value)
	{
		m_Specular_Shininess = value;
	}
	
	void SetAlpha(float value)
	{
		m_Alpha = value;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	float[] GetEmissive()
	{
		return m_Emissive;
	}
	
	float[] GetAmbient () 
	{
		return m_Ambient;
	}
	
	float[]	GetDiffuse()
	{
		return m_Diffuse;
	}
		
	float[]	GetSpecular() 
	{
		return m_Specular;
	}
	
	float GetSpecularShininess()
	{
		return m_Specular_Shininess;
	}
		
	float GetAlpha()
	{
		return m_Alpha;
	}
	
}
