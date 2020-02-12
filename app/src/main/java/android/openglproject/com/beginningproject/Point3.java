package android.openglproject.com.beginningproject;

public class Point3 {
    public float x;
    public float y;
    public float z;

    public Point3() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Point3(float x, float y, float z) {
        this.Set(x,y,z);
    }

    public Point3(Point3 src){
        this.Set(src.x,src.y,src.z);
    }

    public void Set(float _x, float _y, float _z){
        this.x = _x;
        this.y = _y;
        this.z = _z;
    }

    @Override
    public String toString() {
        return "Point3{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
