package com.robsexample.glhelloworld;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ShortBuffer;


public class MeshEx 
{
	// Use Indexed Drawing method
	private FloatBuffer m_VertexBuffer 		= null; // Holds mesh vertex data
	private ShortBuffer m_DrawListBuffer 	= null; // Holds index values into the vertex buffer that indicate 
													// the order in which to draw the vertices.
	
	private int m_CoordsPerVertex = 3;
	private static final int FLOAT_SIZE_BYTES = 4;
	private int m_VertexCount = 0;
	private int m_MeshVerticesDataStrideBytes = m_CoordsPerVertex * FLOAT_SIZE_BYTES;
	private int m_MeshVerticesDataPosOffset = 0;
	private int m_MeshVerticesDataUVOffset = -1;
	private int m_MeshVerticesDataNormalOffset = -1; 
	
	private boolean m_MeshHasUV = false;
	private boolean m_MeshHasNormals = false;
	
	
	
	public MeshEx(int CoordsPerVertex, 
				int MeshVerticesDataPosOffset, 
				int MeshVerticesUVOffset , 
				int MeshVerticesNormalOffset,
				float[] Vertices,
				short[] DrawOrder
				)
	{
		m_CoordsPerVertex = CoordsPerVertex;
		m_MeshVerticesDataStrideBytes	= m_CoordsPerVertex * FLOAT_SIZE_BYTES;
		m_MeshVerticesDataPosOffset 	= MeshVerticesDataPosOffset;
		m_MeshVerticesDataUVOffset 		= MeshVerticesUVOffset ; 
		m_MeshVerticesDataNormalOffset 	= MeshVerticesNormalOffset;
		
		if (m_MeshVerticesDataUVOffset >= 0)
		{
			m_MeshHasUV = true;
		}
		
		if (m_MeshVerticesDataNormalOffset >=0)
		{
			m_MeshHasNormals = true;
		}
		
		// Allocate Vertex Buffer
		ByteBuffer bb = ByteBuffer.allocateDirect(
				    // (# of coordinate values * 4 bytes per float)
					Vertices.length * FLOAT_SIZE_BYTES);
	    bb.order(ByteOrder.nativeOrder());
	    m_VertexBuffer = bb.asFloatBuffer();
		
	    if (Vertices != null)
	    {
	    	m_VertexBuffer.put(Vertices);
	    	m_VertexBuffer.position(0);
	    	    	
	    	m_VertexCount = Vertices.length / m_CoordsPerVertex;
	    }
	    
	   // Initialize DrawList Buffer  
	   m_DrawListBuffer = ShortBuffer.wrap(DrawOrder);
	  
	}
	
	void SetUpMeshArrays(int PosHandle, int TexHandle, int NormalHandle)
	{
		//glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, int offset)
		//glVertexAttribPointer(int indx, int size, int type, boolean normalized, int stride, Buffer ptr)
		
		// Set up stream to position variable in shader
		m_VertexBuffer.position(m_MeshVerticesDataPosOffset);
	    GLES20.glVertexAttribPointer(PosHandle, 
	    							3, 
	    							GLES20.GL_FLOAT, 
	    							false,
	    							m_MeshVerticesDataStrideBytes, 
	    							m_VertexBuffer);
	       
	    GLES20.glEnableVertexAttribArray(PosHandle);
	     
	    
	    if (m_MeshHasUV)
	    {
	    	// Set up Vertex Texture Data stream to shader  
	    	m_VertexBuffer.position(m_MeshVerticesDataUVOffset);
	    	GLES20.glVertexAttribPointer(TexHandle, 
	    								2, 
	    								GLES20.GL_FLOAT, 
	    								false,
	    								m_MeshVerticesDataStrideBytes, 
	    								m_VertexBuffer);
	    	GLES20.glEnableVertexAttribArray(TexHandle);
	    }
	   
	    if (m_MeshHasNormals)
	    {
	    	
	    	// Set up Vertex Texture Data stream to shader
	    	m_VertexBuffer.position(m_MeshVerticesDataNormalOffset);
	    	GLES20.glVertexAttribPointer(NormalHandle, 
	    								3, 
	    								GLES20.GL_FLOAT, 
	    								false,
	    								m_MeshVerticesDataStrideBytes, 
	    								m_VertexBuffer);
	    	GLES20.glEnableVertexAttribArray(NormalHandle);
	    } 
	    
	          
	    
	}
	
	public static void CheckGLError(String glOperation) 
	{
		int error;
	    while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
	    	Log.e("ERROR IN MESHEX", glOperation + " IN CHECKGLERROR() : glError - " + error);
	        throw new RuntimeException(glOperation + ": glError " + error);
	    }
	}
	
	void DrawMesh(int PosHandle, int TexHandle, int NormalHandle)
	{
		SetUpMeshArrays(PosHandle, TexHandle, NormalHandle);
		
	   
		//glDrawElements (int mode, int count, int type, int offset)
		//glDrawElements (int mode, int count, int type, Buffer indices)
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, 
							  m_DrawListBuffer.capacity(),
							  GLES20.GL_UNSIGNED_SHORT, 
							  m_DrawListBuffer);
		
	    // Disable vertex array
	    GLES20.glDisableVertexAttribArray(PosHandle);
	    CheckGLError("glDisableVertexAttribArray ERROR - PosHandle");
	    
	    if (m_MeshHasUV)
	    {
	    	GLES20.glDisableVertexAttribArray(TexHandle);	
	    	CheckGLError("glDisableVertexAttribArray ERROR - TexHandle");
	    }
	    if (m_MeshHasNormals)
	    {
	    	GLES20.glDisableVertexAttribArray(NormalHandle);	
	    	CheckGLError("glDisableVertexAttribArray ERROR - NormalHandle");
	    }
	}
	
}
