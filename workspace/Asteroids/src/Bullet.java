import java.awt.Polygon;
import java.util.LinkedList;
import java.util.List;

public class Bullet extends GameObject
{
    private final static double BULLET_VELOCITY_ = 10.0;
    private long BULLET_LIFETIME_ = 1000;
    
    public static List<Bullet> spreadGenerator( Ship ship )
    {
        long SPREAD_LIFETIME_ = 250;
        
        LinkedList<Bullet> l = new LinkedList<Bullet>();
        l.add( new Bullet( ship.x(), ship.y(), ship.bearing() - ( 10.0 * Math.PI / 180.0 ), SPREAD_LIFETIME_ ));
        l.add( new Bullet( ship.x(), ship.y(), ship.bearing() - ( 5.0 * Math.PI / 180.0 ), SPREAD_LIFETIME_ ));
        l.add( new Bullet( ship.x(), ship.y(), ship.bearing(), SPREAD_LIFETIME_ ));
        l.add( new Bullet( ship.x(), ship.y(), ship.bearing() + ( 5.0 * Math.PI / 180.0 ), SPREAD_LIFETIME_ ));
        l.add( new Bullet( ship.x(), ship.y(), ship.bearing() + ( 10.0 * Math.PI / 180.0 ), SPREAD_LIFETIME_ ));
        return l;
    }
            
    public Bullet( Ship ship )
    {
        super( 
            ship.x() + 10.0 * Math.cos( ship.bearing()), 
            ship.y() + 10.0 * Math.sin( ship.bearing()),
            BULLET_VELOCITY_ * Math.cos( ship.bearing()), 
            BULLET_VELOCITY_ * Math.sin( ship.bearing()),
            ship.bearing(), 
            0.0, // shape rotation
            0.0, // drag coefficient
            new Polygon( new int[]{ -2, 0, 2, 0 }, new int[] { 0, 2, 0, -2 }, 4 ));
    }

    private Bullet( double x, double y, double bearing, long lifetime )
    {
        super( 
            x + 10.0 * Math.cos( bearing ), 
            y + 10.0 * Math.sin( bearing ),
            BULLET_VELOCITY_ * Math.cos( bearing ), 
            BULLET_VELOCITY_ * Math.sin( bearing ),
            bearing, 
            0.0, // shape rotation
            0.0, // drag coefficient
            new Polygon( new int[]{ -2, 0, 2, 0 }, new int[] { 0, 2, 0, -2 }, 4 ));
        BULLET_LIFETIME_ = lifetime;
    }
    
    private Bullet( double x, double y, double bearing )
    {
        super( 
            x + 10.0 * Math.cos( bearing ), 
            y + 10.0 * Math.sin( bearing ),
            BULLET_VELOCITY_ * Math.cos( bearing ), 
            BULLET_VELOCITY_ * Math.sin( bearing ),
            bearing, 
            0.0, // shape rotation
            0.0, // drag coefficient
            new Polygon( new int[]{ -2, 0, 2, 0 }, new int[] { 0, 2, 0, -2 }, 4 ));
    }
    
    public boolean purge()
    {
        return System.currentTimeMillis() > creationTime() + BULLET_LIFETIME_;
    }
}
