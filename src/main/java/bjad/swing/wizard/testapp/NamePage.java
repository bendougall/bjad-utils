package bjad.swing.wizard.testapp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import bjad.swing.wizard.AbstractWizardPage;
import bjad.swing.wizard.PageChangeReason;
import bjad.swing.wizard.testapp.model.IndividualBean;

/**
 * Page used to set the name of the individual in the panel.
 *
 * @author 
 *   Ben Dougall
 */
public class NamePage extends AbstractWizardPage<IndividualBean>
{
   private static final long serialVersionUID = 7607636362978554501L;
 
   /**
    * Text field for the first name of the person. 
    */
   protected JTextField givenNameField;
   /**
    * Text field for the last name of the person.
    */
   protected JTextField surnameField;
   
   /**
    * Constructor, setting the layout of the page.
    */
   public NamePage()
   {
      super(new BorderLayout(), true);
   }
   
   @Override
   protected void buildPanel()
   {
      JPanel fieldArea = new JPanel(true);
      super.add(fieldArea);
      fieldArea.setLayout(new BoxLayout(fieldArea, BoxLayout.Y_AXIS));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      givenNameField = new JTextField(100);
      fieldArea.add(buildLabelAndField("First Name:", givenNameField));     
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      surnameField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Last Name:", surnameField));
   }

   @Override
   protected IndividualBean saveDataToModel(IndividualBean model)
   {
      model.setGivenName(givenNameField.getText());
      model.setFamilyName(surnameField.getText());
      
      return model;
   }

   @Override
   protected void restoreDataFromModel(IndividualBean model)
   {
      this.givenNameField.setText(model.getGivenName());
      this.surnameField.setText(model.getFamilyName());
   }

   @Override
   protected boolean onNextPageOrFinishBeingPressed()
   {
      String validationErrorMessage = "";
      String validationMessageTitle = "Validation Error";
      
      String val = givenNameField.getText();
      if (val == null || val.trim().isEmpty())
      {
         validationErrorMessage = "First name must be entered.";
         givenNameField.requestFocusInWindow();
      }
      
      val = surnameField.getText();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "Last name must be entered.";
         surnameField.requestFocusInWindow();
      }
      
      if (validationErrorMessage.isEmpty())
      {
         return true;
      }
      else
      {
         JOptionPane.showMessageDialog(this, validationErrorMessage, validationMessageTitle, JOptionPane.WARNING_MESSAGE);
         return false;
      }
   }
   
   /**
    * Once the page is displayed we need to focus on the 
    * first field on the panel.
    */
   @Override
   protected void afterPageDisplayed(PageChangeReason reason)
   {      
      givenNameField.requestFocusInWindow();
   }
   
   private JPanel buildLabelAndField(String labelText, JComponent component)
   {
      JPanel pane = new JPanel(new BorderLayout(15, 5), true);
      
      JLabel lbl = new JLabel(labelText);
      pane.add(lbl, BorderLayout.WEST);
      
      pane.add(component, BorderLayout.CENTER);
      
      // Determine the height of the panel based on the control it is going to show.
      int heightPref = component instanceof JLabel ? 10 : 30;
      
      // Set all the size properties for the pane based on the height
      // determined by the control being shown.
      lbl.setMinimumSize(new Dimension(100, heightPref));
      lbl.setPreferredSize(new Dimension(100, heightPref));
      lbl.setMaximumSize(new Dimension(100, heightPref));
      
      pane.setMinimumSize(new Dimension(100, heightPref));
      pane.setPreferredSize(new Dimension(Integer.MAX_VALUE, heightPref));
      pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, heightPref));
      
      // Make sure the panel is aligned on the left within the parent
      // panel.
      pane.setAlignmentX(Component.LEFT_ALIGNMENT);
      return pane;
   }
}
