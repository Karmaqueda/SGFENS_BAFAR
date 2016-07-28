/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.tsp.sct.util.test;

import java.awt.*;
import java.awt.geom.*;
/**
 * This is a polygon that supports floating point values.  It does <b> not </b> extend java.awt.Polygon.  A polygon is comprised
 * of an array of x values and an array of it's corrisponding y values.  There is also an integer value - npoints - that determine 
 * the number of points you want to be valid.  The polygon starts at xpoints[0] and ypoints[0] (if it exists) and connects a line
 * to xpoints[1] and ypoints[1] and then to xpoints[2] and ypoints[2] and so on until the index equals npoints.  The path is then
 * closed back to xpoints[0] and ypoints[0].  Lets see an example:
 * <p> 
 * Polygon2D.Float p = new Polygon2D.Float( new float[] {20,20,40,40}, new float[] {20,40,40,20}, npoints);  //constructs a new new rectangle that is a polygon <br>
 * graphics.fill(p);  // fills a rectangle <br>
 * p.npoints = 3;  // the polygon now ignores the fourth coordinate <br>
 * graphics.fill(p);  // fills a triangle <br>
 * p.npoints = 2; // the polygon now ignores the last two coordinates <br>
 * graphic.fill(p); // does nothing - cannot fill a line (does update the polygon) <br> 
 * graphic.draw(p); // draws a line; <br>
 * p.npoints = 5; //not allowed, but no error yet. <br>
 * try{graphic.draw(p)}catch(Exception e) {}// error - IndexOutOfBoundsException <br>
 * p.npoints = 4; <br>
 * p.xpoints[0] = 10; p.ypoints[0] = 10;  // makes it a weard looking rectangle; <br>
 * Rectangle bounds = p.getBounds(); //the bounds is <b> incorrect </b>.  It is the bounds of when p.npoints was set to 2. <br>
 * boolean contains = p.contains(30, 30); //will return false.  Should return true.  The shape is still set to when the p.npoins = 2 <br>
 * p.invalidate(); //updates the the polygon <br>
 * Rectangle bounds = p.getBounds(); //returns the correct bounds <br>
 * boolean contains = p.contains(30, 30); //returns true <br>
 * <p>
 * If the polygon dosen't draw right make sure that your coordinates and npoints are right.  The polygon does not update itself
 * by virtue of changing its coordinates directly or using the translate(float deltaX, float deltaY) method.  The polygon <b> is </b>
 * updated just before every draw (when the getPathIterator(AffineTransform at) method is called).  This has several implications.
 * All the contains, intersects, and getBounds methods go off the state the polygon was after its <b> last </b> update.  So if any of
 * these methods are called after you have manipulated the polygon and before you have redrawn it, then these specific methods return innacurate
 * results, becuase the polygon has not been updated yet.  If such a case occurs, call the invalidate() method to update the polygon forcibly.
 * The documentation for the methods of Polygon2D are coppied straight out of java.awt.Shape. 
 * @author Christopher Colon 
 * @version 1.0
 */
public abstract class Polygon2D implements Shape
{
    protected GeneralPath path;
    
    public Polygon2D()
    {
       path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    }
    /**Call this method after you are done with direct manipulation of the x and y points (including translate) and if you plan
     * to use any of the contains, intersects, or get bounds methods right after the
     * manipulation.  In effect this method 'updates' the polygon to its position and shape.  The polygon is always updated
     * just before drawing, so if you use any of the contains, intersects, or get bounds method
     * right after drawing and before manipulation, then these methods return accurate results.
     * Its in the period between manipulation and drawing that this method should be called if you
     * plan to use the other methods (excluding translate) before drawing.  */
    public abstract void invalidate();
    
    /**Tests if the specified coordinates are inside the boundary of the Shape. 
     * @param x - the specified x coordinate
     * @param y - the specified y coordinate 
     * @return true if the specified coordinates are inside the Shape boundary; false otherwise.*/
    public boolean contains(double x, double y) 
        {return path.contains(x, y);} 
    
    /**Tests if the interior of the Shape entirely contains the specified rectangular area. All coordinates that lie inside the rectangular area must lie within the Shape for the entire rectanglar area to be considered contained within the Shape. 
     * This method might conservatively return false when: <br>
     * <p>
     * the intersect method returns true and <br>
     * the calculations to determine whether or not the Shape entirely contains the rectangular area are prohibitively expensive. <br>
     * <p>
     * This means that this method might return false even though the Shape contains the rectangular area. 
     * The Area class can be used to perform more accurate computations of geometric intersection for any Shape 
     * object if a more precise answer is required. 
     * @param x - the x coordinate of the specified rectangular area
     * @param y - the y coordinate of the specified rectangular area
     * @param w - the width of the specified rectangular area
     * @param h - the height of the specified rectangular area 
     * @return true if the interior of the Shape entirely contains the specified rectangular area; false otherwise or, if the Shape 
     *          contains the rectangular area and the intersects method returns true and the containment calculations would be too 
     *          expensive to perform.*/
    public boolean contains(double x, double y, double w, double h) 
        {return path.contains(x,y,w,h);}
    /**Tests if a specified Point2D is inside the boundary of the Shape. 
     * @param p - a specified Point2D 
     * @return true if the specified Point2D is inside the boundary of the Shape; false otherwise.*/    
    public boolean contains(Point2D p)
        {return path.contains(p);}
    /**Tests if the interior of the Shape entirely contains the specified rectangular area. All coordinates that lie inside the rectangular area must lie within the Shape for the entire rectanglar area to be considered contained within the Shape. 
     * This method might conservatively return false when: <br>
     * <p>
     * the intersect method returns true and <br>
     * the calculations to determine whether or not the Shape entirely contains the rectangular area are prohibitively expensive. <br>
     * <p>
     * This means that this method might return false even though the Shape contains the rectangular area. 
     * The Area class can be used to perform more accurate computations of geometric intersection for any Shape 
     * object if a more precise answer is required. 
     * @param r - The specified Rectangle2D 
     * @return true if the interior of the Shape entirely contains the specified rectangular area; false otherwise or, if the Shape 
     *          contains the rectangular area and the intersects method returns true and the containment calculations would be too 
     *          expensive to perform.*/
    public boolean contains(Rectangle2D r)
        {return path.contains(r);}
    /**Returns an integer Rectangle that completely encloses the Shape. Note that there is no guarantee that the returned Rectangle is 
     * the smallest bounding box that encloses the Shape, only that the Shape lies entirely within the indicated Rectangle. 
     * The returned Rectangle might also fail to completely enclose the Shape if the Shape overflows the limited range of the integer 
     * data type. The getBounds2D method generally returns a tighter bounding box due to its greater flexibility in representation. 
     * @return an integer Rectangle that completely encloses the Shape.*/
    public Rectangle getBounds()
        {return path.getBounds();}
    /**Returns a high precision and more accurate bounding box of the Shape than the getBounds method. Note that there is no 
     * guarantee that the returned Rectangle2D is the smallest bounding box that encloses the Shape, only that the Shape lies 
     * entirely within the indicated Rectangle2D. The bounding box returned by this method is usually tighter than that returned by 
     * the getBounds method and never fails due to overflow problems since the return value can be an instance of the Rectangle2D that 
     * uses double precision values to store the dimensions.
     * @return an instance of Rectangle2D that is a high-precision bounding box of the Shape.*/
    public Rectangle2D getBounds2D()
        {return path.getBounds2D();}
    /**Returns an iterator object that iterates along the Shape boundary and provides access to the geometry of the Shape outline. 
     * If an optional AffineTransform is specified, the coordinates returned in the iteration are transformed accordingly. 
     * Each call to this method returns a fresh PathIterator object that traverses the geometry of the Shape object independently 
     * from any other PathIterator objects in use at the same time. 
     * It is recommended, but not guaranteed, that objects implementing the Shape interface 
     * isolate iterations that are in process from any changes that might occur to the original object's geometry during such iterations. 
     * Before using a particular implementation of the Shape interface in more than one thread simultaneously, refer 
     * to its documentation to verify that it guarantees that iterations are isolated from modifications. 
     * @param at - an optional AffineTransform to be applied to the coordinates as they are returned in the iteration, 
     *              or null if untransformed coordinates are desired 
     * @return a new PathIterator object, which independently traverses the geometry of the Shape.*/   
    public abstract PathIterator getPathIterator(AffineTransform at);
     /**Returns an iterator object that iterates along the Shape boundary and provides access to a flattened view of the Shape 
      * outline geometry. Only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types are returned by the iterator. 
      * If an optional AffineTransform is specified, the coordinates returned in the iteration are transformed accordingly. 
      * The amount of subdivision of the curved segments is controlled by the flatness parameter, which specifies the maximum 
      * distance that any point on the unflattened transformed curve can deviate from the returned flattened path segments. 
      * Note that a limit on the accuracy of the flattened path might be silently imposed, causing very small flattening parameters 
      * to be treated as larger values. This limit, if there is one, is defined by the particular implementation that is used. 
      * Each call to this method returns a fresh PathIterator object that traverses the Shape object geometry independently 
      * from any other PathIterator objects in use at the same time. It is recommended, but not guaranteed, that objects implementing 
      * the Shape interface isolate iterations that are in process from any changes that might occur to the original object's geometry 
      * during such iterations. Before using a particular implementation of this interface in more than one thread simultaneously, 
      * refer to its documentation to verify that it guarantees that iterations are isolated from modifications. 
      * @param at - an optional AffineTransform to be applied to the coordinates as they are returned in the iteration, or 
      *              null if untransformed coordinates are desired
      * @param flatness - the maximum distance that the line segments used to approximate the curved segments are 
      *                      allowed to deviate from any point on the original curve 
      * @return a new PathIterator that independently traverses the Shape geometry.*/
    public abstract PathIterator getPathIterator(AffineTransform at, double flatness);
    /** Tests if the interior of the Shape intersects the interior of a specified rectangular area. The rectangular area is 
     * considered to intersect the Shape if any point is contained in both the interior of the Shape and the specified rectangular area. 
     * This method might conservatively return true when: <br>
     * there is a high probability that the rectangular area and the Shape intersect, but  <br>
     * the calculations to accurately determine this intersection are prohibitively expensive. <br>
     * This means that this method might return true even though the rectangular area does not intersect the Shape. <br>
     * The Area class can be used to perform more accurate computations of geometric intersection for any Shape object if a more 
     * precise answer is required. 
     * @param x - the x coordinate of the specified rectangular area
     * @param y - the y coordinate of the specified rectangular area
     * @param w - the width of the specified rectangular area
     * @param h - the height of the specified rectangular area 
     * @return true if the interior of the Shape and the interior of the rectangular area intersect, or are 
     *         both highly likely to intersect and intersection calculations would be too expensive to perform; false otherwise.*/
    public boolean intersects(double x, double y, double w, double h)
        {return path.intersects(x, y, w, h);}
    /**Tests if the interior of the Shape intersects the interior of a specified Rectangle2D. 
     * This method might conservatively return true when: <br>
     * there is a high probability that the Rectangle2D and the Shape intersect, but <br>
     * the calculations to accurately determine this intersection are prohibitively expensive. <br>
     * This means that this method might return true even though the Rectangle2D does not intersect the Shape. 
     * @param r - the specified Rectangle2D 
     * @return true if the interior of the Shape and the interior of the specified Rectangle2D intersect, or are both 
     *         highly likely to intersect and intersection calculations would be too expensive to perform; false otherwise.*/
    public boolean intersects(Rectangle2D r)
        {return path.intersects(r);}
    
    public static class Float extends Polygon2D implements Cloneable
    {
        public float xpoints[];
        public float ypoints[];
        public int npoints;
        
        /**Creates a new empty polygon. The array of xpoints and ypoints is set to a size of 4.  So long as npoints remain 0
         * then everthing is fine.  The addPoint(float x, float y) appends the coordinates to xpoints[npoints+1] and ypoints[npoints+1]
         * and then increments npoints.*/
        public Float()
        {
            xpoints = new float[4];
            ypoints = new float[4];
            npoints = 0;
        }
        /**Creates a new empty polygon. The array of xpoints and ypoints is set to a size of the expectedCapacity.  So long as npoints remain 0
         * then everthing is fine.  The addPoint(float x, float y) appends the coordinates to xpoints[npoints+1] and ypoints[npoints+1]
         * and then increments npoints.
         * @throws NegativeArraySizeException If expectedCapacity < 0*/
        public Float(int expectedCapacity)
        {
            if(expectedCapacity < 0) throw new NegativeArraySizeException("negative array size");
            xpoints = new float[expectedCapacity];
            ypoints = new float[expectedCapacity];
            npoints = 0;
        }
        /**Creates a new polygon that represent the given specifications.*/
        public Float(float[] x, float[] y, int npoints)
        {
            if(npoints < 0) throw new NegativeArraySizeException("negative amount of sides (npoints < 0)");
            if(npoints > x.length || npoints > y.length) throw new IndexOutOfBoundsException("more sides than points");
            if(x == null || y == null) throw new NullPointerException("null array of points");
            
            this.xpoints = x;
            this.ypoints = y;
            this.npoints = npoints;
            constructPath();
        }
        /**Creates a new polygon that represent the given specifications.  The integers are coppied over as float values.*/
        public Float(int[] x, int[] y, int npoints)
        {
            if(npoints < 0) throw new NegativeArraySizeException("negative amount of sides (npoints < 0)");
            if(npoints > x.length || npoints > y.length) throw new IndexOutOfBoundsException("more sides than points");
            if(x == null || y == null) throw new NullPointerException("null array of points");
            
            xpoints = new float[x.length];
            for(int index = 0; index < x.length; index++) xpoints[index] = x[index];
            ypoints = new float[y.length];
            for(int index = 0; index < y.length; index++) ypoints[index] = y[index];
            this.npoints = npoints;
            constructPath();            
       }
       /*Updates the polygon.*/     
       private synchronized void constructPath()
         {
            if(npoints < 0) throw new NegativeArraySizeException("negative amound of sides (nPoints < 0)");
            path.reset();
            if(xpoints.length == 0 || ypoints.length == 0) return;
            path.moveTo(xpoints[0], ypoints[0]);
            for(int index = 0; index < npoints; index++)
                path.lineTo(xpoints[index],ypoints[index]);
            path.closePath();
        }
        /**The addPoint(float x, float y) appends the coordinates to xpoints[npoints+1] and ypoints[npoints+1]
         * and then increments npoints.*/
        public synchronized void addPoint(float x, float y)
        {
            if(xpoints.length == npoints)
            {
                float[] temp = new float[xpoints.length+1];
                for(int index = 0; index < xpoints.length; index++) temp[index] = xpoints[index];
                xpoints = temp;
            }
            if(ypoints.length == npoints)
            {
                float[] temp = new float[ypoints.length+1];
                for(int index = 0; index < ypoints.length; index++) temp[index] = ypoints[index];
                ypoints = temp;
            }
            xpoints[npoints+1] = x;
            ypoints[npoints+1] = y;
            npoints++;
        }
        /**Rotate the polygon by the specified amount of degrees around the specified pivot point. It is entirly possible
         * that the polygon could shrink a little or grow a little, after repeated calls to this method.  This is due to
         * rounding errors (double to float).  If the polygon shrinks a little, it will expand a little the next time and vise versa.  
         * The net result is that the polygon is always within 5 (just an estimate) pixels of the intended polygon rotation. So
         * the polygon will never expand too much or shrink too much.  In general the greatest shrinkage or expansion occurs
         * when the polygon is rotated about its center.*/
        public void rotate(double deltaTheta, double pivotX, double pivotY)
        {
            while(deltaTheta >= 360) deltaTheta -= 360;
            while(deltaTheta < 0) deltaTheta += 360;
            for(int index = 0; index < npoints; index++)
            {
                if(xpoints[index] == pivotX && ypoints[index] == pivotY) continue;
                double distance = Point.distance(xpoints[index], ypoints[index], pivotX, pivotY);
                double angle = Math.atan( -(ypoints[index] - pivotY) / (xpoints[index] - pivotX) );
                if((xpoints[index] - pivotX) < 0) angle += Math.PI;
                angle += Math.toRadians(deltaTheta);
                xpoints[index] = (float) (Math.cos(angle) * distance + pivotX);
                ypoints[index] = (float) (-Math.sin(angle) * distance + pivotY);
            }
        }
        /**Rotate the polygon about its center by the specified amount of degrees.  This is the equivalent of calling
         * rotate(deltaTheta, (float) getXMid(), (float) getYMid()).*/
        public void rotate(double deltaTheta)
        {
            rotate(deltaTheta, getXMid(), getYMid());
        }
        /**Resize the polygon by the specified factor.  A factor of 1 will not change the polygon.
         * A factor greater than 1 will make the polygon grow.  A factor between 0 and 1 will make the polygon shrink.
         * The polygon shrinks towards its center or grows away from its center.
         * @throws IllegalArgumentException if the factor is less than 0.*/
        public void resize(double factor)
        {
            if(factor < 0) throw new IllegalArgumentException("illegal factor");
            if(factor == 1) return;
            double xMid = getXMid();
            double yMid = getYMid();
            for(int index = 0; index < npoints; index++)
            {
                xpoints[index] = (float) (((xpoints[index] - xMid) * factor) + xMid);
                ypoints[index] = (float) (((ypoints[index] - yMid) * factor) + yMid);
            }
        }
        /**Returns the x coordinate of the mid point (center) of this polygon.*/
        public double getXMid()
        {
            double sum = 0;
            for(int index = 0; index < npoints; index++) sum += xpoints[index];
            return sum / npoints;
        }
        /**Returns the y coordinate of the mid point (center) of this polygon.*/
        public double getYMid()
        {
            double sum = 0;
            for(int index = 0; index < npoints; index++) sum += ypoints[index];
            return sum / npoints;
        }
        /**Resets this polygon to an empty polygon.*/
        public synchronized void reset()
        {
            xpoints = new float[xpoints.length];
            ypoints = new float[ypoints.length];
            npoints = 0;
        }
         /**Translates the polygon by the specified x and y amounts.  The polygon is not updated
          * after the transformation.*/
        public void translate(float deltaX, float deltaY)
        {
            for(int index = 0; index < xpoints.length; index++) xpoints[index] += deltaX;
            for(int index = 0; index < ypoints.length; index++) ypoints[index] += deltaY;
        }
        public void invalidate()
             {constructPath();}
        public PathIterator getPathIterator(AffineTransform at)
        {   constructPath();
            return path.getPathIterator(at);}
        public PathIterator getPathIterator(AffineTransform at, double flatness)
        {   constructPath();
            return path.getPathIterator(at, flatness);}
        
        /**Returns a polygon with the same shape and points.*/
        public Object clone()
        {
            return new Polygon2D.Float(xpoints, ypoints, npoints);
        }
    }   
}