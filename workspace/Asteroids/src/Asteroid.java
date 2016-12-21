import java.awt.Polygon;

public class Asteroid extends GameObject
{
    private int size_ = 0;    
       
    protected int size() { return size_; }
    
    /**
     * Generic asteroid constructor.
     * 
     * @param x x position in window units
     * @param y y position in window units
     * @param size asteroid size
     */
    public Asteroid( double x, double y, int size )
    {
        super( x, y, 
            1.1*Math.random( ),
            1.1*Math.random( ),
            2.0*Math.PI*Math.random(),
            0.01*Math.PI*Math.random(),
            0.0,
            asteroidShapeGenerator( size ));
        size_ = size;
    }
    
    public Asteroid( double x, double y, int size, double bearingDot )
    {
        super( x, y, 
            0.0,
            0.0,
            0.0,
            bearingDot,
            0.0,
            asteroidShapeGenerator( size ));        
        size_ = size;
    }
    public Asteroid( double x, double y )
    {
        this( x, y, 3 );
    }
    
    /**
     * Asteroid constructor. Inherit location from the input asteroid, but utilize 
     * the passed in bearing.
     * 
     * @param a
     * @param newBearing
     */
    protected Asteroid( Asteroid a, double newBearing )
    {
        super( 
            a.x(), a.y(), 
            a.xDot() * Math.cos( newBearing ) + 0.5,
            a.yDot() * Math.sin( newBearing ) + 0.5,
            newBearing,
            0.01*Math.PI*Math.random(),
            0.0, 
            asteroidShapeGenerator( a.size() - 1 ));
        size_ = a.size() - 1; 
    }
    
    /**
     * Break this asteroid down into two smaller asteroids, and shoot both off in
     * orthogonal directions.
     * @return
     */
    public Asteroid[] explode()
    {
        if ( size_ > 0 )
        {
            Asteroid[] ret = new Asteroid[2];
            ret[0] = new Asteroid( this, bearing() + ( Math.PI / 2.0 ));
            ret[1] = new Asteroid( this, bearing() - ( Math.PI / 2.0 ));
            return ret;
        }
        return null;
    }
    
    /**
     * Generate an asteroid shape.
     * 
     * @param size
     * @return
     */
    public static Polygon asteroidShapeGenerator( int size )
    {
        // Take 8-24 points on a unit circle, and deform each of them 
        // in or out along the radius by a random amount.
        //
        // Not terribly happy w/ this.
        //
        final int numPoints = (int)Math.round( 16.0 * Math.random()) + 8;
        final int radius = 8 * (size+1);                  
        
        // deform it.               
        Polygon p = new Polygon( );
        for ( int ii = 0; ii < numPoints; ++ii )
        {
            // deformation factors: [-1.0 : 1.0 ];
            double eccentricity = 0.45;
            double deformationFactorX = ( radius + ( radius * eccentricity * ( Math.random() * 2.0 - 1.0 )));
            double deformationFactorY = ( radius + ( radius * eccentricity * ( Math.random() * 2.0 - 1.0 )));
            
            double xPos = deformationFactorX * Math.cos( (double)ii * 2.0 * Math.PI / (double)numPoints ); 
            double yPos = deformationFactorY * Math.sin( (double)ii * 2.0 * Math.PI / (double)numPoints );            
            
            p.addPoint( (int)xPos, (int)yPos );
        }
                      
        return p;        
    }
    
    /**
     * 25 points per asteroid size.
     * 
     * @return
     */
    public long score()
    {
        return ( 4 - size_ ) * 25;
    }    
}
