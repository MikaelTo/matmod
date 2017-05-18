package bouncing_balls;

/**
 * Created by Mikael on 5/17/2017.
 */

class Vector
{
    double x,y;
    public Vector(double x, double y)
    {
        this.x = x; this.y = y;
    }

    public Vector(Model.Ball b)
    {
        x = b.vx;
        y = b.vy;
    }

    Vector normalize()
    {
        double l = len();
        x/=l;
        y/=l;
        return this;
    }

    public double distance(Vector b)
    {
        return Math.sqrt(Math.pow(x-b.x,2)+Math.pow(y-b.y,2));
    }

    public void add(Vector v)
    {
        x+=v.x;
        y += v.y;
    }

    public Vector clone()
    {
        return new Vector(x,y);
    }

    double len()
    {
        return Math.sqrt(x*x+y*y);
    }

    public Vector directionTo(Vector vec)
    {
        return new Vector(x-vec.x,y-vec.y).normalize();
    }

    Vector negate()
    {
        x = -x;
        y = -y;
        return this;
    }

    public double angleOf()
    {
        return Math.atan2(y,x);
    }

    public Vector sub(Vector v)
    {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public static Vector sub(Vector a, Vector b)
    {
        Vector o = a.clone();
        return a.sub(b) ;
    }

    public Vector mul(double d){x*=d; y*=d; return this;}

    public void dot(Vector v)
    {
        x*=v.x;
        y*=v.y;
    }

    public static Vector mul(Vector a, Vector b)
    {
        return new Vector(a.x*b.x, b.y*a.y);
    }

    public static double dot(Vector a, Vector b)
    {
       return a.x*b.x+a.y*b.y;
    }

    public void rotate(double angle)
    {
        double tx = x;
        double ty = y;
        x = Math.cos(angle)*tx - Math.sin(angle)*ty;
        y = Math.cos(angle)*ty+tx*Math.sin(angle);
    }

    public static Vector scale(Vector v, double d)
    {
        Vector o = v.clone();
        o.mul(d);
        return o;
    }
}