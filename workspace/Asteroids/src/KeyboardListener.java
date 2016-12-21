import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class KeyboardListener 
{
    private boolean[] keyMap_ = null;
    
    public KeyboardListener(JPanel panel)
    {
        super();
        
        keyMap_ = new boolean[ KeyEvent.KEY_LAST ];
        clearMap();
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, false ), "e pressed" );
        panel.getActionMap().put( "e pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_E, 0, true ), "e released" );
        panel.getActionMap().put( "e released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false ), "s pressed" );
        panel.getActionMap().put( "s pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true ), "s released" );
        panel.getActionMap().put( "s released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false ), "f pressed" );
        panel.getActionMap().put( "f pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, true ), "f released" );
        panel.getActionMap().put( "f released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false ), "space pressed" );
        panel.getActionMap().put( "space pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true ), "space released" );
        panel.getActionMap().put( "space released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, false ), "q pressed" );
        panel.getActionMap().put( "q pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true ), "q released" );
        panel.getActionMap().put( "q released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false ), "p pressed" );
        panel.getActionMap().put( "p pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, true ), "p released" );
        panel.getActionMap().put( "p released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, false ), "1 pressed" );
        panel.getActionMap().put( "1 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, true ), "1 released" );
        panel.getActionMap().put( "1 released", new KeyboardAction( false ));
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, false ), "2 pressed" );
        panel.getActionMap().put( "2 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, true ), "2 released" );
        panel.getActionMap().put( "2 released", new KeyboardAction( false ));        
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, false ), "3 pressed" );
        panel.getActionMap().put( "3 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, true ), "3 released" );
        panel.getActionMap().put( "3 released", new KeyboardAction( false ));  
        
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, false ), "7 pressed" );
        panel.getActionMap().put( "7 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_7, 0, true ), "7 released" );
        panel.getActionMap().put( "7 released", new KeyboardAction( false ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, false ), "8 pressed" );
        panel.getActionMap().put( "8 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_8, 0, true ), "8 released" );
        panel.getActionMap().put( "8 released", new KeyboardAction( false ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_9, 0, false ), "9 pressed" );
        panel.getActionMap().put( "9 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_9, 0, true ), "9 released" );
        panel.getActionMap().put( "9 released", new KeyboardAction( false ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_0, 0, false ), "0 pressed" );
        panel.getActionMap().put( "0 pressed", new KeyboardAction( true ));
        panel.getInputMap().put( KeyStroke.getKeyStroke(KeyEvent.VK_0, 0, true ), "0 released" );
        panel.getActionMap().put( "0 released", new KeyboardAction( false ));
        
    }    
    
    public void clearMap()
    {
        for ( int ii = 0; ii < keyMap_.length; ++ii )
        {
            keyMap_[ii] = false;        
        }
    }

    boolean isKeyPressed( int key )
    {
        return keyMap_[key];
    }
    
    private class KeyboardAction implements javax.swing.Action
    {
        private boolean action_;
        
        public KeyboardAction( boolean action )
        {
            super();
            action_ = action;
        }
        
        @Override
        public void actionPerformed(ActionEvent arg0) 
        {
//            System.out.println( this + ":" + arg0 );
            switch ( arg0.getActionCommand())
            {
            case "e":
                keyMap_[KeyEvent.VK_E] = action_;
                break;
            case "s":
                keyMap_[KeyEvent.VK_S] = action_;
                break;
            case "f":
                keyMap_[KeyEvent.VK_F] = action_;
                break;
            case " ":
                keyMap_[KeyEvent.VK_SPACE] = action_;
                break;   
            case "q":
                keyMap_[KeyEvent.VK_Q] = action_;
                break;
            case "p":
                keyMap_[KeyEvent.VK_P] = action_;
                break;
            case "1":
                keyMap_[KeyEvent.VK_1] = action_;
                break;
            case "2":
                keyMap_[KeyEvent.VK_2] = action_;
                break;
            case "3":
                keyMap_[KeyEvent.VK_3] = action_;
                break;
            case "7":
                keyMap_[KeyEvent.VK_7] = action_;
                break;
            case "8":
                keyMap_[KeyEvent.VK_8] = action_;
                break;
            case "9":
                keyMap_[KeyEvent.VK_9] = action_;
                break;
            case "0":
                keyMap_[KeyEvent.VK_0] = action_;
                break;
            default:
                break;
            }
        }
    
        @Override
        public void addPropertyChangeListener(PropertyChangeListener arg0) {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public Object getValue(String arg0) {
            // TODO Auto-generated method stub
            return null;
        }
    
        @Override
        public boolean isEnabled() {        
            return true;
        }
    
        @Override
        public void putValue(String arg0, Object arg1) {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public void removePropertyChangeListener(PropertyChangeListener arg0) {
            // TODO Auto-generated method stub
            
        }
    
        @Override
        public void setEnabled(boolean arg0) {
            // TODO Auto-generated method stub
            
        }    
    }
}
