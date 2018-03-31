/*
 * Created by William Mann on 7/27/2016.
 */

package com.stoneageartisans.androidopenglproject;

public class Coordinate3d {
    
    private final float x;
    private final float y;
    private final float z;
    
    public Coordinate3d(float X, float Y, float Z) {        
        x = X;
        y = Y;
        z = Z;
    }
    
    public float get_x() {
        return x;
    }
    
    public float get_y() {
        return y;
    }
    
    public float get_z() {
        return z;
    }    
}
