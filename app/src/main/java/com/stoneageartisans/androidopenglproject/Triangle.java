/*
 * Created by William Mann on 7/27/2016.
 */

package com.stoneageartisans.androidopenglproject;

public class Triangle {
    
    private Coordinate3d point1;
    private Coordinate3d point2;
    private Coordinate3d point3;
    
    private Coordinate3d center_point;
    
    private float trajectory_speed;
    
    private float rotation_angle;
    private float rotation_speed;
    private float rotation_x;
    private float rotation_y;
    private float rotation_z;
    
    public Triangle(Coordinate3d POINT1, Coordinate3d POINT2, Coordinate3d POINT3) {
        point1 = POINT1;
        point2 = POINT2;
        point3 = POINT3;
        
        center_point = new Coordinate3d((point1.get_x() + point2.get_x() + point3.get_x()) / 3,
                                        (point1.get_y() + point2.get_y() + point3.get_y()) / 3,
                                        (point1.get_z() + point2.get_z() + point3.get_z()) / 3
        );
        
        rotation_angle = 0.0f;
        rotation_speed = 0.0f;
        rotation_x = 0.0f;
        rotation_y = 0.0f;
        rotation_z = 0.0f;
    }
    
    public Coordinate3d get_point1() {
        return point1;
    }
    
    public Coordinate3d get_point2() {
        return point2;
    }
    
    public Coordinate3d get_point3() {
        return point3;
    }
    
    public Coordinate3d get_center_point() {
        return center_point;
    }
    
    public float get_trajectory_speed() {
        return trajectory_speed;
    }
    
    public void set_trajectory_speed(float TRAJECTORY_SPEED) {
        trajectory_speed = TRAJECTORY_SPEED;
    }
    
    public float get_rotation_angle() {
        return rotation_angle;
    }
    
    public void set_rotation_angle(float ROTATION_ANGLE) {
        rotation_angle = ROTATION_ANGLE;
    }
    
    public float get_rotation_speed() {
        return rotation_speed;
    }
    
    public void set_rotation_speed(float ROTATION_SPEED) {
        rotation_speed = ROTATION_SPEED;
    }
    
    public float get_rotation_x() {
        return rotation_x;
    }
    
    public void set_rotation_x(float ROTATION_X) {
        rotation_x = ROTATION_X;
    }
    
    public float get_rotation_y() {
        return rotation_y;
    }
    
    public void set_rotation_y(float ROTATION_Y) {
        rotation_y = ROTATION_Y;
    }
    
    public float get_rotation_z() {
        return rotation_z;
    }
    
    public void set_rotation_z(float ROTATION_Z) {
        rotation_z = ROTATION_Z;
    }
    
    public void set_points(Coordinate3d POINT1, Coordinate3d POINT2, Coordinate3d POINT3) {
        point1 = POINT1;
        point2 = POINT2;
        point3 = POINT3;
        
        center_point = new Coordinate3d((point1.get_x() + point2.get_x() + point3.get_x()) / 3,
                                        (point1.get_y() + point2.get_y() + point3.get_y()) / 3,
                                        (point1.get_z() + point2.get_z() + point3.get_z()) / 3
        );
    }
}
