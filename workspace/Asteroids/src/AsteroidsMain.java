import javax.swing.JFrame;

public class AsteroidsMain 
{

    public static void main(String[] args) 
    {
        JFrame frame = new JFrame( "Asteroids" );
        frame.add( new AsteroidsPanel());
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);         
	}

}
