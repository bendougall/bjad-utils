package bjad.swing.wizard.testapp;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import bjad.swing.wizard.AbstractWizardPage;
import bjad.swing.wizard.PageChangeReason;
import bjad.swing.wizard.testapp.model.IndividualBean;

/**
 * Page used to set the address details of the individual in the panel.
 *
 * @author 
 *   Ben Dougall
 */
public class SummaryPage extends AbstractWizardPage<IndividualBean>
{
   private static final long serialVersionUID = 7607636362978554501L;
 
   /**
    * Text field for the overall summary of the information entered
    * throughout the wizard test app.
    */
   protected JTextArea summaryField;
   
   /**
    * Constructor, setting the layout of the page.
    */
   public SummaryPage()
   {
      super(new BorderLayout(10, 10), true);
   }

   @Override
   protected void buildPanel()
   {
      JLabel lbl = new JLabel("Individual Details:");
      lbl.setFont(lbl.getFont().deriveFont(Font.BOLD, 16.0f));
      lbl.setBorder(new EmptyBorder(10, 0, 10, 0));
      this.add(lbl, BorderLayout.NORTH);
      
      summaryField = new JTextArea();
      summaryField.setEditable(false);
      this.add(new JScrollPane(summaryField), BorderLayout.CENTER);
   }

   @Override
   protected IndividualBean saveDataToModel(IndividualBean model)
   {
      return model;
   }

   @Override
   protected void restoreDataFromModel(IndividualBean model)
   {
      StringBuilder sb = new StringBuilder();
      sb.append("Name: ").append(model.getFamilyName()).append(", ").append(model.getGivenName());
      sb.append(System.lineSeparator());
      sb.append(System.lineSeparator());
      sb.append("Address: ").append(model.getAddress().getStreetNumberAndName()).append(", ");
      sb.append(model.getAddress().getCity()).append(", ").append(model.getAddress().getProvinceOrState());
      sb.append(", ").append(model.getAddress().getPostalOrZipCode()).append(System.lineSeparator());
      sb.append("Country: ").append(model.getAddress().getCountry());
      
      sb.append(System.lineSeparator());
      sb.append(System.lineSeparator()).append("Gender: ").append(model.getDemographicInformation().getGender());
      sb.append(System.lineSeparator()).append("Age: ").append(model.getDemographicInformation().getAge());
      sb.append(System.lineSeparator()).append("Employment Status: ").append(model.getDemographicInformation().getEmploymentStatus());
      
      summaryField.setText(sb.toString());
   }

   @Override
   protected boolean onNextPageOrFinishBeingPressed()
   {
      return true;
   }
   
   /**
    * Once the page is displayed we need to focus on the 
    * first field on the panel.
    */
   @Override
   protected void afterPageDisplayed(PageChangeReason reason)
   {
      summaryField.requestFocusInWindow();
   }
}
