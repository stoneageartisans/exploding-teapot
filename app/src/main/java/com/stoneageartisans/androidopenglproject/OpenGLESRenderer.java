/*
 * Created by William Mann on 7/26/2016.
 */

package com.stoneageartisans.androidopenglproject;

import android.content.Context;
import android.opengl.GLES11;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenGLESRenderer implements GLSurfaceView.Renderer {
        
    private int render_mode;
    
    private boolean exploding;
    private int current_explode_frame;    
    
    private long current_time;
    private long last_time;
    
    private float aspect_ratio;
    
    private final Context main_activity;
    private final GLSurfaceView gl_surface_view;
    
    private Object3d object3d;

    public OpenGLESRenderer(Context CONTEXT, GLSurfaceView GL_SURFACE_VIEW) {
        render_mode = GLES11.GL_TRIANGLES;
        exploding = false;
        current_explode_frame = 0;        
        current_time = System.currentTimeMillis();
        last_time = current_time;
        main_activity = CONTEXT;
        gl_surface_view = GL_SURFACE_VIEW;
        aspect_ratio = (float) gl_surface_view.getWidth() / (float) gl_surface_view.getHeight();
    }
    
    public int get_render_mode() {
        return render_mode;
    }
    
    public void set_render_mode(int RENDER_MODE) {
        render_mode = RENDER_MODE;
    }
    
    public void set_exploding(boolean STATE) {
        exploding = STATE;
    }
    
    private void initialize_opengl() {        
        GLES11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        
        GLES11.glViewport(0, 0, gl_surface_view.getWidth(), gl_surface_view.getHeight());
        GLES11.glMatrixMode(GLES11.GL_PROJECTION); 
        GLES11.glLoadIdentity();
        GLES11.glFrustumf(-aspect_ratio, aspect_ratio, -1.0f, 1.0f, 1.0f, 100.0f);
        
        GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
        GLES11.glLoadIdentity();
        
        GLES11.glTranslatef(0.0f, 0.0f, -10.0f);
        GLES11.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
        
        GLES11.glEnable(GLES11.GL_DEPTH_TEST);
        GLES11.glDepthFunc(GLES11.GL_LESS);
        
        //GLES11.glEnable(GLES11.GL_NORMALIZE);
        GLES11.glEnable(GLES11.GL_COLOR_MATERIAL);
        GLES11.glEnable(GLES11.GL_LIGHTING);
        GLES11.glEnable(GLES11.GL_LIGHT0);
        
        GLES11.glLightModelx(GLES11.GL_LIGHT_MODEL_TWO_SIDE, GLES11.GL_TRUE);
        
        GLES11.glMaterialfv(GLES11.GL_FRONT, GLES11.GL_AMBIENT, Constants.MATERIAL_AMBIENT, 0);
        GLES11.glMaterialfv(GLES11.GL_FRONT, GLES11.GL_DIFFUSE, Constants.MATERIAL_DIFFUSE, 0);
        GLES11.glMaterialfv(GLES11.GL_FRONT, GLES11.GL_SPECULAR, Constants.MATERIAL_SPECULAR, 0);
        GLES11.glMaterialf(GLES11.GL_FRONT, GLES11.GL_SHININESS, Constants.MATERIAL_SHININESS);

        GLES11.glLightfv(GLES11.GL_LIGHT0, GLES11.GL_AMBIENT, Constants.LIGHT_AMBIENT, 0);
        GLES11.glLightfv(GLES11.GL_LIGHT0, GLES11.GL_DIFFUSE, Constants.LIGHT_DIFFUSE, 0);
        GLES11.glLightfv(GLES11.GL_LIGHT0, GLES11.GL_SPECULAR, Constants.LIGHT_SPECULAR, 0);
        GLES11.glLightfv(GLES11.GL_LIGHT0, GLES11.GL_POSITION, Constants.LIGHT_POSITION, 0);
        
        object3d = new Object3d(main_activity, "teapot");
    }
    
    private void reset() {
        exploding = false;
        current_explode_frame = 0;        
        object3d = new Object3d(main_activity, "teapot");
        gl_surface_view.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 GL, EGLConfig EGL_CONFIG) {
        initialize_opengl();
    }

    @Override
    public void onSurfaceChanged(GL10 GL, int WIDTH, int HEIGHT) {
        aspect_ratio = (float) WIDTH / (float) HEIGHT;
        GLES11.glViewport(0, 0, WIDTH, HEIGHT);
        GLES11.glMatrixMode(GLES11.GL_PROJECTION); 
        GLES11.glLoadIdentity();
        GLES11.glFrustumf(-aspect_ratio, aspect_ratio, -1.0f, 1.0f, 1.0f, 100.0f);
        GLES11.glMatrixMode(GLES11.GL_MODELVIEW);
        GLES11.glLoadIdentity();        
        GLES11.glTranslatef(0.0f, 0.0f, -10.0f);
        GLES11.glRotatef(30.0f, 1.0f, 0.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 GL) {
        if(exploding) {
            current_time = System.currentTimeMillis();
            if((current_time - last_time) > Constants.DELAY) {
                object3d.explode_polygons(current_explode_frame);
                current_explode_frame ++;                
                if(current_explode_frame > Constants.MAX_EXPLODE_FRAMES) {
                    reset();
                }
                last_time = current_time;
                gl_surface_view.requestRender();
            }
        }
        
        GLES11.glClear(GLES11.GL_COLOR_BUFFER_BIT | GLES11.GL_DEPTH_BUFFER_BIT);
        GLES11.glColor4f(0.95f, 0.9f, 0.75f, 1.0f);
        
        GLES11.glEnableClientState(GLES11.GL_NORMAL_ARRAY);
        GLES11.glEnableClientState(GLES11.GL_VERTEX_ARRAY);        
            
        for(int i = 0; i < object3d.get_polygon_count(); i ++) {
            GLES11.glPushMatrix();                
                if(exploding) {
                    Triangle polygon = object3d.get_polygons().get(i);
                    Coordinate3d center_point = polygon.get_center_point();                    
                    GLES11.glTranslatef(center_point.get_x(), center_point.get_y(), center_point.get_z());
                    GLES11.glRotatef(polygon.get_rotation_angle(),
                                     polygon.get_rotation_x(),
                                     polygon.get_rotation_y(),
                                     polygon.get_rotation_z()
                    );
                    GLES11.glTranslatef(-center_point.get_x(), -center_point.get_y(), -center_point.get_z());                    
                }
                GLES11.glVertexPointer(3, GLES11.GL_FLOAT, 0, object3d.get_vertex_buffer_object(i));
                GLES11.glNormalPointer(GLES11.GL_FLOAT, 0, object3d.get_vertex_buffer_object(i));
                GLES11.glDrawArrays(render_mode, 0, 3);
            GLES11.glPopMatrix();
        }
        
        GLES11.glDisableClientState(GLES11.GL_VERTEX_ARRAY);
        GLES11.glDisableClientState(GLES11.GL_NORMAL_ARRAY);
    }
}
