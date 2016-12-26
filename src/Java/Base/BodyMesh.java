// TODO: add package name

import android.opengl.GLES20;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class BodyMesh
{
    public BodyMesh()
    {}

    //
    //  Get/Set
    //

    public abstract int GetPolygonCount();

    public abstract float[] GetVertex(int index);

    public abstract float[] GetPolygonColor(short i);
    public abstract float[] GetColor(int triIndex);

    public abstract int GetProgram();

    public abstract Integer[] GetRenderSequence(float[] cameraCords, float angle, byte axis);


    public static final byte INT_PER_POLYGON = 4;
    public static final byte FLOAT_PER_COLOR = 4;
    public static final byte FLOAT_PER_VERTEX = 3;

    //  Get vertices of polygon
    public abstract FloatBuffer GetFaceVerticesBuffer(int areaIndex, float[] modifier, float angle, byte axis);

    //
    //  Static
    //

    public static int LoadShader(int type, String shaderCode)
    {
        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
    //
    //  Calculations
    //

    public static Integer[] CalculateRenderSequence(short[] polygons, float[] vertices, float[] cameraCords, float angle, byte axis)
    {
        Integer[] sequence = new Integer[polygons.length / INT_PER_POLYGON];
        float[] averageDistance = new float[polygons.length / INT_PER_POLYGON];

        for(int i = 0; i < vertices.length / FLOAT_PER_VERTEX; i++)
        {
            float origVertices[] = new float[]
            {
                vertices[i * FLOAT_PER_VERTEX + 0],
                vertices[i * FLOAT_PER_VERTEX + 1],
                vertices[i * FLOAT_PER_VERTEX + 2],
            };

            float[] v = BodyMesh.RotateAroundOrigin(origVertices, angle, axis);

            vertices[i * FLOAT_PER_VERTEX + 0] = v[0];
            vertices[i * FLOAT_PER_VERTEX + 1] = v[1];
            vertices[i * FLOAT_PER_VERTEX + 2] = v[2];
        }

        for(int i = 0; i < polygons.length / INT_PER_POLYGON; i++)
        {
            float[] averagePoint = new float[3];
            for(int y = 0; y < 3; y++)
            {
                averagePoint[y] =
                (
                    vertices[polygons[i * INT_PER_POLYGON + 0]  * FLOAT_PER_VERTEX + y] +
                    vertices[polygons[i * INT_PER_POLYGON + 1]  * FLOAT_PER_VERTEX + y] +
                    vertices[polygons[i * INT_PER_POLYGON + 2]  * FLOAT_PER_VERTEX + y]
                ) / 3.0f;
            }


            averageDistance[i] = (float)Math.sqrt
            (
                Math.pow(cameraCords[0] - averagePoint[0], 2) +
                Math.pow(cameraCords[1] - averagePoint[1], 2) +
                Math.pow(cameraCords[2] - averagePoint[2], 2)
            );
        }

        List<Integer> list = new ArrayList<Integer>();

        for(int i = 0; i < sequence.length; i++)
        {
            boolean inserted = false;

            for(int y = 0; y < list.size(); y++)
            {
                if(averageDistance[i] > averageDistance[list.get(y)])
                {
                    list.add(y, i);
                    inserted = true;
                    break;
                }
            }

            if(!inserted)
                list.add(i);
        }

        for(int i = 0; i < list.size(); i++)
            sequence[i] = list.get(i);

        return sequence;
    }


    public static float[] RotateAroundOrigin(float[] a, float angle, byte axis)
    {
        float xn;
        float yn;
        float zn;

        switch(axis)
        {
            // X
            case 0:
                xn = a[0];

                yn = a[1] * (float)Math.cos(angle) - a[2] * (float)Math.sin(angle);
                zn = a[1] * (float)Math.sin(angle) + a[2] * (float)Math.cos(angle);
                break;
            // Y
            case 1:
                yn = a[1];

                xn = a[0] * (float)Math.cos(angle) - a[2] * (float)Math.sin(angle);
                zn = a[0] * (float)Math.sin(angle) + a[2] * (float)Math.cos(angle);
                break;
            // Z
            default:
                zn = a[2];

                xn = a[0] * (float)Math.cos(angle) - a[1] * (float)Math.sin(angle);
                yn = a[0] * (float)Math.sin(angle) + a[1] * (float)Math.cos(angle);

                break;
        }

        return new float[]{xn, yn, zn};
    }

    //
    //  Factory for creating Meshes
    //

    public static class Factory
    {
        public static ArrayList<java.lang.Class<? extends BodyMesh>> GetList(java.lang.Class[] cl)
        {
            ArrayList<java.lang.Class<? extends BodyMesh>> ar = new ArrayList<>();

			// TODO add classes
            for(int i = 0; i < cl.length; i++)
                if(cl[i].equals(Triangle.class))
                    ar.add(Triangle.class);
                else if(cl[i].equals(Car.class))
                    ar.add(Car.class);
            return ar;
        }

        public static BodyMesh GetMesh(java.lang.Class<? extends BodyMesh> cl)
        {
            try
            {
                return cl.newInstance();
            }
            catch(InstantiationException e)
            {

            }
            catch(IllegalAccessException e)
            {

            }
            return null;
        }
    }
}
