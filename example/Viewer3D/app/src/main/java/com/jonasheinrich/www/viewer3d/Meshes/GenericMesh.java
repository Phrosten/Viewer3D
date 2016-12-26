package com.jonasheinrich.www.viewer3d.Meshes;

import android.opengl.GLES20;
import android.util.Log;

import com.jonasheinrich.www.viewer3d.Base.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class GenericMesh extends BodyMesh
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

    public GenericMesh()
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
        return GetColor(polygons[INT_PER_POLYGON * i + 3]);
    }

    @Override
    public float[] GetColor(int i)
    {
        float[] r = new float[FLOAT_PER_COLOR];

        for(int y = 0; y < FLOAT_PER_COLOR; y++)
            r[y] = colors[i * FLOAT_PER_COLOR + y];
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
        return BodyMesh.CalculateRenderSequence(polygons, Arrays.copyOf(vertices, vertices.length), cameraCords, angle, axis);
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
                float[] v = BodyMesh.RotateAroundOrigin(GetVertex(polygons[polygonIndex * INT_PER_POLYGON + i]), angle, axis);

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

    float[] colors = new float[]
            {
                    0.800000f, 0.000000f, 0.002678f, 1.0f,
            };
    float[] vertices = new float[]
            {
                    1.000000f, -1.000000f, -1.000000f,
                    1.000000f, -1.000000f, 1.000000f,
                    -1.000000f, -1.000000f, 1.000000f,
                    -1.000000f, -1.000000f, -1.000000f,
                    1.000000f, 1.000000f, -0.999999f,
                    0.999999f, 1.000000f, 1.000001f,
                    -1.000000f, 1.000000f, 1.000000f,
                    -1.000000f, 1.000000f, -1.000000f,
                    1.000000f, -1.000000f, -1.000000f,
                    1.000000f, -1.000000f, 1.000000f,
                    -1.000000f, -1.000000f, 1.000000f,
                    -1.000000f, -1.000000f, -1.000000f,
                    1.000000f, 1.000000f, -0.999999f,
                    0.999999f, 1.000000f, 1.000001f,
                    -1.000000f, 1.000000f, 1.000000f,
                    -1.000000f, 1.000000f, -1.000000f,
                    4.483203f, -1.000000f, -0.999999f,
                    4.483203f, -1.000000f, 1.000001f,
                    4.483204f, 1.000000f, -0.999998f,
                    4.483203f, 1.000000f, 1.000002f,
                    1.000000f, 5.507521f, -0.999999f,
                    0.999999f, 5.507521f, 1.000001f,
                    -1.000000f, 5.507521f, -1.000000f,
                    -1.000000f, 5.507521f, 1.000000f,
            };
    short[] polygons = new short[]
            {
                    3, 1, 0, 0,
                    5, 7, 4, 0,
                    1, 4, 0, 0,
                    2, 5, 1, 0,
                    2, 7, 6, 0,
                    7, 0, 4, 0,
                    9, 11, 8, 0,
                    12, 21, 13, 0,
                    12, 19, 18, 0,
                    13, 10, 9, 0,
                    10, 15, 11, 0,
                    8, 15, 12, 0,
                    18, 17, 16, 0,
                    9, 19, 13, 0,
                    12, 16, 8, 0,
                    9, 16, 17, 0,
                    22, 21, 20, 0,
                    14, 22, 15, 0,
                    15, 20, 12, 0,
                    13, 23, 14, 0,
                    3, 2, 1, 0,
                    5, 6, 7, 0,
                    1, 5, 4, 0,
                    2, 6, 5, 0,
                    2, 3, 7, 0,
                    7, 3, 0, 0,
                    9, 10, 11, 0,
                    12, 20, 21, 0,
                    12, 13, 19, 0,
                    13, 14, 10, 0,
                    10, 14, 15, 0,
                    8, 11, 15, 0,
                    18, 19, 17, 0,
                    9, 17, 19, 0,
                    12, 18, 16, 0,
                    9, 8, 16, 0,
                    22, 23, 21, 0,
                    14, 23, 22, 0,
                    15, 22, 20, 0,
                    13, 21, 23, 0,
            };


}