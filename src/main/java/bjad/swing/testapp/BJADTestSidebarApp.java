package bjad.swing.testapp;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import bjad.swing.BJADComboBox;
import bjad.swing.CountryDropdown;
import bjad.swing.DateEntryField;
import bjad.swing.DateTimeTextField;
import bjad.swing.NumericTextField;
import bjad.swing.TextField;
import bjad.swing.WrappedLabel;
import bjad.swing.beans.ICountryDisplay;
import bjad.swing.listing.AbstractIItemTableModel;
import bjad.swing.listing.EnhancedListModel;
import bjad.swing.listing.IListingItemEditor;
import bjad.swing.listing.ItemListingEditorPanelHelper;
import bjad.swing.listing.ItemTableEditorPanelHelper;
import bjad.swing.nav.AbstractBJADNavPanel;
import bjad.swing.nav.BJADModuleEntry;
import bjad.swing.nav.BJADNavModule;
import bjad.swing.nav.BJADSidebarNavContentPane;
import bjad.swing.nav.SidebarSectionBehaviour;
import bjad.swing.positioner.BJADConsolePositionHelper;
import bjad.swing.positioner.BJADUIPositionerHelper;

/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class BJADTestSidebarApp extends JFrame
{
   private static final long serialVersionUID = -6784328019471275079L;

   /**
    * Constructor, creating the window and the controls
    * and wiring the frame to close when the close button
    * is pressed. 
    */
   public BJADTestSidebarApp()
   {
      super("BJAD Sidebar Nav Test App");
      setSize(800, 440);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
      List<BJADNavModule> modules = new ArrayList<BJADNavModule>();
      
      BJADNavModule module = new BJADNavModule();
      module.setDisplayName("Field Positioner");
      module.setOrdinial(1);
      
      BJADModuleEntry entry = new BJADModuleEntry();
      entry.setDisplayName("Demo");
      entry.setOrdinial(1);
      entry.setNavPanel(new PositionerDemoPanel());
      module.getEntries().add(entry);
      modules.add(module);
      
      module = new BJADNavModule();
      module.setDisplayName("Entry Field Demos");
      module.setOrdinial(0);
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("TextField Demo");
      entry.setOrdinial(1);
      entry.setNavPanel(new TextFieldEntryPanel());
      module.getEntries().add(entry);
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("NumField Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new NumericEntryPanel());
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("DateField Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new DateEntryPanel());
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("Dropdown Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new DropdownDemoPanel());
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("MultiItem Editor Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new MultiItemEditorPanel());
      
      entry = new BJADModuleEntry();
      entry.setDisplayName("MultiItem Table Editor Demo");
      entry.setOrdinial(0);
      module.getEntries().add(entry);
      entry.setNavPanel(new MultiItemTableEditorPanel());
      modules.add(module);
      
      setContentPane(new BJADSidebarNavContentPane(modules, SidebarSectionBehaviour.ALL_SHOWN_AND_USER_CAN_COLLAPSE));
   }
   
   /**
    * Launching point of the application, showing the 
    * frame to the user. 
    * 
    * @param args
    *    Command line arguments are not used for this 
    *    application. 
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(new Runnable()
      {         
         @Override
         public void run()
         {
            BJADTestSidebarApp app = new BJADTestSidebarApp();
            app.setVisible(true);
         }
      });
   }
}

class TextFieldEntryPanel extends AbstractBJADNavPanel
{
   private static final long serialVersionUID = -5886406219517070874L;
   
   private TextField noRestrictionsField = new TextField();
   private TextField abcdeField = new TextField();
   private TextField maxLengthField = new TextField();
   
   
   public TextFieldEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("No Restrictions Field:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(noRestrictionsField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("'ABCDE' Only Field:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(abcdeField, BorderLayout.CENTER);
      abcdeField.setName("abcdeField");
      abcdeField.addAllowableCharacter('a');
      abcdeField.addAllowableCharactersFromString("bcdeABCDE");
      abcdeField.setPlaceholderText("Can only enter the following characters in this field: AaBbCcDdEe");
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("3 Characters only Field");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(maxLengthField, BorderLayout.CENTER);
      maxLengthField.setName("maxLengthField");
      maxLengthField.setMaxLength(3);
      content.add(pane);
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
      
      
   }
   @Override
   public String getPanelTitle()
   {
      return "Text Field entry options";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return noRestrictionsField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
   }  
}

class NumericEntryPanel extends AbstractBJADNavPanel
{
   private static final long serialVersionUID = -5886406219517070874L;
   
   private NumericTextField intField = NumericTextField.newIntegerFieldNoLimits();
   private NumericTextField decimalField = NumericTextField.newDecimalFieldNoLimits();
   private NumericTextField moneyField = NumericTextField.newMoneyField();
   
   public NumericEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Integer Only Field:");
      lbl.setPreferredSize(new Dimension(150, 39));
      intField.setPlaceholderText("Integers only in this field.");
      pane.add(lbl, BorderLayout.WEST);
      pane.add(intField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Decimal Field:");
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(decimalField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Money Field");
      lbl.setPreferredSize(new Dimension(150, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(moneyField, BorderLayout.CENTER);
      moneyField.setPlaceholderText("Decimal Field but with 2 decimal places maximum.");
      content.add(pane);
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
   }
   @Override
   public String getPanelTitle()
   {
      return "Numeric Entry Field Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return moneyField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      boolean ret = !moneyField.isFieldEmpty();
      if (!ret)
      {
         JOptionPane.showMessageDialog(this, "Money field must cannot be empty prior to navigating to next screen.", "Validation", JOptionPane.WARNING_MESSAGE);
         moneyField.requestFocusInWindow();
      }
      return ret;
   }

   @Override
   public void onPanelClosed()
   {
   }   
}

class DateEntryPanel extends AbstractBJADNavPanel implements ActionListener
{
   private static final long serialVersionUID = -5886406219517070874L;
   private static final String[] FORMATS = new String[] 
         {
               "MMM d, yyyy",
               "yyyy-MM-dd",
               "yyyy/MM/dd",
               "EEE, MMM d, ''yy" 
         };
   private DateEntryField dateWithDefaultField = new DateEntryField(new Date(1641060941000L));
   private DateEntryField dateField = new DateEntryField();
   private JComboBox<String> firstFormatDropdown = new JComboBox<>(FORMATS);
   private JComboBox<String> secondFormatDropdown = new JComboBox<>(FORMATS);
   private DateTimeTextField dateTimeTextField = new DateTimeTextField(new Date());
   
   public DateEntryPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Date Field defaulting to 2022/01/01:");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateWithDefaultField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date format for the above field:");
      lbl.setPreferredSize(new Dimension(300, 20));
      lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN | Font.ITALIC));
      pane.add(lbl, BorderLayout.WEST);      
      firstFormatDropdown.addActionListener(this);
      firstFormatDropdown.setSelectedIndex(firstFormatDropdown.getItemCount()-1);
      pane.add(firstFormatDropdown, BorderLayout.CENTER);
      content.add(pane);  
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date Field (no default):");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date format for the above field:");
      lbl.setPreferredSize(new Dimension(300, 20));
      lbl.setFont(lbl.getFont().deriveFont(Font.PLAIN | Font.ITALIC));
      pane.add(lbl, BorderLayout.WEST);      
      secondFormatDropdown.addActionListener(this);
      pane.add(secondFormatDropdown, BorderLayout.CENTER);
      content.add(pane);  
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(new JLabel(), BorderLayout.CENTER);
      content.add(pane);     
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Date Time Entry Field (use arrow keys to set):");
      lbl.setPreferredSize(new Dimension(300, 30));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(dateTimeTextField, BorderLayout.CENTER);
      content.add(pane);      
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
   }
   @Override
   public String getPanelTitle()
   {
      return "Date and Time Entry Field Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return dateTimeTextField;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
   }
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (e.getSource().equals(firstFormatDropdown))
      {
         if (firstFormatDropdown.getSelectedIndex() > -1)
         {
            dateWithDefaultField.setDisplayFormat(new SimpleDateFormat(firstFormatDropdown.getSelectedItem().toString()));
         }
         else
         {
            firstFormatDropdown.setSelectedIndex(0);
         }
      }
      if (e.getSource().equals(secondFormatDropdown))
      {
         if (secondFormatDropdown.getSelectedIndex() > -1)
         {
            dateField.setDisplayFormat(new SimpleDateFormat(secondFormatDropdown.getSelectedItem().toString()));
         }
         else
         {
            secondFormatDropdown.setSelectedIndex(0);
         }
      }
   }  
}

class PositionerDemoPanel extends AbstractBJADNavPanel implements ComponentListener
{
   private static final long serialVersionUID = -4087361006062121042L;
   
   private WrappedLabel firstLabel;
   private WrappedLabel instructionLabel;   
   private TextField textfield = new TextField();
   private boolean firstPositioning = true;
   
   public PositionerDemoPanel()
   {
      super();
      setLayout(null);
      
      firstLabel = new WrappedLabel("This display demos the field positioner helper.");
      firstLabel.setBounds(10, 10, 400, 25);
      firstLabel.setName("TitleLabel");
      this.add(firstLabel);
      
      StringBuilder instructionsSb = new StringBuilder("When focus is on the text field, you can do the ");
      instructionsSb.append("activities below and the console output will show the control's position ");
      instructionsSb.append("size each time an arrow key event occurs.");
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("  - Arrow Keys to position the field left/right or up/down.");      
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("  - CTRL + Arrow Keys to resize the field wider/thinner or taller/shorter");
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append(System.lineSeparator());
      instructionsSb.append("");
      instructionLabel = new WrappedLabel(instructionsSb.toString());
      instructionLabel.setBounds(10, 40, 400, 200);
      instructionLabel.setName("InstructionLabel");
      this.add(instructionLabel);
      
      JPanel innerPane = new JPanel(null, true);
      innerPane.setName("InnerPane");
      innerPane.setBounds(10, 130, 612, 60);
      innerPane.setBorder(new TitledBorder("Inner Panel"));
      this.add(innerPane);
      
      JLabel innerLabel = new JLabel("Label");
      innerLabel.setName("InnerLabel");
      innerLabel.setBounds(10, 23, 50, 25);
      innerPane.add(innerLabel);
      
      TextField innerTextField = new TextField();
      innerTextField.setPlaceholderText("Text Field");
      innerTextField.setName("InnerTextField");
      innerTextField.setBounds(70, 23, 150, 25);
      innerPane.add(innerTextField);
      
      JCheckBox innerCheckbox = new JCheckBox("Checkbox");
      innerCheckbox.setName("InnerCheckbox");
      innerCheckbox.setBounds(230, 23, 100, 25);
      innerPane.add(innerCheckbox);
      
      JButton innerButton = new JButton("Button");
      innerButton.setName("InnerButton");
      innerButton.setBounds(340, 23, 100, 25);
      innerPane.add(innerButton);
      
      textfield.setBounds(10, 260, 400, 25);
      textfield.setName("TextField");
      textfield.addAllowableCharacter('?');
      this.add(textfield);
      
      new BJADConsolePositionHelper(textfield, textfield);
      this.addComponentListener(this);
      
      BJADUIPositionerHelper.wireHorizontalPositionerDialog(firstLabel, firstLabel, instructionLabel, innerPane, textfield);
      
      BJADUIPositionerHelper.wireVerticalPositionerDialog(textfield, firstLabel, instructionLabel, innerPane, textfield);
   }
   
   @Override
   public String getPanelTitle()
   {
      return "BJAD Field Positioning Helper Demo";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {
      return textfield;
   }

   @Override
   public void onPanelDisplay()
   {
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {  
   }

   @Override
   public void componentResized(ComponentEvent e)
   {
      int labelWidth = this.getWidth() - firstLabel.getX() - firstLabel.getX();
      
      firstLabel.setBounds(firstLabel.getX(), firstLabel.getY(), labelWidth, firstLabel.getHeight());
      
      labelWidth = this.getWidth() - instructionLabel.getX() - instructionLabel.getX();
      instructionLabel.setBounds(instructionLabel.getX(), instructionLabel.getY(), labelWidth, instructionLabel.getHeight());
      
      if (firstPositioning)
      {
         textfield.setBounds(textfield.getX(), textfield.getY(), labelWidth, textfield.getHeight());
         firstPositioning = false;
      }
   }

   @Override
   public void componentMoved(ComponentEvent e)
   {  
   }

   @Override
   public void componentShown(ComponentEvent e)
   {  
   }

   @Override
   public void componentHidden(ComponentEvent e)
   {      
   }   
}

class DropdownDemoPanel extends AbstractBJADNavPanel implements ItemListener, CaretListener
{
   private static final long serialVersionUID = -5886406219517070554L;
   
   static String[] UNITS = new String[] 
         {
               "Celsius",
               "Fahrenheit",
               "Kelvin"
         };
   
   private CountryDropdown countryDropdown = CountryDropdown.createDropdownFromPackagedISO3166List();
   private BJADComboBox<String> formatDropdown = new BJADComboBox<String>(new String[] {"Name", "Two Character Code", "Three Character Code", "Name and Code"});
   private NumericTextField tempField = NumericTextField.newDecimalFieldNoLimits();
   private NumericTextField resultField = NumericTextField.newDecimalFieldNoLimits();
   private BJADComboBox<String> fromDropdown = new BJADComboBox<>(UNITS);
   private BJADComboBox<String> toDropdown = new BJADComboBox<>(UNITS);
   private BJADComboBox<String> abcDropdown = new BJADComboBox<String>(new String[] {"a", "b", "c"});
   
   private String oldValue = "0";
   
   public DropdownDemoPanel()
   {
      super();
      setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("Country Dropdown");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(countryDropdown, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Country Format");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(formatDropdown, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Temp to Convert");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(tempField, BorderLayout.CENTER);
      pane.add(fromDropdown, BorderLayout.EAST);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Conversion Result");
      lbl.setPreferredSize(new Dimension(150, 35));
      
      toDropdown.setPreferredSize(new Dimension(150, 35));
      fromDropdown.setPreferredSize(new Dimension(150, 35));
      
      pane.add(lbl, BorderLayout.WEST);
      pane.add(resultField, BorderLayout.CENTER);
      pane.add(toDropdown, BorderLayout.EAST);
      
      content.add(pane);
      
      abcDropdown.setEditable(true);
      abcDropdown.addAllowableCharactersFromString("abcABC");
      abcDropdown.setSelectedIndex(-1);
      abcDropdown.setPlaceholderText("Only A, B, C charcaters are allowed in this dropdown");
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("ABC Dropdown");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(abcDropdown, BorderLayout.CENTER);
      content.add(pane);
      
      tempField.setValue(0.00);
      oldValue = tempField.getDecimalValue().toPlainString();
      resultField.setValue(32.0);
      resultField.setEditable(false);
      fromDropdown.setSelectedIndex(0);
      toDropdown.setSelectedIndex(1);
      
      countryDropdown.setSelectedIndex(-1);
      countryDropdown.setPlaceholderText("Select a country");
      
      formatDropdown.setSelectedIndex(0);
      formatDropdown.addItemListener(this);
      
      this.add(content, BorderLayout.NORTH);
      this.add(new JLabel(""), BorderLayout.CENTER);
      
      fromDropdown.addItemListener(this);
      toDropdown.addItemListener(this);
      
      tempField.addCaretListener(this);
   }
   
   @Override
   public String getPanelTitle()
   {
      return "Combobox Field Demo";
   }
   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return countryDropdown;
   }
   @Override
   public void onPanelDisplay()
   {
      countryDropdown.setSelectedCountryByText("CA");
      System.out.println(countryDropdown.getSelectedIndex());
   }
   @Override
   public boolean canPanelClose()
   {
      return true;
   }
   @Override
   public void onPanelClosed()
   {    
      
   }

   @Override
   public void caretUpdate(CaretEvent e)
   {
      String temp = tempField.isFieldEmpty() ? "" : tempField.getDecimalValue().toPlainString();
      if (!temp.equals(oldValue))
      {
         doConversion();
         oldValue = temp;
      }
   }
   
   @Override
   public void itemStateChanged(ItemEvent e)
   {
      if (e.getSource().equals(formatDropdown))
      {
         switch (formatDropdown.getSelectedIndex())
         {
         case 0: 
            countryDropdown.setDisplayFormatter(ICountryDisplay.createEnglishDisplaySafeImpl());
            break;
         case 1:
            countryDropdown.setDisplayFormatter(ICountryDisplay.createAlpha2DisplayImpl());
            break;
         case 2:
            countryDropdown.setDisplayFormatter(ICountryDisplay.createAlpha3DisplayImpl());
            break;
         case 3:
            countryDropdown.setDisplayFormatter(bean -> String.format("%s (%s)", bean.getEnglishText(), bean.getAlpha2Code()));
            break;
         }
      }
      else
      {
         doConversion();
      }
   }
   
   private void doConversion()
   {
      if (tempField.isFieldEmpty())
      {
         resultField.clearField();
         return;
      }
      int fromIndex = fromDropdown.getSelectedIndex();
      int toIndex = toDropdown.getSelectedIndex();
      
      BigDecimal fromAmount = tempField.getDecimalValue();
      BigDecimal result = fromAmount;
      switch (fromIndex)
      {
      case 0:
         switch (toIndex)
         {          
         case 1:
            result = fromAmount.multiply(new BigDecimal(9)).divide(new BigDecimal(5)).add(new BigDecimal(32));            
            break;
         case 2:
            result = fromAmount.add(new BigDecimal("273.15"));
            break;
         }         
         break;
      case 1:
         switch (toIndex)
         {
         case 0:
            BigDecimal factor = new BigDecimal("5.0000").divide(new BigDecimal("9.0000"), 15, RoundingMode.HALF_UP);
            result = fromAmount.subtract(new BigDecimal(32)).multiply(factor);
            break;
         case 2:
            factor = new BigDecimal("5.0000").divide(new BigDecimal("9.0000"), 15, RoundingMode.HALF_UP);
            result = fromAmount.subtract(new BigDecimal(32)).multiply(factor).add(new BigDecimal("273.15"));
            break;
         }         
         break;
      case 2:
         switch (toIndex)
         {
         case 0:
            result = fromAmount.subtract(new BigDecimal("273.15"));
            break;
         case 1:
            BigDecimal factor = new BigDecimal("9.0000").divide(new BigDecimal("5.0000"), 15, RoundingMode.HALF_UP);
            result = fromAmount.subtract(new BigDecimal("273.15")).multiply(factor).add(new BigDecimal("32"));     
            break;
         }         
         break;
      }
      
      resultField.setValue(result.stripTrailingZeros());
   }
}

class MultiItemEditorPanel extends AbstractBJADNavPanel implements IListingItemEditor<PersonModel>
{
   private static final long serialVersionUID = -4831598939589994296L;
   
   private TextField firstNameField = new TextField();
   private TextField lastNameField = new TextField();
   
   private JButton saveButton = new JButton("Save");
   private JButton newButton = new JButton("New");
   private JButton deleteButton = new JButton("Delete");

   private EnhancedListModel<PersonModel> listModel = new EnhancedListModel<PersonModel>();
   private JList<PersonModel> personList = new JList<PersonModel>(listModel);
   
   private ItemListingEditorPanelHelper<PersonModel> listingHelper;
   
   private String currentID = null;
   
   public MultiItemEditorPanel()
   {
      super();
      setLayout(new GridLayout(1, 2, 5, 5));
      
      this.add(createEditorPanel());
      this.add(createListingPanel());
      
      listingHelper = new ItemListingEditorPanelHelper<PersonModel>(personList, newButton, deleteButton, this);
      
      saveButton.addActionListener(new ActionListener()
      {         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            listingHelper.addOrUpdateItem(retrieveItemFromEditor());
         }
      });
   }
   @Override
   public String getPanelTitle()
   {      
      return "Multi-Item Editor";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return firstNameField;
   }

   @Override
   public void onPanelDisplay()
   {
      
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
      
   }
   
   private JPanel createEditorPanel()
   {
      JPanel editorPane = new JPanel(true);
      editorPane.setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("First Name:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(firstNameField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Last Name");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(lastNameField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      saveButton.setPreferredSize(new Dimension(100, 25));
      pane.add(new JLabel(""), BorderLayout.CENTER);
      pane.add(saveButton, BorderLayout.EAST);
      content.add(pane);
      
      editorPane.add(content, BorderLayout.NORTH);
      editorPane.add(new JLabel(""), BorderLayout.CENTER);
      
      return editorPane;
   }
   
   private JPanel createListingPanel()
   {
      JPanel listPanel = new JPanel(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      
      JScrollPane sp = new JScrollPane(personList);
      sp.setPreferredSize(new Dimension(150, 300));
      content.add(sp);
      
      JPanel pane = new JPanel(new BorderLayout(5,5));
      
      pane.add(newButton, BorderLayout.WEST);
      pane.add(deleteButton, BorderLayout.EAST);
      content.add(pane);
      
      listPanel.add(content, BorderLayout.NORTH);
      listPanel.add(new JLabel(""), BorderLayout.CENTER);
      
      return listPanel;
   }
   @Override
   public void newItemStarted(boolean viaLastItemDeleted)
   {
      currentID = null;
      firstNameField.setText("");
      lastNameField.setText("");
      
      firstNameField.requestFocusInWindow();
   }
   @Override
   public void setItemInEditor(PersonModel item)
   {
      currentID = item.getId();
      firstNameField.setText(item.getFirstName());
      lastNameField.setText(item.getLastName());
   }
   
   @Override
   public PersonModel retrieveItemFromEditor()
   {
      PersonModel person = new PersonModel();
      person.setFirstName(firstNameField.getText());
      person.setLastName(lastNameField.getText());
      
      if (currentID != null)
      {
         person.setId(currentID);
      }
      
      return person;
   }
   @Override
   public boolean isItemFromEditorSameAs(PersonModel itemToCompareAgainst)
   {
      return retrieveItemFromEditor().equals(itemToCompareAgainst);
   }
   
   @Override
   public boolean isNewAllowed(PersonModel currentSelection)
   {
      return true;
   }
   @Override
   public boolean isDeleteAllowed(PersonModel itemToDelete)
   {
      return true;
   }
   @Override
   public boolean isSelectionAllowToChange(PersonModel currentSelection)
   {
      return true;
   }
}

class MultiItemTableEditorPanel extends AbstractBJADNavPanel implements IListingItemEditor<PersonModel>
{
   private static final long serialVersionUID = -4831598939589994296L;
   
   private TextField firstNameField = new TextField();
   private TextField lastNameField = new TextField();
   
   private JButton saveButton = new JButton("Save");
   private JButton newButton = new JButton("New");
   private JButton deleteButton = new JButton("Delete");

   private PersonTableModel tableModel = new PersonTableModel();
   private JTable table; 
   
   private ItemTableEditorPanelHelper<PersonModel> listingHelper;
   
   private String currentID = null;
   
   public MultiItemTableEditorPanel()
   {
      super();
      setLayout(new GridLayout(1, 2, 5, 5));
      
      this.add(createEditorPanel());
      this.add(createListingPanel());
      
      listingHelper = new ItemTableEditorPanelHelper<PersonModel>(table, tableModel, newButton, deleteButton, this);
      
      saveButton.addActionListener(new ActionListener()
      {         
         @Override
         public void actionPerformed(ActionEvent e)
         {
            listingHelper.addOrUpdateItem(retrieveItemFromEditor());
         }
      });
   }
   @Override
   public String getPanelTitle()
   {      
      return "Multi-Item Table Editor";
   }

   @Override
   public JComponent getComponentForDefaultFocus()
   {      
      return firstNameField;
   }

   @Override
   public void onPanelDisplay()
   {
      
   }

   @Override
   public boolean canPanelClose()
   {
      return true;
   }

   @Override
   public void onPanelClosed()
   {
      
   }
   
   private JPanel createEditorPanel()
   {
      JPanel editorPane = new JPanel(true);
      editorPane.setLayout(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      JPanel pane = new JPanel(new BorderLayout(5,5));
      JLabel lbl = new JLabel("First Name:");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(firstNameField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      lbl = new JLabel("Last Name");
      lbl.setPreferredSize(new Dimension(150, 35));
      pane.add(lbl, BorderLayout.WEST);
      pane.add(lastNameField, BorderLayout.CENTER);
      content.add(pane);
      
      pane = new JPanel(new BorderLayout(5,5));
      saveButton.setPreferredSize(new Dimension(100, 25));
      pane.add(new JLabel(""), BorderLayout.CENTER);
      pane.add(saveButton, BorderLayout.EAST);
      content.add(pane);
      
      editorPane.add(content, BorderLayout.NORTH);
      editorPane.add(new JLabel(""), BorderLayout.CENTER);
      
      return editorPane;
   }
   
   private JPanel createListingPanel()
   {
      table = new JTable(tableModel);
      
      JPanel listPanel = new JPanel(new BorderLayout());
      
      JPanel content = new JPanel(true);
      content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
      
      JScrollPane sp = new JScrollPane(table);
      sp.setPreferredSize(new Dimension(150, 300));
      content.add(sp);
      
      JPanel pane = new JPanel(new BorderLayout(5,5));
      
      pane.add(newButton, BorderLayout.WEST);
      pane.add(deleteButton, BorderLayout.EAST);
      content.add(pane);
      
      listPanel.add(content, BorderLayout.NORTH);
      listPanel.add(new JLabel(""), BorderLayout.CENTER);
      
      return listPanel;
   }
   @Override
   public void newItemStarted(boolean viaLastItemDeleted)
   {
      currentID = null;
      firstNameField.setText("");
      lastNameField.setText("");
      
      firstNameField.requestFocusInWindow();
   }
   @Override
   public void setItemInEditor(PersonModel item)
   {
      currentID = item.getId();
      firstNameField.setText(item.getFirstName());
      lastNameField.setText(item.getLastName());
   }
   
   @Override
   public PersonModel retrieveItemFromEditor()
   {
      PersonModel person = new PersonModel();
      person.setFirstName(firstNameField.getText());
      person.setLastName(lastNameField.getText());
      
      if (currentID != null)
      {
         person.setId(currentID);
      }
      
      return person;
   }
   @Override
   public boolean isItemFromEditorSameAs(PersonModel itemToCompareAgainst)
   {
      return retrieveItemFromEditor().equals(itemToCompareAgainst);
   }
   
   @Override
   public boolean isNewAllowed(PersonModel currentSelection)
   {
      return true;
   }
   @Override
   public boolean isDeleteAllowed(PersonModel itemToDelete)
   {
      return true;
   }
   @Override
   public boolean isSelectionAllowToChange(PersonModel currentSelection)
   {
      return true;
   }
}

class PersonModel 
{
   private String id = UUID.randomUUID().toString();
   private String firstName;
   private String lastName;
   /**
    * Returns the value of the PersonModel instance's 
    * id property.
    *
    * @return 
    *   The value of id
    */
   public String getId()
   {
      return this.id;
   }
   /**
    * Sets the value of the PersonModel instance's 
    * id property.
    *
    * @param id 
    *   The value to set within the instance's 
    *   id property
    */
   public void setId(String id)
   {
      this.id = id;
   }
   /**
    * Returns the value of the PersonModel instance's 
    * firstName property.
    *
    * @return 
    *   The value of firstName
    */
   public String getFirstName()
   {
      return this.firstName;
   }
   /**
    * Sets the value of the PersonModel instance's 
    * firstName property.
    *
    * @param firstName 
    *   The value to set within the instance's 
    *   firstName property
    */
   public void setFirstName(String firstName)
   {
      this.firstName = firstName;
   }
   /**
    * Returns the value of the PersonModel instance's 
    * lastName property.
    *
    * @return 
    *   The value of lastName
    */
   public String getLastName()
   {
      return this.lastName;
   }
   /**
    * Sets the value of the PersonModel instance's 
    * lastName property.
    *
    * @param lastName 
    *   The value to set within the instance's 
    *   lastName property
    */
   public void setLastName(String lastName)
   {
      this.lastName = lastName;
   }
   
   @Override
   public int hashCode()
   {
      return Objects.hash(firstName, id, lastName);
   }
   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      PersonModel other = (PersonModel) obj;
      return Objects.equals(this.firstName, other.firstName) && Objects.equals(this.id, other.id)
            && Objects.equals(this.lastName, other.lastName);
   }
   @Override
   public String toString()
   {
      return this.lastName + ", " + this.firstName;
   }
   
}

class PersonTableModel extends AbstractIItemTableModel<PersonModel>
{
   private static final long serialVersionUID = 4294566941602378959L;
   private static final String[] COLUMNS = new String[] { "First Name", "Last Name" };
   
   @Override
   public String getColumnName(int column)
   {
      return COLUMNS[column];
   }

   @Override
   public boolean isCellEditable(int rowIndex, int columnIndex)
   {
      return false;
   }

   @Override
   public int getColumnCount()
   {
      return COLUMNS.length;
   }

   @Override
   public Object getValueAt(int rowIndex, int columnIndex)
   {
      PersonModel model = getItemAt(rowIndex);
      if (model != null)
      {
         switch (columnIndex)
         {
         case 0: return model.getFirstName();
         case 1: return model.getLastName();
         }
      }
      return "";
   }
   
}