// TODO: add package name

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;


class GLSurfaceView_General extends GLSurfaceView
{
    private final CustomRenderer renderer;
    public GLSurfaceView_General(Context context)
    {
        super(context);
        renderer = new CustomRenderer();
        Initialize();
    }

    private void Initialize()
    {
        setEGLContextClientVersion(2);
        setRenderer(this.renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e)
    {
        return true;
    }
}