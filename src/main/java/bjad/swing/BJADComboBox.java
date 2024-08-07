package bjad.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;

import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxEditor;


/**
 * Combobox extension that includes the restrictive text box
 * and placeholder capabilities.
 *
 * @author 
 *   Ben Dougall
 * @param <T> 
 *   The type of object to show within the combobox
 */
public class BJADComboBox<T> extends JComboBox<T> implements FocusListener
{
   private static final long serialVersionUID = 2257674315285171013L;
   private RestrictiveComboBoxEditor editor = null;
   private boolean selectionEnforced = false;
   private AutoCompleteMode autoCompleteMode = AutoCompleteMode.DO_NOTHING;
   
   /**
    * Default Constructor
    */
   public BJADComboBox()
   {
      super();
      setDefaultsAndCreateEditor();
   }

   /**
    * Constructor, setting the model for the combobox to 
    * show the items from.
    * 
    * @param aModel
    *    The model containing the items to display
    */
   public BJADComboBox(ComboBoxModel<T> aModel)
   {
      super(aModel);
      setDefaultsAndCreateEditor();
   }

   /**
    * Constructor, setting the array of items 
    * to display. 
    * 
    * @param items
    *    The items to add to the list.
    */
   public BJADComboBox(T[] items)
   {
      super(items);
      setDefaultsAndCreateEditor();
   }

   /**
    * Constructor, setting the collection of items
    * to display. 
    * 
    * @param items
    *    The collection of items to display.
    */
   public BJADComboBox(Collection<T> items)
   {
      this();
      if (items != null)
      {
         Iterator<T> iter = items.iterator();
         iter.forEachRemaining(new Consumer<T>()
         {
            @Override
            public void accept(T t)
            {
               addItem(t);               
            }
         });
      }
   }

   /**
    * Returns the maximum length for the combobox.
    * @return
    *    The maxmium length.
    */
   public int getMaxLength()
   {
      return editor.editor.getMaxLength();
   }
   
   /**
    * Sets the maximum length of the text in the combobox
    * @param length
    *    The max length.
    */
   public void setMaxLength(int length)
   {
      editor.editor.setMaxLength(length);
   }
   
   /**
    * Adds an allowable character to the text field's
    * allowable character set. 
    * 
    * @param c
    *    The character to allow.
    */
   public void addAllowableCharacter(Character c)
   {
      editor.editor.addAllowableCharacter(c);
   }
   
   /**
    * Adds the characters in the string provided to the 
    * allowable character set for the field. 
    * @param str
    *    The string containing the characters to add
    *    to the allowable character set for the field.
    */
   public void addAllowableCharactersFromString(String str)
   {
      editor.editor.addAllowableCharactersFromString(str);
   }
   
   /**
    * Removes an allowable character from the text field's
    * allowable character set. 
    * 
    * @param c
    *    The character to no longer allow.
    */
   public void removeAllowableCharacter(Character c)
   {
      editor.editor.removeAllowableCharacter(c);
   }
   
   /**
    * Removes the characters in the string provided from the 
    * allowable character set for the field. 
    * @param str
    *    The string containing the characters to remove
    *    from the allowable character set for the field.
    */
   public void removeAllowableCharactersFromString(String str)
   {
      editor.editor.removeAllowableCharactersFromString(str);
   }
   
   /**
    * Returns the placeholder text, which will be shown to the user 
    * when the field is empty, but enabled and editable. 
    * 
    * @return
    *    The placeholder text currently in use by the field.
    */
   public String getPlaceholderText()
   {
      return editor.editor.placeholderText;
   }
   
   /**
    * Sets the placeholder text to display in the field when its
    * empty, but enabled and editable. 
    * @param text
    *    The text to display.
    */
   public void setPlaceholderText(String text)
   {
      editor.editor.placeholderText = text;
   }
   
   /**
    * Returns the value of the BJADComboBox instance's 
    * placeholderColor property.
    *
    * @return 
    *   The value of placeholderColor
    */
   public Color getPlaceholderColor()
   {
      return editor.editor.placeholderColor;
   }

   /**
    * Sets the value of the BJADComboBox instance's 
    * placeholderColor property.
    *
    * @param placeholderColor 
    *   The value to set within the instance's 
    *   placeholderColor property
    */
   public void setPlaceholderColor(Color placeholderColor)
   {
      editor.editor.placeholderColor = placeholderColor;
   }

   /**
    * Returns the value of the BJADComboBox instance's 
    * placeholderFont property.
    *
    * @return 
    *   The value of placeholderFont
    */
   public Font getPlaceholderFont()
   {
      return editor.editor.placeholderFont;
   }

   /**
    * Sets the value of the BJADComboBox instance's 
    * placeholderFont property.
    *
    * @param placeholderFont 
    *   The value to set within the instance's 
    *   placeholderFont property
    */
   public void setPlaceholderFont(Font placeholderFont)
   {
      editor.editor.placeholderFont = placeholderFont;
   }

   /**
    * Sets whether the text in the field will be selected when focus
    * is gained in the text box (default), or if the caret position 
    * will be moved to the end of the text in the field. 
    * 
    * @param val
    *    True to select all text when focus is gained, false to move
    *    the caret to the end of the text content.
    */
   public void setSelectAllOnFocus(boolean val)
   {
      editor.editor.selectAllOnFocus = val;
   }
   
   /**
    * Returns the value of the BJADComboBox instance's 
    * beepOnInvalidKeyEntry property.
    *
    * @return 
    *   The value of beepOnInvalidKeyEntry
    */
   public boolean isBeepOnInvalidKeyEntry()
   {
      return editor.editor.beepOnInvalidKeyEntry;
   }

   /**
    * Sets the value of the BJADComboBox instance's 
    * beepOnInvalidKeyEntry property.
    *
    * @param beepOnInvalidKeyEntry 
    *   The value to set within the instance's 
    *   beepOnInvalidKeyEntry property
    */
   public void setBeepOnInvalidKeyEntry(boolean beepOnInvalidKeyEntry)
   {
      editor.editor.beepOnInvalidKeyEntry = beepOnInvalidKeyEntry;
   }
   
   /**
    * Adds the invalid entry listener to the field so it will
    * be registered for the events. 
    * 
    * @param listener
    *    The listener to register
    */
   public void addInvalidEntryListener(InvalidKeyEntryListener listener)
   {
      editor.editor.addInvalidEntryListener(listener);
   }
   
   /**
    * Returns the text in the editor portion of the combobox.
    * @return
    *    The text in the editor text field.
    */
   public String getText()
   {
      String text = (editor != null && editor.editor != null) ? editor.editor.getText(): "";
      // Selection not enforced, return the text right away
      if (!this.selectionEnforced)
      {
         return text;
      }
      // Selection enforced, see if the text matches any of the
      // items in the dropdown's list (case-insensitive) and 
      // if a match is found, set the selected index to the match
      // (if it hasn't been set already) and return the text. 
      else
      {
         for (int index = 0; index != this.getItemCount(); ++index)
         {
            if (this.getItemAt(index).toString().equalsIgnoreCase(text))
            {
               if (index != this.getSelectedIndex())
               {
                  this.setSelectedIndex(index);
               }
               return this.getItemAt(index).toString();
            }
         }
         // No matches and selection enforced, return empty string. 
         return "";
      }
   }
   
   /**
    * Sets the text within the editor portion of the combobox.
    * @param text
    *    The text to set in the field.
    */
   public void setText(String text)
   {
      editor.editor.setText(text);
   }
   
   /**
    * Removes the invalid entry listener from the field so it
    * will no longer be registered for events.
    * 
    * @param listener
    *    The listener to remove.
    */
   public void removeInvalidEntryListener(InvalidKeyEntryListener listener)
   {
      editor.editor.removeInvalidEntryListener(listener);
   }
   
   /**
    * Provides direct access to the text field acting as the 
    * editor for the combobox.
    * 
    * @return
    *    The TextField acting as the editor for the combobox.
    */
   public TextField getEditorTextField()
   {
      return editor.editor;
   }
   
   /**
    * Returns the flag specifying if a selection from the 
    * combobox options is required to be made in order to get
    * the text from the dropdown. 
    * 
    * @return
    *    True if the text in the dropdown needs to match with 
    *    an item in the dropdown or not. 
    */
   public boolean isSelectionEnforced()
   {
      return this.selectionEnforced;
   }
   
   /**
    * Sets whether or not the text in the dropdown must match an
    * entry in the dropdown in order for getText to return the 
    * text in the dropdown. 
    * 
    * @param enforceSelection
    *    True if the text needs to match an item in the dropdown for
    *    getText() to return the text, false for the text to be returned 
    *    with or without a match. 
    */
   public void setSelectionEnforced(boolean enforceSelection)
   {
      this.selectionEnforced = enforceSelection;
   }
   
   /**
    * Resets the ComboBox to a blank slate, setting the text
    * to blank (if editable), and setting the selection to 
    * no selection.
    */
   public void resetValue()
   {
      if (isEditable())
      {
         this.setText("");
      }
      this.setSelectedIndex(-1);
   }
   
   /**
    * Auto completes the text in the combobox if the autocomplete
    * mode states to do so. 
    * 
    * @param mode
    *    The way the auto complete operation will execute.
    */
   public void autoCompleteComboboxEntry(AutoCompleteMode mode)
   {
      // Do not do autocomplete logic if the combobox cannot be 
      // edited or if the mode is DO_NOTHING.
      if (!isEditable() && mode == AutoCompleteMode.DO_NOTHING)
      {
         return;
      }
      
      String text = getText();
      boolean found = false;
      if (text != null && !text.trim().isEmpty())
      {
         text = text.trim().toUpperCase();
         for (int index = 0; index != this.getItemCount(); ++index)
         {
            String itemText = getItemAt(index).toString();
            if (itemText != null && !itemText.trim().isEmpty())
            {
               if (itemText.toUpperCase().startsWith(text))
               {
                  setText(itemText);
                  setSelectedIndex(index);
                  found = true;
                  break;
               }
            }
         }
      }
      
      // No match found, and the selection is enforced or 
      // the auto complete mode is CLEAR ON unknown, wipe
      // the selection and text from the combobox.
      if (!found && (selectionEnforced || mode == AutoCompleteMode.FILL_BUT_CLEAR_UNKNOWN_TEXT))
      {
         resetValue();
      }
   }
   
   /**
    * Creates the default editor for the combobox with the default
    * max length of 500.
    */
   private void setDefaultsAndCreateEditor()
   {
      // Make sure no items are selected by default. Developer using 
      // the dropdown can add a default selection if they wish.
      setSelectedIndex(-1);
            
      // Create the editor so that the character restrictions and 
      // what not can be applied within the combobox itself. 
      editor = new RestrictiveComboBoxEditor();
      editor.editor.setMaxLength(500);
      setEditor(editor);

      // add the focus listener as we make the editor so we can 
      // reset bad selections to blank if selection is enforced and 
      // the focus is lost when a non-matching selection is in the 
      // text field. 
      editor.editor.addFocusListener(this);
   }
   
   /**
    * Returns the auto-completion mode for the combobox
    * @return
    *    The auto complete mode in use for the dropdown.
    */
   public AutoCompleteMode getAutoCompleteMode()
   {
      return this.autoCompleteMode;
   }
   
   /**
    * Sets the auto-completion mode for the dropdown.
    * @param mode
    *    The mode the dropdown will use for auto-completion.
    */
   public void setAutoCompleteMode(AutoCompleteMode mode)
   {
      this.autoCompleteMode = mode;
   }

   @Override
   public void focusGained(FocusEvent e)
   {
      ; // Do nothing. 
   }

   @Override
   public void focusLost(FocusEvent e)
   {
      // Make sure that the selection is set properly 
      // when the editor in the combobox has lost focus.
      autoCompleteComboboxEntry(autoCompleteMode);
   }

   @Override
   public T getSelectedItem()
   {
      // Make sure the selection is set properly 
      // when the selected item is requested from
      // the dropdown
      autoCompleteComboboxEntry(this.getAutoCompleteMode());
      
      int selectedIndex = getSelectedIndex();
      return selectedIndex != -1 ? this.getItemAt(selectedIndex) : null;
   }

   @Override
   public Object[] getSelectedObjects()
   {
      // Get the selected item from the dropdown, 
      // triggering auto-complete logic along 
      // the way if needed. 
      T item = getSelectedItem();
      
      // No item selected, return whatever the base
      // combobox class would return. 
      if (item == null)
      {
         return super.getSelectedObjects();
      }
      // Item selected, return an object array with
      // that item as its element. 
      else
      {
         return new Object[] { item };
      }
   }
}

class RestrictiveComboBoxEditor implements ComboBoxEditor, FocusListener
{
   protected TextField editor;
   private Object oldValue;

   public RestrictiveComboBoxEditor() {
       editor = createEditorComponent();
   }

   public Component getEditorComponent() {
       return editor;
   }

   /**
    * Creates the internal editor component. Override this to provide
    * a custom implementation.
    *
    * @return a new editor component
    * @since 1.6
    */
   protected TextField createEditorComponent() {
       TextField editor = new BorderlessTextField();
       //editor.setBorder(null);
       return editor;
   }

   /**
    * Sets the item that should be edited.
    *
    * @param anObject the displayed value of the editor
    */
   public void setItem(Object anObject) {
       String text;

       if ( anObject != null )  {
           text = anObject.toString();
           if (text == null) {
               text = "";
           }
           oldValue = anObject;
       } else {
           text = "";
       }
       // workaround for 4530952
       if (! text.equals(editor.getText())) {
           editor.setText(text);
       }
   }

   public Object getItem() {
       Object newValue = editor.getText();

       if (oldValue != null && !(oldValue instanceof String))  {
           // The original value is not a string. Should return the value in it's
           // original type.
           if (newValue.equals(oldValue.toString()))  {
               return oldValue;
           } else {
               /*
               // Must take the value from the editor and get the value and cast it to the new type.
               Class<?> cls = oldValue.getClass();
               try {                   
                  Method method = sun.reflect.misc.MethodUtil.getMethod(cls, "valueOf", new Class[]{String.class});
                   newValue = sun.reflect.misc.MethodUtil.invoke(method, oldValue, new Object[] { editor.getText()});
               } catch (Exception ex) {
                   // Fail silently and return the newValue (a String object)
               }
               */
              ;
           }
       }
       return newValue;
   }

   public void selectAll() {
       editor.selectAll();
       editor.requestFocus();
   }

   // This used to do something but now it doesn't.  It couldn't be
   // removed because it would be an API change to do so.
   public void focusGained(FocusEvent e) {}

   // This used to do something but now it doesn't.  It couldn't be
   // removed because it would be an API change to do so.
   public void focusLost(FocusEvent e) {}

   public void addActionListener(ActionListener l) {
       editor.addActionListener(l);
   }

   public void removeActionListener(ActionListener l) {
       editor.removeActionListener(l);
   }

   static class BorderlessTextField extends TextField
   {
      private static final long serialVersionUID = -2092030076472151063L;

      public BorderlessTextField()
      {
         super();
      }

      public void setText(String s)
      {
         if (getText().equals(s))
         {
            return;
         }
         super.setText(s);
      }

      public void setBorder(Border b)
      {
         super.setBorder(b);
      }
   }

   /**
    * A subclass of BasicComboBoxEditor that implements UIResource.
    * BasicComboBoxEditor doesn't implement UIResource
    * directly so that applications can safely override the
    * cellRenderer property with BasicListCellRenderer subclasses.
    * <p>
    * <strong>Warning:</strong>
    * Serialized objects of this class will not be compatible with
    * future Swing releases. The current serialization support is
    * appropriate for short term storage or RMI between applications running
    * the same version of Swing. As of 1.4, support for long term storage
    * of all JavaBeans&trade;
    * has been added to the <code>java.beans</code> package.
    * Please see {@link java.beans.XMLEncoder}.
    */
   public static class UIResource extends BasicComboBoxEditor implements javax.swing.plaf.UIResource
   {
   }
}