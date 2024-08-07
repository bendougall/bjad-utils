package bjad.swing.testapp;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bjad.swing.nav.BJADTitlePanel;

/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class CustomHeaderTestApp extends JFrame
{
   private static final long serialVersionUID = -6602877877149162464L;
   private BJADTitlePanel titlePane;
   
   /**
    * Constructor, creating the frame and wiring the listeners.
    */
   public CustomHeaderTestApp()
   {
      super("BJAD TitleBar Test App");
      setSize(800, 440);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      setDefaultLookAndFeelDecorated(false);
      setContentPane(createContentPanel());
      setLocationByPlatform(true);
      setUndecorated(true);
      setResizable(true);
   }
   
   private JPanel createContentPanel()
   {
      JPanel contentPane = new JPanel(new BorderLayout(), true);
      titlePane = new BJADTitlePanel(this, "Custom");
      contentPane.add(titlePane, BorderLayout.NORTH);
      
      JPanel mainPane = new JPanel(new BorderLayout(), true);
      mainPane.add(new JLabel("Main Content Area"), BorderLayout.CENTER);
      contentPane.add(mainPane, BorderLayout.CENTER);
      
      return contentPane;
   }
   
   /**
    * Launch point for the test app
    * @param args
    *    CLI arguments, which are not used for this app.
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
            new CustomHeaderTestApp().setVisible(true);
         }
      });
   }
}
