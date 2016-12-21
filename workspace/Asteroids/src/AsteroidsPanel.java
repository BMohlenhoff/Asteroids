import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class AsteroidsPanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int h_;
    private int w_;
    
    private Ship ship_;
    private LinkedList<Bullet> bullets_;
    private LinkedList<Asteroid> asteroids_;
    private KeyboardListener listener_;
    
    private Asteroid[] pointDisplay = {
        new Asteroid( 0.0, 0.0, 3, 0.0 ),
        new Asteroid( 0.0, 0.0, 2, 0.0 ),
        new Asteroid( 0.0, 0.0, 1, 0.0 ),
        new Asteroid( 0.0, 0.0, 0, 0.0 )   
    };   
    
    private long gameStartTime_ = 0;
    private long gameOverTime_ = 0;
    private long lastDebugTime_ = 0;
    private final long DEBUG_COOLDOWN_ = 250;
    
    private long lastSwapTime_ = 0;
    private final long SWAP_COOLDOWN_ = 1000;
    
    private Timer renderThread_ = null;
    
    private long score_ = 0;
    
    private enum GameMode
    {
        IN_PROGRESS,
        TITLE,
        GAME_OVER,
        PAUSE
    }
    private GameMode gameMode_ = GameMode.TITLE;
    
    public AsteroidsPanel()
    {
        super();              
        
        gameSetup();        
    }
    
    private void gameSetup()
    {
        Dimension d = new Dimension( 600, 600 );
        setPreferredSize( d );
        w_ = d.width;
        h_ = d.height;
        gameMode_ = GameMode.TITLE;
        listener_ = new KeyboardListener(this);
        
        renderThread_ = new Timer();
        renderThread_.schedule( new TimerTask() { public void run() { gameStep(); repaint(); }}, 0l, 16l ); // 60fps
    }
    
    private void initPlay()
    {
        ship_ = new Ship( 0.5 * (double)w_, 0.5 * (double)h_ );
        gameMode_ = GameMode.IN_PROGRESS;
        bullets_ = new LinkedList<Bullet>();
        asteroids_ = new LinkedList<Asteroid>();
        score_= 0;
        gameOverTime_ = System.currentTimeMillis();
        gameStartTime_= System.currentTimeMillis();
        lastDebugTime_ = System.currentTimeMillis();
        lastSwapTime_ = System.currentTimeMillis();
    }
    
    private void gameStep()
    {
        processInput();    
        if ( gameMode_ == GameMode.IN_PROGRESS )
        {        
            propagateObjects();
            collisionDetection();
            
            if ( asteroids_.size() == 0 )
            {
                // Spawn new asteroids
                for ( int ii = 0; ii < 4; ++ii )
                {
                    spawnAsteroid();
                }
            }
        }
    }
    
    /**
     * Spawn an asteroid somewhere away from the ship. Try 4 times, 
     * and if it still doesn't work then just spawn the asteroid anywhere.
     * 
     */
    private void spawnAsteroid()
    {
        spawnAsteroid( 3 );
    }
    private void spawnAsteroid( int size )
    {
        // Draw safe area around the ship.
        Rectangle r = ship_.getBounds();
        r.x -= 50;
        r.width += 100;
        r.y -= 50;
        r.height += 100;
        
        boolean done = false;
        for ( int ii = 0; ii < 4 && !done; ++ii )
        {
            double x = Math.random() * w_;
            double y = Math.random() * h_;            
            if ( !r.contains( new Point( (int)x, (int)y )))
            {
                asteroids_.add( new Asteroid( x, y, size ));
                done = true;
            }            
        }
        if ( !done )
        {
            System.out.println("test");
            asteroids_.add( new Asteroid( Math.random() * (double)w_, Math.random() * (double)h_, size ));
        }
    }
    
    private void processInput()
    {
        if ( gameMode_ == GameMode.TITLE || gameMode_ == GameMode.GAME_OVER )
        {
            if ( System.currentTimeMillis() > gameOverTime_ + 2000 && 
                listener_.isKeyPressed( KeyEvent.VK_SPACE ))
            {
                initPlay();
            }
        }
        else if ( gameMode_ == GameMode.IN_PROGRESS )
        {
            if ( listener_.isKeyPressed( KeyEvent.VK_E ))
            {
                ship_.thrust();
            }
            if ( listener_.isKeyPressed( KeyEvent.VK_S ))
            {
                ship_.rotate( -1.0 );
            }
            else if ( listener_.isKeyPressed( KeyEvent.VK_F ))
            {
                ship_.rotate( 1.0 );           
            }
                        
            if ( listener_.isKeyPressed( KeyEvent.VK_SPACE ))
            {
                List<Bullet> newBullets = ship_.fire();                 
                bullets_.addAll( ship_.fire());                
            }
            
            if ( lastSwapTime_ + SWAP_COOLDOWN_ < System.currentTimeMillis())
            {
                if ( listener_.isKeyPressed( KeyEvent.VK_1 ) && ship_.weaponType() != Ship.WeaponType.NORMAL )
                {
                    ship_.weaponType( Ship.WeaponType.NORMAL );
                    lastSwapTime_ = System.currentTimeMillis();
                }
                if ( listener_.isKeyPressed( KeyEvent.VK_2 ) && ship_.weaponType() != Ship.WeaponType.SPREAD )
                {
                    ship_.weaponType( Ship.WeaponType.SPREAD );
                    lastSwapTime_ = System.currentTimeMillis();
                }
                if ( listener_.isKeyPressed( KeyEvent.VK_3 ) && ship_.weaponType() != Ship.WeaponType.OCTOPUS )
                {
                    ship_.weaponType( Ship.WeaponType.OCTOPUS );
                    lastSwapTime_ = System.currentTimeMillis();
                }
            }
            
            if ( lastDebugTime_ + DEBUG_COOLDOWN_ < System.currentTimeMillis())
            {
             
                if ( listener_.isKeyPressed( KeyEvent.VK_P ))
                {
                    DebugManager.instance().godModeToggle();
                    lastDebugTime_ = System.currentTimeMillis();
                }
                if(  listener_.isKeyPressed( KeyEvent.VK_Q ))
                {
                    DebugManager.instance().boundingBoxesEnabledToggle();
                    lastDebugTime_ = System.currentTimeMillis();
                }
                
                // Allow spawning of random asteroids during god mode
                if ( DebugManager.instance().godMode())
                {       
                    if ( listener_.isKeyPressed( KeyEvent.VK_7 ))
                    {
                        spawnAsteroid( 3 );
                        lastDebugTime_ = System.currentTimeMillis();
                    }
                    if ( listener_.isKeyPressed( KeyEvent.VK_8 ))
                    {
                        spawnAsteroid( 2 );
                        lastDebugTime_ = System.currentTimeMillis();
                    }
                    if ( listener_.isKeyPressed( KeyEvent.VK_9 ))
                    {
                        spawnAsteroid( 1 );
                        lastDebugTime_ = System.currentTimeMillis();
                    }                                
                    if ( listener_.isKeyPressed( KeyEvent.VK_0 ))
                    {
                        spawnAsteroid( 0 );
                        lastDebugTime_ = System.currentTimeMillis();                    
                    }
                }
            }
        }
    }
    private void propagateObjects()
    {
        Dimension d = getSize();
        ship_.propagate( d );
        
        Iterator<Bullet> itB = bullets_.iterator();
        while ( itB.hasNext())            
        { 
            Bullet bill = itB.next();
            if ( bill.purge())
            {
                itB.remove();
            }
            else
            {
                bill.propagate( d );   
            }           
        }
        for ( Asteroid a : asteroids_ ) { a.propagate( d ); }        
    }
    
    private void gameOver()
    {        
        // play amusing sound effect
        gameOverTime_ = System.currentTimeMillis();
        gameMode_ = GameMode.GAME_OVER;
    }
    
    private void collisionDetection()
    {
        // Ship with asteroids. Disable if godMode enabled.
        if ( !DebugManager.instance().godMode())
        {
            Iterator<Asteroid> itA = asteroids_.iterator();
            Rectangle r = ship_.getBounds();
            while ( itA.hasNext())
            {
                Asteroid a = itA.next();
                if ( a.getBounds().intersects( r ))
                {
                    gameOver();
                }
            }
        }
        
        // Bullets with asteroids
        Iterator<Asteroid> itA = asteroids_.iterator();
        LinkedList<Asteroid> newAsteroids = new LinkedList<Asteroid>();
        
        // Shitty n^2 collision algorithm
        while ( itA.hasNext())
        {
            Asteroid a = itA.next();
            Iterator<Bullet> itB = bullets_.iterator();
            boolean done = false;
            while ( itB.hasNext() && !done )
            {
                Bullet b = itB.next();
                if ( a.getBounds().contains( b.getBounds()))
                {
                    done = true;
                    Asteroid[] n = a.explode();
                    if ( n != null )
                    {
                        for ( int ii = 0; ii < n.length; ++ii )
                        {
                            newAsteroids.add( n[ii]);
                        }
                    }
                    
                    // Don't count the score if godMode is enabled, you cheater.
                    if ( !DebugManager.instance().godMode())
                    {
                        score_ += a.score(); 
                    }
                    
                    itB.remove();
                    itA.remove();                                       
                }
            }
        }
        for ( Asteroid a : newAsteroids ) { asteroids_.add( a ); }                            
    }
    
    protected void paintComponent( Graphics g )
    {
        super.paintComponent(g);
        
        Dimension d = getSize();
        w_ = d.width;
        h_ = d.height;        
        
        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor( Color.BLACK );
        g2d.fillRect( 0,  0,  w_,  h_ );
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
        
        switch ( gameMode_ )
        {
        case TITLE:
        {
            g2d.setColor( Color.YELLOW );
            Font ogFont = g2d.getFont();
            AffineTransform ogXform = g2d.getTransform();
            
            g2d.setFont( new Font( "Arial", 1, 36 ));
            centerTextX( g2d, "ASTEROIDS", 0.5*(double)w_, 0.5*(double)h_ );
            
            g2d.setFont( new Font( "Arial", 1, 12 ));
            centerTextX( g2d, "Press space to start", 0.5*(double)w_, 0.75*(double)h_ );
            
            g2d.setFont( ogFont );

            drawPointValues( g2d );
            drawControls( g2d );
            
            g2d.setTransform( ogXform );
            g2d.setFont( ogFont );
        }
            break;
        case GAME_OVER:
        {
            Font ogFont = g2d.getFont();

            g2d.setColor( Color.RED );
            g2d.setFont( new Font( "Arial", 1, 36 ));
            centerTextX( g2d, "Game Over", 0.5*(double)w_, 0.5*(double)h_ );
            
            // Pause briefly in between games.
            g2d.setFont( new Font( "Arial", 1, 14 ));
            g2d.setColor( Color.WHITE );
            if ( System.currentTimeMillis() > gameOverTime_ + 2000 )
            {
                centerTextX( g2d, "Press space to start", 0.5*(double)w_, 0.7*(double)h_);
            }
            
            centerTextX( g2d, "Score: " + String.valueOf( score_ ), 0.5*(double)w_, 0.85*(double)h_ );
            
            long elapsedTimeSec = ( gameOverTime_ - gameStartTime_ ) / 1000 ;
            centerTextX( g2d, "Time Wasted: " + String.valueOf( elapsedTimeSec ) + " seconds", 0.5*(double)w_, 0.88*(double)h_ );
                                               
            g2d.setFont( ogFont );
            drawPointValues( g2d );                        
            g2d.setFont( ogFont );
            break;
        }
        case PAUSE:
            break;
        case IN_PROGRESS:
        {
            g2d.setColor( Color.WHITE );
            
            // Draw ship
            ship_.draw( g2d, d );
            
            // Draw bullets
            for ( Bullet bill : bullets_ )
            {
                bill.draw( g2d, d );
            }
                    
            // Draw asteroids
            for ( Asteroid a : asteroids_ )
            {
                a.draw( g2d, d );
            }
            
            g2d.setColor( Color.GREEN );
            g2d.setFont( new Font( "Arial" , 1, 12 ));
            g2d.drawString( "Score: " + String.valueOf( score_ ), 0, 12 );
            
            g2d.drawString( "Weapon: " + ship_.weaponType().toString(), 0, 24 );
                
            if ( DebugManager.instance().godMode())
            {
                g2d.drawString( "God Mode enabled. CHEATER!!!", 0, 36 );
            }
            break;
        }
        default:
            break;
        }
               
    }
    
    /**
     * Render control information on the graphics object.
     * @param g2d
     */
    private void drawControls( Graphics2D g2d )
    {               
        centerTextX(g2d, "Thrust:     E", 0.5*(double)w_, 0.8*(double)h_);
        centerTextX(g2d, "Rotate CCW: S", 0.5*(double)w_, 0.83*(double)h_ );
        centerTextX(g2d, "Rotate CW:  F", 0.5*(double)w_, 0.86*(double)h_ );
        centerTextX( g2d, "Fire:  Space", 0.5*(double)w_, 0.89*(double)h_ );                  
    }
    
    /** 
     * Render the asteroid shaped and associated point values.
     * @param g2d
     */
    private void drawPointValues( Graphics2D g2d )
    {
        AffineTransform og = g2d.getTransform();
                               
        g2d.transform( AffineTransform.getTranslateInstance( 0.35 * (double)w_, 0.20 * (double)h_ ));
        g2d.draw( pointDisplay[0].p());
        g2d.setTransform( og );
        centerTextX( g2d, "25 points", 0.37*(double)w_, 0.15*(double)h_ );
        
        g2d.transform( AffineTransform.getTranslateInstance( 0.65 * (double)w_, 0.20 * (double)h_ ));
        g2d.draw( pointDisplay[1].p());
        g2d.setTransform( og );
        centerTextX( g2d, "50 points", 0.67*(double)w_, 0.15*(double)h_ );
        
        g2d.transform( AffineTransform.getTranslateInstance( 0.35 * (double)w_, 0.30 * (double)h_ ));
        g2d.draw( pointDisplay[2].p());
        g2d.setTransform( og );
        centerTextX( g2d, "75 points", 0.37*(double)w_, 0.28*(double)h_ );
        
        g2d.transform( AffineTransform.getTranslateInstance( 0.65 * (double)w_, 0.30 * (double)h_ ));
        g2d.draw( pointDisplay[3].p());
        g2d.setTransform( og );
        centerTextX( g2d, "100 points", 0.67*(double)w_, 0.28*(double)h_ );
        
        g2d.setTransform( og );
    }

    private void centerTextX( Graphics2D g2d, String s, double x, double y )
    {        
        FontMetrics fm = g2d.getFontMetrics( g2d.getFont());
        int width = fm.stringWidth( s );
        g2d.drawString( s,  (int)(x - ( width / 2 )), (int)( y ));
    }
}
