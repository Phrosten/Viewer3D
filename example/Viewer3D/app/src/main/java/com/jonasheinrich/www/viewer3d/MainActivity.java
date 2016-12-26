package com.jonasheinrich.www.viewer3d;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class MainActivity extends AppCompatActivity {
    private GLSurfaceView glSurfaceView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glSurfaceView = new GLSurfaceView_General(this);
        setContentView(glSurfaceView);
    }
}
