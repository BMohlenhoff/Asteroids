import java.awt.Polygon;
import java.util.LinkedList;
import java.util.List;

public class Ship extends GameObject
{
    private final double ACC_ = 1.5;
    
    public enum WeaponType
    {
        NORMAL, SPREAD, FLAME, LASER
    };
    private WeaponType weaponType_;
    
    /**
     * Construct ship object at window position x.y.
     * @param x
     * @param y
     */
    public Ship( double x, double y )
    {
        super( x, y, 0.0, 0.0, Math.toRadians( -90.0 ), 0.0, 0.1, 
            new Polygon( 
                new int[]{ 18, -9, 0, -9 },
                new int[]{ 0,   9, 0, -9 }, 
                4 ));       
        weaponType_ = WeaponType.NORMAL;
    }
    
    /**
     * Thrust the ship according to the current bearing and the given acceleration factor. 
     */
    public void thrust()
    {
        xDot_ += ACC_ * Math.cos( bearing_ );
        yDot_ += ACC_ * Math.sin( bearing_ );
        
//        System.out.println( "xDot_=" + xDot_ + " x_=" + x_ + " ACC_=" + ACC_ + " bearing_=" + bearing_ );
    }
    
    /**
     * Rotate the ship as per the direction.
     * @param dir 1.0 for clockwise, -1.0 for counterclockwise.
     */
    public void rotate( double dir )
    {
        bearing_ += ( dir * 10.0 * Math.PI / 180.0 );
    }
    
    public WeaponType weaponType() { return weaponType_; }
    public void weaponType( WeaponType type ) { weaponType_ = type; }
    
    /**
     * Spawn bullet.
     * 
     * @return
     */
    public List<Bullet> fire( )
    {               
        switch ( weaponType_ )
        {        
        case SPREAD:
            return Bullet.spreadGenerator( this );
        case NORMAL:            
        default:
            LinkedList<Bullet> l = new LinkedList<Bullet>();
            l.add( new Bullet( this ));
            return l;                        
        }                                        
    }
}
