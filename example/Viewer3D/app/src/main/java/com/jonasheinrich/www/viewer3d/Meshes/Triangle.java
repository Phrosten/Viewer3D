package com.jonasheinrich.www.viewer3d.Meshes;

import android.opengl.GLES20;
import android.util.Log;

import com.jonasheinrich.www.viewer3d.Base.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle extends BodyMesh
{
    int program;

    private final String vertexShaderCode =
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Triangle()
    {
        int vertexShader = LoadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = LoadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertexShader);
        GLES20.glAttachShader(program, fragmentShader);
        GLES20.glLinkProgram(program);
    }

    @Override
    public int GetPolygonCount()
    {
        return polygons.length / INT_PER_POLYGON;
    }

    public float[] GetPolygonColor(short i)
    {
        return GetColor(polygons[INT_PER_POLYGON * i + 4]);
    }

    @Override
    public float[] GetColor(int i)
    {
        float[] r = new float[FLOAT_PER_COLOR];

        for(int y = 0; y < FLOAT_PER_COLOR; y++)
            r[y] = colors[i * 3 + y];
        return r;
    }

    @Override
    public int GetProgram()
    {
        return program;
    }

    @Override
    public Integer[] GetRenderSequence(float[] cameraCords, float angle, byte axis)
    {
        return BodyMesh.CalculateRenderSequence(polygons, vertices, cameraCords, angle, axis);
    }

    @Override
    public FloatBuffer GetFaceVerticesBuffer(int polygonIndex, float[] origin, float angle, byte axis)
    {
        FloatBuffer vertexBuffer;
        float[] result = null;


        if(polygonIndex >= 0 && polygonIndex < polygons.length / INT_PER_POLYGON)
        {
            result = new float[3 * 3];
            for(int i = 0; i < 3; i++)
            {
                float[] v = BodyMesh.RotateAroundOrigin(GetVertex(i), angle, axis);

                for(int y = 0; y < 3; y++)
                    result[i * 3 + y] = origin[y] + v[y];
            }

            ByteBuffer bb = ByteBuffer.allocateDirect(3 * 3 * 4);
            bb.order(ByteOrder.nativeOrder());
            vertexBuffer = bb.asFloatBuffer();
            vertexBuffer.put(result);
            vertexBuffer.position(0);
            return vertexBuffer;
        }
        return null;
    }

    @Override
    public float[] GetVertex(int index)
    {
        return new float[]{vertices[index * 3], vertices[index * 3 + 1], vertices[index * 3 + 2]};
    }


    float[] vertices = new float[]
    {
        1.0f,    0.0f,   0f,
        2.0f,    4.0f,   0f,
        3.0f,    0f,     0f,
    };

    float[] colors =
    {
        1f, 0f, 0f, 1.0f,
    };

    //  Structure:
    // Index of 3 verts, index of color
    short[] polygons =
    {
        0, 1, 2, 0,
    };
}