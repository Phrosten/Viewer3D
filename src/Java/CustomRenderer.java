// TODO: add package name

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;



import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CustomRenderer implements GLSurfaceView.Renderer
{
    List<Class<? extends BodyMesh>> meshTypes;
    BodyMesh[] meshes;

    float[] cameraPosition = new float[]{10, 10, 10};
    float[] cameraCenter = new float[]{0, 1, 0};

    public CustomRenderer()
    {
        meshTypes = BodyMesh.Factory.GetList(new Class[]{Pyramid.class});
    }

    private void Initialize()
    {
        meshes = new BodyMesh[meshTypes.size()];
        for(int i = 0; i < meshTypes.size(); i++)
            meshes[i] = BodyMesh.Factory.GetMesh(meshTypes.get(i));
    }

    public void onSurfaceCreated(GL10 unused, EGLConfig config)
    {
        Initialize();
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused)
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        Draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height)
    {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 2, 100000);
    }

    private final float[] MVPMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    float angle = 0.7f;

    private void Draw()
    {
        angle += 0.005f;


        int positionHandle;
        int colorHandle;

        // Define matrix
        Matrix.setLookAtM
        (
                viewMatrix,
                0,
                cameraPosition[0],
                cameraPosition[1],
                cameraPosition[2],
                cameraCenter[0],
                cameraCenter[1],
                cameraCenter[2],
                0f, 1.0f, 0
        );

        Matrix.multiplyMM(MVPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Draw single bodies
        for(int i = 0; i < meshes.length; i++)
        {
            // Load program
            int program = meshes[i].GetProgram();
            GLES20.glUseProgram(program);

            // Get and enable position
            positionHandle = GLES20.glGetAttribLocation(program, "vPosition");
            GLES20.glEnableVertexAttribArray(positionHandle);

            // Get matrix handle
            int MVPMatrixHandle;
            MVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

            // Apply matrix
            GLES20.glUniformMatrix4fv(MVPMatrixHandle, 1, false, MVPMatrix, 0);

            // Get color handle
            colorHandle = GLES20.glGetUniformLocation(program, "vColor");

            Integer[] renderingSequence = meshes[i].GetRenderSequence(cameraPosition, angle, (byte)1);

            // Draw surfaces of bodies
            for(int y = 0; y < renderingSequence.length; y++)
            {
                // Copy to memory
                GLES20.glVertexAttribPointer(positionHandle, 3,
                        GLES20.GL_FLOAT, false,
                        12, meshes[i].GetFaceVerticesBuffer(renderingSequence[y], new float[]{0, 0, 0}, angle, (byte)1));

                //  Set color
                GLES20.glUniform4fv(colorHandle, 1, meshes[i].GetPolygonColor((short)renderingSequence[y].intValue()), 0);

                // Draw array
                GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
            }

            GLES20.glDisableVertexAttribArray(positionHandle);
        }
    }
}
