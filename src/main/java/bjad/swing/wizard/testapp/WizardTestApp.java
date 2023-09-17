package bjad.swing.wizard.testapp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bjad.swing.wizard.AbstractWizardController;
import bjad.swing.wizard.AbstractWizardPage;
import bjad.swing.wizard.WizardListener;
import bjad.swing.wizard.testapp.model.IndividualBean;

/**
 * Main Entry point for the test application and 
 * the definition of the frame the application will
 * show.
 *
 * @author 
 *   Ben Dougall
 */
public class WizardTestApp extends JFrame implements ActionListener, WizardListener<IndividualBean, AbstractWizardPage<IndividualBean>>
{
   private static final long serialVersionUID = 6838925724323132496L;

   private JPanel wizardPanel = new JPanel(new BorderLayout(), true);
   private JPanel buttonPanel = new JPanel(new BorderLayout(), true);
   private JButton previousButton = new JButton("Previous");
   private JButton nextButton = new JButton("Next");
   private JLabel pageLabel = new JLabel("Page 1 of 3");
   
   IndividualWizardController wizardController;
      
   /**
    * Constructor, creating the application window.
    */
   public WizardTestApp()
   {
      // Add the title to the frame window.
      super("BJAD Wizard Framework Test App");
      
      // Create the wizard controller so the pages are available
      wizardController = new IndividualWizardController(wizardPanel);
      
      // Set up the controller to disable the next/previous buttons
      // as the first or last pages are displayed.
      wizardController.setAutoDisableButtons(true);
      wizardController.setNextButton(nextButton);
      wizardController.setPreviousButton(previousButton);
      
      // Set the content pane for the window
      setContentPane(createContentPane());
      
      // Make it so pressing enter on the screen presses Next by default
      getRootPane().setDefaultButton(nextButton);
      
      // Set the size of the framework.
      setSize(800, 600);
      
      // Close the application if the window is closed.
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      // Add the wizard listener to the controller so the window can 
      // react to page chnages.
      wizardController.addWizardListener(this);
      
      // Show the first page.
      wizardController.changePage(0);
   }
   
   private JPanel createContentPane()
   {  
      // Show the initial page.
      wizardController.doInitialDisplay();
      
      nextButton.addActionListener(this);
      previousButton.addActionListener(this);
      
      // Create the button area of the frame with the next and previous 
      // buttons (as well as a label to add some artifical spacing in the flow
      // layout.
      JPanel buttonArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0), true);
      buttonArea.add(nextButton);
      buttonArea.add(new JLabel("   "));
      buttonArea.add(previousButton);
      
      buttonPanel.add(buttonArea, BorderLayout.WEST);
      buttonPanel.add(new JLabel(), BorderLayout.CENTER);
      buttonPanel.add(pageLabel, BorderLayout.EAST);
      
      // Add the wizard panel and the button panel to a panel that 
      // will act as the content panel for the frame and return it.
      JPanel contentPane = new JPanel(new BorderLayout(), true);
      contentPane.add(wizardPanel, BorderLayout.CENTER);
      contentPane.add(buttonPanel, BorderLayout.SOUTH);
      // Get the controls off the edges of the frame with the 
      // empty (padding) border.
      contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
      
      return contentPane;
   }
   
   /**
    * Entry point to the application.
    * @param args
    *    Arguments from the command line, not used in this application.
    */
   public static void main(String[] args)
   {
      // Schedule a job for the event-dispatching thread to show the 
      // main window for the application.
      javax.swing.SwingUtilities.invokeLater(new Runnable() 
      {
          public void run() 
          {
              new WizardTestApp().setVisible(true);
          }
      });
   }

   @Override
   public void pageChanged(AbstractWizardPage<IndividualBean> pageDisplayed, int pageIndex, boolean firstPage,
         boolean lastPage)
   {      
      nextButton.setText(lastPage ? "Finish" : "Next");
      nextButton.setEnabled(true);
      
      pageLabel.setText("Page: " + (pageIndex+1) + " of " + wizardController.getPages().size());
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource().equals(nextButton))
      {
         if (wizardController.isLastPage())
         {
            System.exit(0);
         }
         else
         {
            wizardController.nextPage();
         }
      }
      else if (e.getSource().equals(previousButton))
      {
         wizardController.previousPage();
      }
   }
}

class IndividualWizardController extends AbstractWizardController<IndividualBean, AbstractWizardPage<IndividualBean>>
{
   public IndividualWizardController(JPanel containingPanel)
   {
      super(containingPanel);
      
      this.dataModel = new IndividualBean();
      setupPageList();
      this.currentPageIndex = 0;
   }
   
   @Override
   protected void setupPageList()
   {
      this.pages.add(new NamePage());
      this.pages.add(new AddressPage());
      this.pages.add(new DemographicsPage());
      this.pages.add(new SummaryPage());
   }
   
}
