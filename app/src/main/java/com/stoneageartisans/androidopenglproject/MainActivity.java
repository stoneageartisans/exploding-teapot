/*
 * Created by William Mann on 7/26/2016.
 */

package com.stoneageartisans.androidopenglproject;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    
    private static GLSurfaceView gl_surface_view;
    private static OpenGLESRenderer renderer;
    
    private void create_view() {
        gl_surface_view = new GLSurfaceView(this);
        renderer = new OpenGLESRenderer(this, gl_surface_view);
        gl_surface_view.setRenderer(renderer);
        gl_surface_view.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setContentView(gl_surface_view);
        
        gl_surface_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent me) {
                renderer.set_exploding(true);
                gl_surface_view.requestRender();
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        create_view();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gl_surface_view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gl_surface_view.onResume();
    }
}
