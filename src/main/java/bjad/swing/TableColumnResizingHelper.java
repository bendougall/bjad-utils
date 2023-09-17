package bjad.swing;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * Helper utility used to resize columns in a JTable 
 * implementation based on the content within the 
 * column.
 *
 * @author 
 *   Ben Dougall
 */
public class TableColumnResizingHelper
{
   private TableColumnResizingHelper()
   {}
   
   /**
    * Resizes the columns of the table based on the content in the cells
    * and the viewport of the scroll pane used to display the table. 
    * 
    * All extra space will be placed in the column index passed to the 
    * function. 
    * 
    * @param tbl
    *    The table to resize the columns for.
    * @param scrollPane
    *    The scrollpane displaying the table on the screen. 
    * @param columnGettingExtraSpace
    *    The column to add extra space to. 
    */
   public static void resizeColumnsWithExtraSpaceToSpecificColumn(JTable tbl, JScrollPane scrollPane, int columnGettingExtraSpace)
   {
      resizeColumnsForContent(tbl, scrollPane, columnGettingExtraSpace);
   }
   
   /**
    * Resizes the columns of the table based on the content in the cells
    * and the viewport of the scroll pane used to display the table. 
    * 
    * All extra space will be spread across all the columns in the table.
    * 
    * @param tbl
    *    The table to resize the columns for.
    * @param scrollPane
    *    The scrollpane displaying the table on the screen.
    */
   public static void resizeColumnsForAllColumns(JTable tbl, JScrollPane scrollPane)
   {
      resizeColumnsForContent(tbl, scrollPane, -1);
   }
   
   private static void resizeColumnsForContent(JTable tbl, JScrollPane scrollPane, int columnIndexToResize)
   {
      if (tbl.isShowing() && tbl.getColumnCount() > 0)
      {
         int totalWidth = 0; 
         int columnWidths[] = new int[tbl.getColumnCount()];
         int columnCount = columnWidths.length;
         
         for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex)
         {
            columnWidths[columnIndex] = getPreferredColumnWidth(tbl, columnIndex);
            totalWidth += columnWidths[columnIndex];
         }
         
         int extraWidth = scrollPane.getViewport().getWidth() - totalWidth;
         
         // Deal with extra space in case the data in the table is not equal to 
         // or more than the space in the viewport for the table. 
         if (extraWidth > 0)
         {
            // If we are sizing all the columns equally, we need to split the extra space
            // between all the columns.
            if (columnIndexToResize == -1)
            {
               for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex)
               {
                  columnWidths[columnIndex] += extraWidth / columnCount;
               }
               // Add any remaining extra space to the last column if there is any left over.
               columnWidths[columnWidths.length-1] += (extraWidth % columnCount);
            }
            // If one column gets all the extra space, add the extra space to that column.
            else
            {
               columnWidths[columnIndexToResize] += extraWidth;
            }
         }
         
         // Now that we have all the sizes for each column, actually do the 
         // resizing of the columns now.
         TableColumnModel columnModel = tbl.getColumnModel();
         for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex)
         {
            TableColumn column = columnModel.getColumn(columnIndex);
            tbl.getTableHeader().setResizingColumn(column);
            column.setWidth(columnWidths[columnIndex]);
         }
      }
   }
   
   private static int getPreferredColumnWidth(JTable tbl, int columnIndex)
   {
      int maxWidth = getWidthOfColumnContent(tbl, tbl.getTableHeader().getDefaultRenderer(), -1, columnIndex);
      for (int rowIndex = 0; rowIndex != tbl.getRowCount(); ++rowIndex)
      {
         maxWidth = Math.max(maxWidth, 
               getWidthOfColumnContent(tbl, tbl.getTableHeader().getDefaultRenderer(), rowIndex, columnIndex));
      }
      return maxWidth + tbl.getIntercellSpacing().width;
   }
   
   private static int getWidthOfColumnContent(JTable tbl, TableCellRenderer renderer, int rowIndex, int columnIndex)
   {
      TableColumn column = tbl.getColumnModel().getColumn(columnIndex);
      Object value = rowIndex > -1 ? 
            tbl.getValueAt(rowIndex, columnIndex) : 
            column.getIdentifier(); 
      
      return (int)renderer.getTableCellRendererComponent(
            tbl, 
            value, 
            false, 
            false, 
            rowIndex, 
            columnIndex).getPreferredSize().getWidth();
   }
}
