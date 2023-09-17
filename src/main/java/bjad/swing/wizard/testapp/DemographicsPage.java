package bjad.swing.wizard.testapp;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
public class DemographicsPage extends AbstractWizardPage<IndividualBean>
{
   private static final long serialVersionUID = 7607636362978554501L;
 
   /**
    * Radio button for the male gender option.
    */
   protected JRadioButton maleRadioButton;
   /**
    * Radio button for the female gender option
    */
   protected JRadioButton femaleRadioButton;
   /**
    * Radio button for the other gender option.
    */
   protected JRadioButton otherRadioButton;
   
   /**
    * Button group for the gender option radio buttons.
    */
   protected ButtonGroup genderButtonGroup;
   
   /**
    * Text field for the age
    */
   protected JTextField ageField;
   
   /**
    * Dropdown for the employment status 
    */
   protected JComboBox<String> employmentStatusDropdown;
   
   /**
    * Constructor, setting the layout of the page.
    */
   public DemographicsPage()
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
      
      JPanel genderOptionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5), true);
      maleRadioButton = new JRadioButton("Male");
      femaleRadioButton = new JRadioButton("Female");
      otherRadioButton = new JRadioButton("Other");
      
      genderOptionPanel.add(maleRadioButton);
      genderOptionPanel.add(femaleRadioButton);
      genderOptionPanel.add(otherRadioButton);
      
      genderButtonGroup = new ButtonGroup();
      genderButtonGroup.add(maleRadioButton);
      genderButtonGroup.add(femaleRadioButton);
      genderButtonGroup.add(otherRadioButton);
      
      fieldArea.add(buildLabelAndField("Gender:", genderOptionPanel));     
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      ageField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Age:", ageField));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      employmentStatusDropdown = new JComboBox<>(new String[] {
            "Employed",
            "Unemployed",
            "Retired"
      });
      fieldArea.add(buildLabelAndField("Employment Status:", employmentStatusDropdown));
      employmentStatusDropdown.setSelectedIndex(-1);
   }

   @Override
   protected IndividualBean saveDataToModel(IndividualBean model)
   {
      Enumeration<AbstractButton> options = genderButtonGroup.getElements();
      while (options.hasMoreElements())
      {
         JRadioButton option = (JRadioButton)options.nextElement();
         if (option.isSelected())
         {
            model.getDemographicInformation().setGender(option.getText());
            break;
         }
      }
      model.getDemographicInformation().setAge(Integer.parseInt(ageField.getText()));
      model.getDemographicInformation().setEmploymentStatus(employmentStatusDropdown.getSelectedItem().toString());
      
      return model;
   }

   @Override
   protected void restoreDataFromModel(IndividualBean model)
   {      
      Enumeration<AbstractButton> options = genderButtonGroup.getElements();
      while (options.hasMoreElements())
      {
         JRadioButton option = (JRadioButton)options.nextElement();
         if (option.getText().equals(model.getDemographicInformation().getGender()))
         {
            option.setSelected(true);
            break;
         }
      }
      if (model.getDemographicInformation().getAge() > 0)
      {
         this.ageField.setText(String.valueOf(model.getDemographicInformation().getAge()));
      }
      if (
            model.getDemographicInformation().getEmploymentStatus() != null && 
            !model.getDemographicInformation().getEmploymentStatus().trim().isEmpty()
         )
      {
         this.employmentStatusDropdown.setSelectedItem(model.getDemographicInformation().getEmploymentStatus());
      }
   }

   @Override
   protected boolean onNextPageOrFinishBeingPressed()
   {
      String validationErrorMessage = "";
      String validationMessageTitle = "Validation Error";
      
      if (genderButtonGroup.getSelection() == null)
      {
         validationErrorMessage = "Gender must be selected.";
         maleRadioButton.requestFocusInWindow();
      }
      
      String val = ageField.getText().trim();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "Age must be entered";
         ageField.requestFocusInWindow();
      }
      if (validationErrorMessage.isEmpty())
      {
         try
         { 
            int age = Integer.parseInt(val);
            if (age < 1)
            {
               validationErrorMessage = "Age cannot be less than 1";
               ageField.requestFocusInWindow();
            }
         }
         catch (NumberFormatException ex)
         {
            validationErrorMessage = "Age must be a number";
            ageField.requestFocusInWindow();
         }
      }
      
      Object selection = employmentStatusDropdown.getSelectedItem();
      if (selection == null)
      {
         validationErrorMessage = "Employment status must have a selection";
         employmentStatusDropdown.requestFocusInWindow();
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
      maleRadioButton.requestFocusInWindow();
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
      lbl.setMinimumSize(new Dimension(175, heightPref));
      lbl.setPreferredSize(new Dimension(175, heightPref));
      lbl.setMaximumSize(new Dimension(175, heightPref));
      
      pane.setMinimumSize(new Dimension(100, heightPref));
      pane.setPreferredSize(new Dimension(Integer.MAX_VALUE, heightPref));
      pane.setMaximumSize(new Dimension(Integer.MAX_VALUE, heightPref));
      
      // Make sure the panel is aligned on the left within the parent
      // panel.
      pane.setAlignmentX(Component.LEFT_ALIGNMENT);
      return pane;
   }
}
