package bjad.swing;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * (Description)
 *
 *
 * @author 
 *   bendo
 */
public class BJADResizeDelegate implements ComponentListener, MouseMotionListener
{
   protected Window targetComponent; 
   protected Rectangle originalSize = null;
   protected Point origianlMousePosition = null;
   protected int borderWidthToGrab = 5;

   /**
    * Constructor, setting the window for the resize to work with.
    * @param component
    *    The window to apply the resizer to.
    */
   public BJADResizeDelegate(Window component)
   {
      this(component, 5);
   }
   
   /**
    * Constructor, setting the window for the resizer to work with and 
    * the width of the border of thw window that will trigger the cursor
    * updates.
    * 
    * @param component
    *    The window to apply the resizer to.
    * @param borderWidthToGrab
    *    The width of the border on the width that will trigger the cursor
    *    update to indicate that window can be resized.
    */
   public BJADResizeDelegate(Window component, int borderWidthToGrab)
   {
      this.targetComponent = component; 
      this.borderWidthToGrab = Math.max(1, borderWidthToGrab);
      
      this.targetComponent.addMouseMotionListener(this);
      this.targetComponent.addMouseListener(new MouseAdapter()
      {

         @Override
         public void mousePressed(MouseEvent e)
         {
            if (targetComponent.getCursor() != Cursor.getDefaultCursor())
            {               
               originalSize = targetComponent.getBounds();
               System.out.println("Set original size to " + originalSize.toString());
               
               origianlMousePosition = e.getLocationOnScreen();
               System.out.println("Set original mouse point on screen to " + origianlMousePosition.toString());
            }
         }
         
      });
   }
   
   @Override
   public void componentResized(ComponentEvent e)
   {
      
   }

   @Override
   public void componentMoved(ComponentEvent e)
   {
      
   }

   @Override
   public void componentShown(ComponentEvent e)
   {
      ; // Does nothing for resizing.
   }

   @Override
   public void componentHidden(ComponentEvent e)
   {
      ; // Does nothing for resizing.
   }

   @Override
   public void mouseDragged(MouseEvent e)
   {
      if (targetComponent.getCursor() != Cursor.getDefaultCursor())
      {
         boolean xLocationUpdate = false;
         boolean yLocationUpdate = false;
         
         int widthChange = 0;
         switch (targetComponent.getCursor().getType())
         {
         case Cursor.W_RESIZE_CURSOR:
         case Cursor.NW_RESIZE_CURSOR:
         case Cursor.SW_RESIZE_CURSOR:
            // Handle the repositioning and the resizing as west Edge = Location Change as well as size.
            xLocationUpdate = true;
         case Cursor.E_RESIZE_CURSOR:
         case Cursor.NE_RESIZE_CURSOR:
         case Cursor.SE_RESIZE_CURSOR:
            widthChange = e.getLocationOnScreen().x - origianlMousePosition.x;
            break;
         }
         int heightChange = 0;
         switch (targetComponent.getCursor().getType())
         {
         case Cursor.N_RESIZE_CURSOR:
         case Cursor.NE_RESIZE_CURSOR:
         case Cursor.NW_RESIZE_CURSOR:
            // Handle the repositioning and the resizing as North Edge = Location Change as well as size.
            yLocationUpdate = true;
         case Cursor.S_RESIZE_CURSOR:
         case Cursor.SE_RESIZE_CURSOR:
         case Cursor.SW_RESIZE_CURSOR:
            heightChange = e.getLocationOnScreen().y - origianlMousePosition.y;
            break;
         }
         
         // TODO: Suppress the resize/relocation if the resulting size of the window
         // is going to be lower than the minimum size or greater than the maxmimum size
         // if either property is set.
         
         // TODO: Condense the logic so setLocation is only called once to stop the double
         // draw and flickering on the screen. 
         
         // Move the window on the screen if the size is adjusted from the west edge of the window
         if (xLocationUpdate)
         {
            this.targetComponent.setLocation(originalSize.x + widthChange, originalSize.y);
            widthChange *= xLocationUpdate ? -1 : 1;
         }
         // Move the window on the screen if the size is adjusted from the north edge of the window.
         if (yLocationUpdate)
         {
            this.targetComponent.setLocation(this.targetComponent.getX(), originalSize.y + heightChange);
            heightChange *= yLocationUpdate ? -1 : 1;
         }
         
         // Apply the size change to the screen.
         this.targetComponent.setSize(originalSize.width + widthChange, originalSize.height + heightChange);         
      }
   }

   @Override
   public void mouseMoved(MouseEvent e)
   {
      double mX = e.getPoint().getX();
      double mY = e.getPoint().getY();
      
      double wWidth = this.targetComponent.getWidth();
      double wHeight = this.targetComponent.getHeight();
      
      int cursorToSet = Cursor.DEFAULT_CURSOR;
      
      if (mX > 0 && mX < borderWidthToGrab)
      {
         cursorToSet = Cursor.W_RESIZE_CURSOR;
      }
      else if (mX < wWidth && mX > wWidth - this.borderWidthToGrab)
      {       
         cursorToSet = Cursor.E_RESIZE_CURSOR;
      }
      
      if (mY > 0 && mY < borderWidthToGrab)
      {       
         switch (cursorToSet)
         {
         case Cursor.E_RESIZE_CURSOR:
            cursorToSet = Cursor.NE_RESIZE_CURSOR;
            break;
         case Cursor.W_RESIZE_CURSOR:
            cursorToSet = Cursor.NW_RESIZE_CURSOR;
            break;
         default:
            cursorToSet = Cursor.N_RESIZE_CURSOR;
            break;
         }
      }
      else if (mY < wHeight && mY > wHeight - this.borderWidthToGrab)
      {       
         switch (cursorToSet)
         {
         case Cursor.E_RESIZE_CURSOR:
            cursorToSet = Cursor.SE_RESIZE_CURSOR;
            break;
         case Cursor.W_RESIZE_CURSOR:
            cursorToSet = Cursor.SW_RESIZE_CURSOR;
            break;
         default:
            cursorToSet = Cursor.S_RESIZE_CURSOR;
            break;
         }
      }
      targetComponent.setCursor(new Cursor(cursorToSet));
      
      /*
      if (cursorToSet == Cursor.DEFAULT_CURSOR)
      {
         System.out.println(mY + " :: " + targetComponent.getBounds().toString());
      }
      */
   }
   
}
