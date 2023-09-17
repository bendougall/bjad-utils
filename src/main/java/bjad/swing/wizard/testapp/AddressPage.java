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
 * Page used to set the address details of the individual in the panel.
 *
 * @author 
 *   Ben Dougall
 */
public class AddressPage extends AbstractWizardPage<IndividualBean>
{
   private static final long serialVersionUID = 7607636362978554501L;
 
   /**
    * Text field for the street address
    */
   protected JTextField streetNumberAndNameField;
   /**
    * Text field for the city
    */
   protected JTextField cityField;
   /**
    * Text field for the province.
    */
   protected JTextField provinceField;
   /**
    * Text field for the country
    */
   protected JTextField countryField;
   /**
    * Text field for the postal code
    */
   protected JTextField postalOrZipCodeField;
   
   /**
    * Constructor, setting the layout of the page.
    */
   public AddressPage()
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
      
      streetNumberAndNameField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Street Address:", streetNumberAndNameField));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      cityField = new JTextField(100);
      fieldArea.add(buildLabelAndField("City:", cityField));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      provinceField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Province/State:", provinceField));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      countryField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Country:", countryField));
      
      // Add some padding
      fieldArea.add(buildLabelAndField("", new JLabel("")));
      
      postalOrZipCodeField = new JTextField(100);
      fieldArea.add(buildLabelAndField("Postal/ZIP Code:", postalOrZipCodeField));
   }

   @Override
   protected IndividualBean saveDataToModel(IndividualBean model)
   {
      model.getAddress().setStreetNumberAndName(streetNumberAndNameField.getText());
      model.getAddress().setCity(cityField.getText());
      model.getAddress().setProvinceOrState(provinceField.getText());
      model.getAddress().setCountry(countryField.getText());
      model.getAddress().setPostalOrZipCode(postalOrZipCodeField.getText());
      
      return model;
   }

   @Override
   protected void restoreDataFromModel(IndividualBean model)
   {
      streetNumberAndNameField.setText(model.getAddress().getStreetNumberAndName());
      cityField.setText(model.getAddress().getCity());
      provinceField.setText(model.getAddress().getProvinceOrState());
      countryField.setText(model.getAddress().getCountry());
      postalOrZipCodeField.setText(model.getAddress().getPostalOrZipCode());
   }

   @Override
   protected boolean onNextPageOrFinishBeingPressed()
   {
      String validationErrorMessage = "";
      String validationMessageTitle = "Validation Error";
      
      String val = streetNumberAndNameField.getText();
      if (val == null || val.trim().isEmpty())
      {
         validationErrorMessage = "Street Address must be entered.";
         streetNumberAndNameField.requestFocusInWindow();
      }
      
      val = cityField.getText();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "City must be entered.";
         cityField.requestFocusInWindow();
      }
      
      val = provinceField.getText();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "Province/State must be entered.";
         provinceField.requestFocusInWindow();
      }
      
      val = countryField.getText();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "Country must be entered.";
         countryField.requestFocusInWindow();
      }
      
      val = postalOrZipCodeField.getText();
      if (validationErrorMessage.isEmpty() && (val == null || val.trim().isEmpty()))
      {
         validationErrorMessage = "Postal/Zip code must be entered.";
         postalOrZipCodeField.requestFocusInWindow();
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
      streetNumberAndNameField.requestFocusInWindow();
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
