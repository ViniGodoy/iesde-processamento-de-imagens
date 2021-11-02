package cap1;

public final class Vector3 {
    public float x;
    public float y;
    public float z;

    public Vector3() {}

    public Vector3(float x, float y, float z) {
        set(x, y, z);
    }

    public Vector3 set(float x, float y, float z) {
        this.x = x; this.y = y; this.z = z;
        return this;
    }

    public Vector3 add(Vector3 v) {
        x += v.x; y += v.y; z += v.z;
        return this;
    }
   
    public Vector3 sub(Vector3 v) {
        x -= v.x; y -= v.y; z -= v.z;
        return this;
    }

    //Produto de hadamard
    public Vector3 mul(Vector3 v) {
        x *= v.x; y *= v.y; z *= v.z;
        return this;
    }

    //Multiplicação por um escalar
    public Vector3 mul(float s) {
        x *= s; y *= s; z *= s;
        return this;
    }

    //Divisão por um escalar
    public Vector3 div(float s) {
        x /= s; y /= s; z /= s;
        return this;
    }

    public float dot(Vector3 v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public float size() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalize() {
        return div(size());
    }

    @Override
    public String toString() {
        return String.format("(%.3f, %.3f, %.3f)", x, y, z);
    }

    public static void main(String[] args) {
        var v1 = new Vector3(5, -3, 2);
        var v2 = new Vector3(2, 1, -5);
        var v3 = v1.add(v2.mul(3));
        System.out.println(v3);
        System.out.println(v3.size()); //17.029387
    }
}