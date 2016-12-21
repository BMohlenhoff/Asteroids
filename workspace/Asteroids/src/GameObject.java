import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public class GameObject
{
    protected double x_;
    protected double y_;
    protected double xDot_;
    protected double yDot_;
    protected double bearing_;
    protected double bearingDot_;
    protected double drag_;
    protected Polygon p_;
    
    private long lastUpdate_;
    private long creationTime_;        
    
    public double x() { return x_; }
    public double y() { return y_; }
    public double xDot() { return xDot_; }
    public double yDot() { return yDot_; }
    public double bearing() { return bearing_; }
    public double bearingDot() { return bearingDot_; }
    public long creationTime() { return creationTime_; }
    public Polygon p() { return p_; }
    
    protected GameObject( 
        double x, double y, 
        double xDot, double yDot,
        double bearing, double bearingDot,
        double drag,
        Polygon p )
    {
        x_ = x;
        y_ = y;
        xDot_ = xDot;
        yDot_ = yDot;
        bearing_ = bearing;
        bearingDot_ = bearingDot;
        drag_ = drag;
        p_ = p;
        
        lastUpdate_ = System.currentTimeMillis();
        creationTime_ = System.currentTimeMillis();
        
//        System.out.println( "ctor: " + this );
    }
    
    /**
     * Propagate this game object.
     * 
     * @param d
     */
    public void propagate( Dimension d )
    {
        double w = (double)d.width;
        double h = (double)d.height;
        
        double elapsed = ( System.currentTimeMillis() - lastUpdate_ ) / 1000.0;
        
        // Propagate new x position
        x_ += xDot_;
        y_ += yDot_;        
        
        // Account for drag coefficient
        xDot_ -= ( xDot_ * drag_ );
        yDot_ -= ( yDot_ * drag_ );       
        
        // Account for any rotation.
        bearing_ += bearingDot_;
        
        // Bound rotation from [0, 2*pi]
        if ( bearing_ > 2.0 * Math.PI ) { bearing_ -= ( 2.0 * Math.PI ); }
        if ( bearing_ < 0.0 ) { bearing_ += ( 2.0 * Math.PI ); }
        
        // Bound x/y to the size of the window.
        if ( x_ > w ) { x_ -= w; }
        if ( x_ < 0 ) { x_ += w; }
        if ( y_ > h ) { y_ -= h; }
        if ( y_ < 0 ) { y_ += h; }                        
        
//        System.out.println( this );
    }
    
    /**
     * Draw the shape on the graphics object. Wrap it around if the object extends past 
     * one of the window edges.
     * 
     * @param g2d
     * @param d
     */
    public void draw(Graphics2D g2d, Dimension d )
    {
        double h = d.height;
        double w = d.width;
                
        AffineTransform ogXform = g2d.getTransform();
        
        Rectangle r = p_.getBounds();
        
        // Draw object
        g2d.setColor( Color.WHITE );
        g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
        g2d.transform( AffineTransform.getRotateInstance( bearing_ ));
        g2d.draw( p_ );
        g2d.setTransform( ogXform );
        
        // Propagate around edges
        if ( x_ - r.width < 0 )
        {
            g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
            g2d.transform( AffineTransform.getTranslateInstance( w, 0 ));
            g2d.rotate( bearing_ );
            g2d.draw( p_ );            
            g2d.setTransform( ogXform );
        }
        else if ( x_ + r.width > w )
        {
            g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
            g2d.transform( AffineTransform.getTranslateInstance( -w, 0 ));
            g2d.rotate( bearing_ );
            g2d.draw( p_ );
            g2d.setTransform( ogXform );
        }
        if ( y_ - r.height < 0 )       
        {
            g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
            g2d.transform( AffineTransform.getTranslateInstance( 0, h ));
            g2d.rotate( bearing_ );
            g2d.draw( p_ );
            g2d.setTransform( ogXform );
        }
        else if ( y_ + r.height > h )
        {
            g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
            g2d.transform( AffineTransform.getTranslateInstance( 0, -h ));
            g2d.rotate( bearing_ );
            g2d.draw( p_ );
            g2d.setTransform( ogXform );
        }
        
        // debug 
        if ( DebugManager.instance().boundingBoxesEnabled())
        {
            g2d.transform( AffineTransform.getTranslateInstance( x_, y_ ));
            g2d.transform( AffineTransform.getRotateInstance( bearing_ ));
        
            // Bounding box.
            g2d.setColor( Color.YELLOW );
            g2d.draw( r );
        
            // Up vector
            g2d.setColor( Color.GREEN );
            g2d.drawLine( 0,  0,  50, 0 );
        
            // Center point.
            g2d.setColor( Color.RED );
            g2d.drawOval( (int)(r.getCenterX() - 2), (int)(r.getCenterY()-2),  2,  2 );
        }
        
        g2d.setTransform( ogXform );        
    }
    
    public String toString()
    {
        return new String( "GameObject(): x_=" + x_ + " y_=" + y_ + " xDot_=" + xDot_ + " yDot_=" + yDot_ );
    }
    
    public Rectangle getBounds()
    {
        Rectangle r = p_.getBounds();
        r.x += x_;
        r.y += y_;
        return r;
    }
}
