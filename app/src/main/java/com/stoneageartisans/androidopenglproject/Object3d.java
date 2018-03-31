/*
 * Created by William Mann on 7/27/2016.
 */

package com.stoneageartisans.androidopenglproject;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

public class Object3d {
    
    private final Context main_activity;
    
    private final Random random;
    
    private ArrayList<Triangle> polygons;
    
    private int polygon_count;
    private int vertex_count;
    
    private FloatBuffer[] vertex_buffer_object;
    
    public Object3d(Context CONTEXT, String OBJECT_NAME) {
        main_activity = CONTEXT;
        random = new Random();
        if(!load_object(OBJECT_NAME)) {
            polygon_count = 0;
            vertex_count = 0;
            vertex_buffer_object = new FloatBuffer[0];
        }
    }
    
    public ArrayList<Triangle> get_polygons() {
        return polygons;
    }

    public int get_polygon_count() {
        return polygon_count;
    }
    
    public int get_vertex_count() {
        return vertex_count;
    }
    
    public FloatBuffer get_vertex_buffer_object(int INDEX) {
        return vertex_buffer_object[INDEX];
    }
    
    private boolean load_object(String OBJECT_NAME) {
        polygons = new ArrayList<Triangle>();
        
        BufferedReader buffered_reader = new BufferedReader(
            new InputStreamReader(main_activity.getResources()
                .openRawResource(main_activity.getResources()
                    .getIdentifier(OBJECT_NAME, "raw", main_activity.getPackageName())
                )
            )
        );
        
        try {
            if(!buffered_reader.ready()) {
                return false;
            } else {
                String line;
                while((line = buffered_reader.readLine()) != null) {
                    String[] token = line.split(" ");                    
                    /* 
                     * Each line of the .raw file has 9 values representing
                     * the x, y, z coordinates of each point of the triangle
                     */                    
                    polygons.add(new Triangle(new Coordinate3d(Float.valueOf(token[0]),
                                                               Float.valueOf(token[1]),
                                                               Float.valueOf(token[2])
                                              ),
                                              new Coordinate3d(Float.valueOf(token[3]),
                                                               Float.valueOf(token[4]),
                                                               Float.valueOf(token[5])
                                              ),
                                              new Coordinate3d(Float.valueOf(token[6]),
                                                               Float.valueOf(token[7]),
                                                               Float.valueOf(token[8])
                                              )
                    ));
                }
                polygon_count = polygons.size();
                vertex_count = polygon_count * 3;
                vertex_buffer_object = new FloatBuffer[polygon_count];
                for(int i = 0; i < polygon_count; i ++) {
                    vertex_buffer_object[i] = ByteBuffer.allocateDirect(2 * 9 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
                    
                    Triangle polygon = polygons.get(i);
                    
                    polygon.set_trajectory_speed((float) (random.nextInt(4) + 5) * 0.5f);
                    
                    switch(random.nextInt(2)) {
                        case 0:
                            polygon.set_rotation_speed((float) random.nextInt(6) * (-5.0f));
                            break;
                        case 1:
                            polygon.set_rotation_speed((float) random.nextInt(6) * 5.0f);
                            break;
                    }
                    
                    switch(random.nextInt(3)) {
                        case 0:
                            polygon.set_rotation_x(1.0f);
                            break;
                        case 1:
                            polygon.set_rotation_x(-1.0f);
                            break;
                        case 2:
                            polygon.set_rotation_x(0.0f);
                            break;
                    }

                    switch(random.nextInt(3)) {
                        case 0:
                            polygon.set_rotation_y(1.0f);
                            break;
                        case 1:
                            polygon.set_rotation_y(-1.0f);
                            break;
                        case 2:
                            polygon.set_rotation_y(0.0f);
                            break;
                    }

                    switch(random.nextInt(3)) {
                        case 0:
                            polygon.set_rotation_z(1.0f);
                            break;
                        case 1:
                            polygon.set_rotation_z(-1.0f);
                            break;
                        case 2:
                            polygon.set_rotation_z(0.0f);
                            break;
                    }
                    
                    Coordinate3d point1 = polygon.get_point1();
                    Coordinate3d point2 = polygon.get_point2();
                    Coordinate3d point3 = polygon.get_point3();
                    
                    float[] temp_array = {
                        point1.get_x(), point1.get_y(), point1.get_z(),
                        point2.get_x(), point2.get_y(), point2.get_z(),
                        point3.get_x(), point3.get_y(), point3.get_z()
                    };
                    
                    vertex_buffer_object[i].put(temp_array);
                    vertex_buffer_object[i].flip();
                }
            }
        } catch(IOException ex) {}
        
        return true;
    }
    
    public void explode_polygons(int CURRENT_FRAME) {
        for(int i = 0; i < polygon_count; i ++) {
            Triangle polygon = polygons.get(i);
            
            float speed;
            if(CURRENT_FRAME < 30) {
                speed = ((float) CURRENT_FRAME + 1.0f) * polygon.get_trajectory_speed();
            } else {
                 speed = ((float) CURRENT_FRAME + 0.25f) * polygon.get_trajectory_speed();
            }
                        
            Coordinate3d point1 = polygon.get_point1();
            Coordinate3d point2 = polygon.get_point2();
            Coordinate3d point3 = polygon.get_point3();
            
            Coordinate3d center_point = polygon.get_center_point(); // makes the trajectory centered on the triangle
            
            // Translate the polygon away from origin along the trajectory
            point1 = new Coordinate3d(point1.get_x() + (center_point.get_x() / speed),
                                      point1.get_y() + (center_point.get_y() / speed),
                                      point1.get_z() + (center_point.get_z() / speed)
            );
            point2 = new Coordinate3d(point2.get_x() + (center_point.get_x() / speed),
                                      point2.get_y() + (center_point.get_y() / speed),
                                      point2.get_z() + (center_point.get_z() / speed)
            );
            point3 = new Coordinate3d(point3.get_x() + (center_point.get_x() / speed),
                                      point3.get_y() + (center_point.get_y() / speed),
                                      point3.get_z() + (center_point.get_z() / speed)
            );
            
            polygon.set_points(point1, point2, point3);            
            
            polygon.set_rotation_angle(polygon.get_rotation_angle() + polygon.get_rotation_speed());

            float[] vertex_array = {
                point1.get_x(), point1.get_y(), point1.get_z(),
                point2.get_x(), point2.get_y(), point2.get_z(),
                point3.get_x(), point3.get_y(), point3.get_z(),
            };

            vertex_buffer_object[i].clear();
            vertex_buffer_object[i].put(vertex_array);
            vertex_buffer_object[i].flip();
        }
    }
}
