
public class DebugManager
{
    private static DebugManager instance_ = null;
    
    /**
     * If true, collision detection is disabled on the ship
     */
    private boolean godMode_ = false;
    
    /**
     * If true, bounding boxes and up vectors are rendered.
     */
    private boolean boundingBoxesEnabled_ = false;
    
    public static DebugManager instance()
    {
        if ( instance_ == null )
        {            
            instance_ = new DebugManager();            
        }
        return instance_;
    }
    
    protected DebugManager()
    {
        godMode_ = false;
        boundingBoxesEnabled_ = false;
    }
    
    public boolean boundingBoxesEnabled() { return boundingBoxesEnabled_; }
    public void boundingBoxesEnabledToggle( ) { boundingBoxesEnabled_ = !boundingBoxesEnabled_; };
    
    public boolean godMode() { return godMode_; }
    public void godModeToggle() { godMode_ = !godMode_; }
}
