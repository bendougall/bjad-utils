package bjad.swing.nav;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 * A custom panel customzied to be the title area of a frame, window,
 * or dialog with title text and icon.
 *
 *
 * @author 
 *   Ben Dougall
 */
public class BJADTitlePanel extends JPanel implements WindowFocusListener
{
   private static final long serialVersionUID = 1349798874036167285L;

   /**
    * Label containing the title text and the icon for the 
    * window/frame/dialog.
    */
   protected JLabel titleAndIconLabel = new JLabel();
   /**
    * Label acting as the minimize button within the panel.
    */
   protected JLabel minimizeLabel = new JLabel();
   /**
    * Label acting as the maximize button within the panel.
    */
   protected JLabel maximizeLabel = new JLabel();
   /**
    * Label acting as the close button within the panel.
    */
   protected JLabel closeLabel = new JLabel();
   /**
    * Panel for the center area of the title bar, in case the 
    * implementing frame or dialog wants to customize the title
    * bar with other controls.
    */
   protected JPanel centerPanel = new JPanel(true);   
   /**
    * The background color for the title bar when the window is 
    * active.
    */
   protected Color activeBgColor = Color.BLUE;
   /**
    * The background color for the title bar when the window is 
    * inactive. 
    */
   protected Color inactiveBgColor = Color.PINK;
   /**
    * The color of the text to display the title text in. 
    */
   protected Color titleTextColor = Color.YELLOW;
   /**
    * The background color of the action buttons when the 
    * mouse is over it.  
    */
   protected Color buttonHoverColor = Color.RED;
   /**
    * Flag for if the owner of the title bar is active or inactive
    * in terms of window focus.
    */
   protected boolean ownerActive = true; 
   /**
    * Owner of the panel to perform any actions against, such as 
    * dispatching the action events from the minimize, maximize, and
    * close buttons.
    */
   private Window panelOwner;
   
   /**
    * Point used to store the original window location when the user 
    * decides to drag the window around.
    */
   private Point originalPosition = new Point();
   
   /**
    * Constructor for the panel applying the Parent Frame to
    * act against. 
    * 
    * @param frame
    *    The owning frame for the panel.
    */
   public BJADTitlePanel(Window frame)
   {
      this(frame, "");
      if (frame instanceof JFrame)
      {
         setTitleText(((JFrame)frame).getTitle());
      }
      if (frame instanceof JDialog)
      {
         setTitleText(((JDialog)frame).getTitle());
      }
   }
   
   /**
    * Constructor for the panel applying the Parent Frame to
    * act against and the title text to display in the panel.
    * 
    * @param frame
    *    The owning frame for the panel.
    * @param titleText
    *    The text to display within the title bar panel.
    */
   public BJADTitlePanel(Window frame, String titleText)
   {
      super(new BorderLayout(), true);
      this.panelOwner = frame;
      commonConstructorActions();
      setTitleText(titleText);
   }
   
   /**
    * Sets the text to display for the title panel.
    * 
    * @param text
    *    The text to display as the title text within the panel.
    */
   public void setTitleText(String text)
   {
      this.titleAndIconLabel.setText(text);
      if (panelOwner instanceof JFrame)
      {
         ((JFrame) panelOwner).setTitle(text);
      }
   }
   
   /**
    * Sets the icon to display for the title panel.
    * 
    * @param icon
    *    The icon to display within the title and icon label.
    */
   public void setTitleText(ImageIcon icon)
   {
      this.titleAndIconLabel.setIcon(icon);
      if (panelOwner instanceof JFrame)
      {
         ((JFrame) panelOwner).setIconImage(icon == null ? null : icon.getImage());
      }
      if (panelOwner instanceof JDialog)
      {
         ((JDialog) panelOwner).setIconImage(icon == null ? null : icon.getImage());
      }
   }
   
   /**
    * Provides access to the center area of the title bar in
    * case implementations want to customize the title bar
    * with other controls.
    * 
    * @return
    *    The center panel within the title panel.
    */
   public JPanel getCenterPanel()
   {
      return this.centerPanel;
   }
   
   /**
    * Implementation for the window focus listener that marks 
    * the active flag in the panel and sets the background 
    * accordingly. 
    */
   @Override
   public void windowGainedFocus(WindowEvent e)
   {
      ownerActive = true;
      applyBackgroundToAll(activeBgColor);
   }

   /**
    * Implementation for the window focus listener that marks 
    * the active flag in the panel and sets the background 
    * accordingly. 
    */
   @Override
   public void windowLostFocus(WindowEvent e)
   {
      ownerActive = false;
      applyBackgroundToAll(inactiveBgColor);
   }
   
   /**
    * Function reacting to any of the action labels being clicked. 
    * 
    * @param actionLabel
    *    The label that was clicked
    * @param evt
    *    The mouse event, in case additional details are needed.
    */
   protected void actionButtonPressed(JLabel actionLabel, MouseEvent evt)
   {
      if (minimizeLabel.equals(actionLabel))
      {
         if (panelOwner instanceof JFrame)
         {  
            ((JFrame) panelOwner).setState(JFrame.ICONIFIED);
         }
      }
      else if (maximizeLabel.equals(actionLabel))
      {
         if (panelOwner instanceof JFrame)
         {
            JFrame owner = (JFrame)panelOwner;
            if (owner.getExtendedState() == JFrame.MAXIMIZED_BOTH)
            {
               owner.setExtendedState(JFrame.NORMAL);
            } 
            else
            {
               owner.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
         }
      }
      else if (closeLabel.equals(actionLabel))
      {
         if (panelOwner instanceof JFrame)
         {  
            panelOwner.dispatchEvent(new WindowEvent(panelOwner, WindowEvent.WINDOW_CLOSING));
         }
      }
   }
   
   private void commonConstructorActions()
   {
      setBorder(new EmptyBorder(4, 2, 4, 2));
      loadIconImages();
      
      titleAndIconLabel.setOpaque(true);
      titleAndIconLabel.setForeground(titleTextColor);
      
      constructPanelControls();
      
      // Add the window focus listener to the owner of the panel
      // so the background will adjust when the owner has or loses
      // focus within the OS.
      panelOwner.addWindowFocusListener(this);
      
      // Hide the minimize and maximize buttons if the owner of 
      // this panel is not a Frame. 
      if (!(panelOwner instanceof JFrame))
      {
         minimizeLabel.setVisible(false);
         maximizeLabel.setVisible(false);
      }

      applyBackgroundToAll(activeBgColor);
      
      // Enable the drag and drop ability as if this was a real title bar.
      applyDragAndDropLogic();      
   }
   
   private void applyDragAndDropLogic()
   {
      // React to when the panel is clicked so we can store the location
      // of the mouse click to use when adjusting the position of the 
      // owning window.
      this.addMouseListener(new MouseAdapter()
      {
         @Override
         public void mousePressed(MouseEvent e)
         {
            originalPosition.x = e.getX();
            originalPosition.y = e.getY();
         }
         
         @Override
         public void mouseClicked(MouseEvent e)
         {
            // If the mouse is clicked, and in fact, the click was a double click, 
            // see if the window is in normal mode. If in normal mode, maximize the 
            // window, otherwise, make the window go into normal mode.
            if (e.getClickCount() >= 2 && SwingUtilities.isLeftMouseButton(e))
            {
               if (panelOwner instanceof JFrame)
               {
                  JFrame owner = (JFrame)panelOwner;
                  if (owner.getExtendedState() != JFrame.NORMAL)
                  {
                     owner.setExtendedState(JFrame.NORMAL);
                  } 
                  else
                  {
                     owner.setExtendedState(JFrame.MAXIMIZED_BOTH);
                  }
               }
            }
         }
      });
      
      // React to the drag event so the panel owner can be relocated 
      // based on where the user is dragging the control to.
      this.addMouseMotionListener(new MouseMotionAdapter()
      {
         @Override
         public void mouseDragged(MouseEvent e)
         {
            int x = e.getXOnScreen();
            int y = e.getYOnScreen();
            panelOwner.setLocation(x - originalPosition.x, y - originalPosition.y);
         }
      });
   }
   
   private void loadIconImages()
   {
      Object[][] mapping = new Object[][]
            {
               {"/icons/icons8_minus_18px_1.png", minimizeLabel},
               {"/icons/icons8_rectangle_stroked_18px.png", maximizeLabel},
               {"/icons/icons8_multiply_18px_1.png", closeLabel}
            };
      
      // Go through each of the icon paths and apply the icon to
      // the related label.
      for (int index = 0; index != mapping.length; ++index)
      {
         try (InputStream stream = getClass().getResourceAsStream(mapping[index][0].toString()))
         {
            ImageIcon icon= new ImageIcon(ImageIO.read(stream));
            ((JLabel)mapping[index][1]).setIcon(icon);
            ((JLabel)mapping[index][1]).setOpaque(true);
         } 
         catch (IOException ex)
         {
            ex.printStackTrace();
         }
      }
   }
   
   private void constructPanelControls()
   {   
      // Add in the title text and icon label control to the left side 
      // of the title bar. 
      JPanel labelPanel = new JPanel(new FlowLayout(SwingConstants.LEFT, 2, 0), true);
      labelPanel.add(titleAndIconLabel);
      this.add(labelPanel, BorderLayout.WEST);
      
      // Add in the center panel so controls can be spaced, and implementations
      // can add controls to customize the title bar. 
      this.add(centerPanel, BorderLayout.CENTER);
      
      // Create the hover listener so we can change the background of
      // the action area when the mouse is over it. 
      MouseAdapter hoverListener = buildHoverListener();
      JLabel[] labels = new JLabel[] {
            minimizeLabel, maximizeLabel, closeLabel
      };
      
      // Create the action area of the panel by adding the controls to the 
      // button panel and applying the hover listener against it. 
      JPanel buttonPanel = new JPanel(new FlowLayout(SwingConstants.RIGHT, 2, 0), true);
      for (int index = 0; index != labels.length; ++index)
      {
         buttonPanel.add(labels[index]);
         labels[index].addMouseListener(hoverListener);
      }            
      this.add(buttonPanel, BorderLayout.EAST);
   }
   
   private void applyBackgroundToAll(Color c)
   {
      applyBackgroundToAll(this, c);
   }
   
   private void applyBackgroundToAll(JPanel pane, Color c)
   {
      pane.setBackground(c);
      Component[] controls = pane.getComponents();
      
      for (Component control : controls)
      {
         control.setBackground(c);
         if (control instanceof JPanel)
         {
            applyBackgroundToAll((JPanel)control, c);
         }
      }
   }
   
   private MouseAdapter buildHoverListener()
   {
      return new MouseAdapter()
      {
         @Override
         public void mouseEntered(MouseEvent e)
         {
            if (e.getSource() instanceof JLabel)
            {
               ((JLabel)e.getSource()).setBackground(buttonHoverColor);
               ((JLabel)e.getSource()).repaint();
            }
         }

         @Override
         public void mouseExited(MouseEvent e)
         {
            if (e.getSource() instanceof JLabel)
            {
               ((JLabel)e.getSource()).setBackground(ownerActive ? activeBgColor : inactiveBgColor);
               ((JLabel)e.getSource()).repaint();
            }
         }
         
         @Override
         public void mouseClicked(MouseEvent e)
         {
            if (e.getSource() instanceof JLabel)
            {
               actionButtonPressed((JLabel)e.getSource(), e);
            }
         }
      };
   }
}
