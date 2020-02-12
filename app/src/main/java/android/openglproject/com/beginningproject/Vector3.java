package android.openglproject.com.beginningproject;

import android.util.FloatMath;

import java.lang.Math;

class Vector3 {
    public float x;
    public float y;
    public float z;

    @Override
    public String toString() {
        return "Vector3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    public Vector3(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    //////////////////// Vector Operators ////////////////////////////////////

    void Multiply(float v) {
        x *= v;
        y *= v;
        z *= v;
    }

    void Negate() {
        x = -x;
        y = -y;
        z = -z;
    }
    void AddScale(float _scale){
        this.x += _scale;
        this.y += _scale;
        this.z += _scale;
    }
    void Add(Vector3 other){
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }
    ///////////// Static Vector Operations ///////////////////////////////////

    static Vector3 Add(Vector3 vec1, Vector3 vec2) {
        Vector3 result = new Vector3(0, 0, 0);

        result.x = vec1.x + vec2.x;
        result.y = vec1.y + vec2.y;
        result.z = vec1.z + vec2.z;

        return result;
    }

    /////////////////////////////////////////////////////////////////////////

    void Set(float _x, float _y, float _z) {
        x = _x;
        y = _y;
        z = _z;
    }

    public void Set(Vector3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    void Normalize() {
        float l = Length();

        x = x / l;
        y = y / l;
        z = z / l;
    }

    float Length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
        //return java.lang.Math.sqrt(x*x + y*y + z*z);
    }

    static Vector3 CrossProduct(Vector3 a, Vector3 b) {
        Vector3 result = new Vector3(0, 0, 0);

        result.x = (a.y * b.z) - (a.z * b.y);
        result.y = (a.z * b.x) - (a.x * b.z);
        result.z = (a.x * b.y) - (a.y * b.x);

        return result;
    }

    float DotProduct(Vector3 vec) {
        return (x * vec.x) +
                (y * vec.y) +
                (z * vec.z);

    }

    boolean inRange(float min, float max){
        if (this.x > min && this.x < max){
            if (this.y > min && this.y < max) {
                if (this.z > min && this.z < max) {
                    return true;
                }
            }
        }
        return false;
    }


}
