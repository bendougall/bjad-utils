package bjad.swing;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import bjad.swing.nav.BJADTitlePanel;

/**
 * Abstract Dialog window that allows for custom dialog 
 * windowss, packaged with the custom title bar. 
 *
 * @author 
 *   Ben Dougall
 */
public abstract class BJADDialog extends JDialog
{
   private static final long serialVersionUID = -1915685829454831514L;

   /**
    * Result code for when cancel or the close button is pressed. 
    */
   public static final int CANCEL_RESULT = 0;
   /**
    * Result code for when OK is pressed
    */
   public static final int OK_RESULT = 1;
   
   private static final String OK_COMMAND = "OK";
   private static final String CANCEL_COMMAND = "CANCEL";
   
   
   /**
    * The title bar pane for the dialog.
    */
   protected BJADTitlePanel titlePane;
   /**
    * The main area of the dialog for the controls/etc.
    */
   protected JPanel contentPane; 
   /**
    * The button area of the dialog, containing the ok
    * and cancel button by default. 
    */
   protected JPanel buttonPanel;
   
   /**
    * The ok button for the dialog. 
    */
   protected JButton okButton;
   /**
    * The cancel button for the dialog.
    */
   protected JButton cancelButton;
   
   /**
    * The result code for the dialog, defaulting to CANCEL
    * which will represent cancel or the title pane's close 
    * button being pressed. 
    */
   protected int dialogResult = CANCEL_RESULT;
   
   /**
    * Constructor, building the base controls on the 
    * screen. 
    * 
    * @param titleText
    *    The title of the dialog.
    */
   public BJADDialog(String titleText)
   {
      setUndecorated(true);
      setTitle(titleText);
      setContentPane(createBaseControls(titleText));
      getRootPane().setDefaultButton(okButton);
   }
   
   /**
    * Returns the dialog result code from the dialog.
    * @return
    *    The result code 
    */
   public int getDialogResult()
   {
      return this.dialogResult;
   }
   
   /**
    * Quick function to easily check if the cancel button (or close
    * button from the title bar) was pressed.
    * 
    * @return  
    *    True if the cancel button was pressed, or the close option 
    *    from the title pane.
    */
   public boolean wasCancelPressed()
   {
      return this.dialogResult == CANCEL_RESULT;
   }
   
   /**
    * Function to implment to react to the OK Button being 
    * pressed. 
    * 
    * @return
    *    True to resume with OK processing. 
    */
   protected boolean onOKPressed()
   {
      return true;
   }
   
   /**
    * Fucntion to implement to react to the cancel button being
    * pressed. 
    * 
    * @return
    *    True to resume with the cancel processing.
    */
   protected boolean onCancelPressed()
   {
      return true;
   }
   
   private JPanel createBaseControls(String titleText)
   {
      titlePane = new BJADTitlePanel(this, titleText);
      contentPane = new JPanel(new FlowLayout(), true);
      buttonPanel = new JPanel(new FlowLayout(SwingConstants.RIGHT));
      
      // Now that we have the buttons and added them to the panel,
      // create the action listener to react to them being pressed.
      bindOKAndCancelActionListener();

      // Create the root panel for the dialog so the title, content, and 
      // button bars are shown in the proper locations. 
      JPanel rootPane = new JPanel(new BorderLayout(0, 0), true);
      rootPane.add(titlePane, BorderLayout.NORTH);
      rootPane.add(contentPane, BorderLayout.CENTER);
      rootPane.add(buttonPanel, BorderLayout.SOUTH);
      
      return rootPane;
   }
   
   private void bindOKAndCancelActionListener()
   {
      okButton = new JButton("OK");
      cancelButton = new JButton("Cancel");
      
      buttonPanel.add(okButton);
      buttonPanel.add(cancelButton);
      
      ActionListener listener = new ActionListener()
      {         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            boolean continueProcessing = e.getSource().equals(okButton) ? onOKPressed() : onCancelPressed();
            if (continueProcessing)
            {
               // Now that we know we will be proceding with the OK/Cancel operation, set 
               // the result code for dialog. 
               dialogResult = e.getSource().equals(okButton) ? OK_RESULT : CANCEL_RESULT;
               
               // Hide the dialog window. 
               setVisible(false);
            }
         }
      };
      
      okButton.setActionCommand(OK_COMMAND);
      okButton.addActionListener(listener);
      cancelButton.setActionCommand(CANCEL_COMMAND);
      cancelButton.addActionListener(listener);
   }
}
