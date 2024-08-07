package bjad.swing.testapp;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import bjad.swing.BJADResizeDelegate;

/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class ResizeTestApp extends JFrame
{
   private static final long serialVersionUID = 2368676687115994035L;
   
   /**
    * Constructor, creating the frame and wiring the listeners.
    */
   public ResizeTestApp()
   {
      super("BJAD Resizing Test App");
      new BJADResizeDelegate(this);
      
      setSize(800, 440);
      setMinimumSize(new Dimension(400, 200));
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
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      
      JButton closeButton = new JButton("Close");
      closeButton.addActionListener((e) -> System.exit(0));
      
      JPanel mainPane = new JPanel(new BorderLayout(), true);
      mainPane.add(new JLabel("Main Content Area"), BorderLayout.CENTER);
      mainPane.add(closeButton, BorderLayout.SOUTH);
      
      contentPane.add(mainPane, BorderLayout.CENTER);
      
      return contentPane;
   }
   
   /**
    * starts the app
    * @param args
    *    Command line arguments.
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(() -> new ResizeTestApp().setVisible(true));
   }
}
