package bouncing_balls;

import java.util.Map;

/**
 * The physics model.
 *
 * This class is where you should implement your bouncing balls model.
 *
 * The code has intentionally been kept as simple as possible, but if you wish, you can improve the design.
 *
 * @author Simon Robillard
 *
 */
class Model {

    double areaWidth, areaHeight;

    Ball [] balls;

    Model(double width, double height) {
        areaWidth = width;
        areaHeight = height;

        // Initialize the model with a few balls
        balls = new Ball[2];
        balls[0] = new Ball(width / 3, height * 0.9, 1.2, 1.6, 0.25);
        balls[1] = new Ball(2 * width / 3, height * 0.7, -0.6, 0.6, 0.4);
    }

    void step(double deltaT) {
        // TODO this method implements one step of simulation with a step deltaT
        // free moment in the air the vertical pos y changes according to F=my, x no force.
        // Collision with the walls reverse DONE
        // Collision between the balls
        if((Math.pow(balls[0].x - balls[1].x,2) + Math.pow(balls[0].y - balls[1].y, 2)) < Math.pow(balls[0].radius+balls[1].radius, 2))
        { // Intersection.
            //balls[0].vx *= -1;
            Vector a = new Vector(balls[0].x, balls[0].y);
            Vector b = new Vector(balls[1].x, balls[1].y);
            Vector dir = a.directionTo(b);
            /// Noraml vector from B to A
            // now we have to mirror the velocitys around this vec to get the new directions
            /// if d is incomming and r is outcomming the formula is : r = d-2(dot(d,n))n

            /*
            Debug code.
            Vector n = dir.clone();
            Vector d = new Vector(balls[0]);
            Vector para = Vector.scale(n,Vector.dot(Vector.scale(d,2),n));
            Vector r = d.clone();
            r.sub(para);
            */


            // This is to decide the new angles of the balls.
            /*dir.negate();
            Vector n0 = dir.clone();
            Vector aMirror = new Vector(balls[0]);
            aMirror.sub(Vector.scale(n0.mul(Vector.dot(new Vector(balls[0]), n0)),2));
            dir.negate();
            Vector n1 = dir.clone();
            Vector bMirror = new Vector(balls[1]);
            bMirror.sub(Vector.scale(n1.mul(Vector.dot(new Vector(balls[1]), n1)),2));

            aMirror.normalize();
            aMirror.mul(balls[0].vel());
            bMirror.normalize();
            bMirror.mul(balls[1].vel());
            Vector rlt1 = aMirror;
            Vector rlt2 = bMirror;*/

           // DIR = BA
           Vector actualLen = a.clone();
           actualLen.sub(b);
           double distance = actualLen.len();
           double targetDistance = balls[0].radius + balls[1].radius;
           if(distance < targetDistance){
               //System.out.println(targetDistance - distance);
               balls[0].x += dir.x * (targetDistance - distance);
               balls[0].y += dir.y * (targetDistance - distance);
           }


            /*
                Now we have 2 mirrored velocity vectors, depending on what the real equation is we can use these diffrently.
                If the tangentical velocity is not affected we can represent it as: aTangent = aMirror.add(new Vector(Ball[0])) ;

             */

            Ball b1 = balls[0];
            Ball b2 = balls[1];

            // TEST1
            /*Vector b1pos = new Vector(b1.x, b1.y);
            Vector b2pos = new Vector(b2.x, b2.y);
            Vector between = b2pos.clone();
            between.sub(b1pos); // B1-B2
            double phiold = between.angleOf();

            Vector vang = new Vector(balls[0]);
            Vector uang = new Vector(balls[1]);
            double phi = deltaAngle(vang.angleOf(), uang.angleOf());
            System.out.println("old = " + phiold + " new = " + phi);

            Vector rlt1 = test(phi, b1, b2);
            Vector rlt2 = test(phi, b2, b1);*/
            //TEST2
           // Vector rlt1 = test2(balls[0], balls[1]);
           // Vector rlt2 = test2(balls[1], balls[0]);
            // TEST 3
            test3(balls[0], balls[1]);


            double va = balls[0].vel();
            double vb = balls[1].vel();
            double ma = balls[0].mass();
            double mb = balls[1].mass();
            final double e1 = ma*va*va/2 + mb*vb*vb/2;
           // ma*va+mb*vb=ma*(ub-(va-vb))+mb*ub;


            /*double ub = (ma*va+mb*vb)/((ma-ma*(va-vb)+mb));
            ub = Math.abs(ub);
            double ua = (e1-mb*ub)/ma;
            ua = Math.abs(ua);

            aMirror.normalize();
            bMirror.normalize();

            balls[0].vx = aMirror.x * ua;
            balls[0].vy = aMirror.y * ua;
            balls[1].vx = bMirror.x * ub;
            balls[1].vy = bMirror.y * ub;*/

            /*balls[0].vx = rlt1.x;
            balls[0].vy = rlt1.y;
            balls[1].vx = rlt2.x;
            balls[1].vy = rlt2.y;*/

            double e2 = balls[0].mass()*balls[0].vel()*balls[0].vel()/2 + balls[1].mass()*balls[1].vel()*balls[1].vel()/2;

            System.out.println("E1 = " + e1 + " and E2 = " + e2);
        }

        //System.out.println("Sum E = " + 9.82*balls[0].y+balls[0].vy*balls[0].vy/2 + 9.82*balls[1].y+balls[1].vy*balls[1].vy/2);

        for (Ball b : balls) {
            // detect collision with the border
            if (b.x < b.radius || b.x > areaWidth - b.radius) {
                b.vx *= -1; // change direction of ball
            }
            if (b.y < b.radius || b.y > areaHeight - b.radius) {
                b.vy *= -1;
                if(b.y < b.radius) b.y = b.radius;
            }

            b.vy -= deltaT*9.82;

            b.x += deltaT * b.vx;
            b.y += deltaT * b.vy;
        }
    }

    /**
     * Simple inner class describing balls.
     */


    class Ball {

        Ball(double x, double y, double vx, double vy, double r) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.radius = r;

        }

        double mass()
        { // we assume that one sphere/ball has the mass of its volume
            return 4*Math.PI*radius*radius*radius/3;
        }

        double vel()
        {
            return Math.sqrt(vx*vx+vy*vy);
        }

        Vector getPosition()
        {
            return new Vector(x,y);
        }
        /**
         * Position, speed, and radius of the ball. You may wish to add other attributes.
         */
        double x, y, vx, vy, radius;
    }


    Vector test(double phi, Ball b1, Ball b2)
    {

        Vector v1 = new Vector(b1);
        Vector v2 = new Vector(b2);
        double o1 = v1.angleOf();
        double o2 = v2.angleOf();

        double b1x1 = (b1.vel()*Math.cos(o1-phi)*(b1.mass()-b2.mass())+2*b2.mass()*b2.vel()*Math.cos(o2-phi)*Math.cos(phi)) / (b1.mass()+b2.mass());
        b1x1 += b1.vel()*Math.sin(o1-phi)*Math.cos(phi+Math.PI/2);

        double b1y1 = (b1.vel()*Math.cos(o1-phi)*(b1.mass()-b2.mass())+2*b2.mass()*b2.vel()*Math.cos(o2-phi)*Math.cos(phi)) / (b1.mass()+b2.mass());
        b1x1 += b1.vel()*Math.sin(o1-phi)*Math.sin(phi+Math.PI/2);

        return new Vector(b1x1, b1y1);
    }

    Vector test2(Ball b1, Ball b2)
    {
        Vector v1 = new Vector(b1);
        Vector v2 = new Vector(b2);
        Vector normal = new Vector(b1.x, b1.y);
        normal.sub(new Vector(b2.x, b2.y));
        normal.normalize();
        double k = b2.mass()*2/(b1.mass()+b2.mass());// 2m2/m1+m2
        double dp = Vector.dot(Vector.sub(v1,v2), normal); // (v1-v2 dot x1-x2)
        if(normal.len() > 1.01 || normal.len() < 0.99) System.out.println("error");

        Vector vNew = v1.sub(Vector.scale(normal,k*dp));
        return vNew;
    }


    void test3(Ball b1, Ball b2)
    {
        double angle =  b1.getPosition().directionTo(b2.getPosition()).angleOf();

        Vector oldVelocity1 = new Vector(b1);
        Vector oldVelocity2 = new Vector(b2);

        Vector nv1 = oldVelocity1.clone();
        Vector nv2 = oldVelocity2.clone();
        // rotate the vectors to the intersection direction
        nv1.rotate(-angle);
        nv2.rotate(-angle);
        // adjust the new velocity
        double newVel1 = ((b1.mass()-b2.mass())*nv1.x+2*b2.mass()*nv2.x) / (b1.mass()+b2.mass());
        double newVel2 = ((b2.mass()-b1.mass())*nv2.x+2*b1.mass()*nv1.x) / (b2.mass()+b1.mass());
        nv1.x = newVel1;
        nv2.x = newVel2;
        // rotate them back. the velocity in the normal to the intersection ray is not affected.
        nv1.rotate(angle);
        oldVelocity1 = nv1.clone();
        nv2.rotate(angle);
        oldVelocity2 = nv2.clone();

        b1.vx = oldVelocity1.x;
        b1.vy = oldVelocity1.y;
        b2.vx = oldVelocity2.x;
        b2.vy = oldVelocity2.y;
    }

    private double deltaAngle(double alpha, double beta) {
        double phi = Math.abs(beta - alpha) % (Math.PI*2);       // This is either the distance or 360 - distance
        double distance = phi > Math.PI ? Math.PI*2 - phi : phi;
        return distance;
    }
}