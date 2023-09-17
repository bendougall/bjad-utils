package bjad.db;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Unit tests for the database wrapper.  
 *
 * @author 
 *  Ben Dougall
 */
@SuppressWarnings("javadoc")
public class DatabaseWrapperTest
{
   private static String FILE_NAME = System.getenv("TEMP") + "\\bjadUtilTest.sqlLite";
   private static String connectionString = "jdbc:sqlite:" + FILE_NAME;
   
   @BeforeAll
   public static void setupDB() throws Exception
   {
      DatabaseWrapper.setGlobalConnectionString(connectionString);
      
      try (DatabaseWrapper db = new DatabaseWrapper("drop table if exists person"))
      {
         db.executeNonQuery();
      }
      try (DatabaseWrapper db = new DatabaseWrapper("create table person (id integer, name string)"))
      {
         db.executeNonQuery();
      }
      
      String insertCommand = "INSERT INTO Person VALUES (?, ?)";
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 1, "mike"))
      {
         db.addToBatch(2, "leo");
         assertEquals(2, db.executeNonQuery(), "Batch job returns 2 affected");
      }
   }
   
   @AfterAll
   public static void eraseDB() throws Exception
   {
      new File(FILE_NAME).delete();
   }
   
   @Test
   public void testAddBatch() throws Exception
   {
      String insertCommand = "INSERT INTO Person VALUES (?, ?)";
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 100, "john"))
      {
         db.addToBatch(101, "jane");
         assertEquals(2, db.executeNonQuery(), "Batch job returns 2 affected");
         
         db.newCommand("SELECT * FROM Person WHERE id = ?", 100);
      }
   }
   
   @Test
   public void testResultProcessor() throws Exception
   {
      try (DatabaseWrapper db = new DatabaseWrapper("SELECT * FROM person"))
      {
         db.executeQuery(new ResultSetProcessor()
            {               
               @Override
               public void processRow(ResultSet rs) throws SQLException
               {
                  int id = rs.getInt("id");
                  String name = rs.getString("name");
                  
                  switch (id)
                  {
                  case 1:
                     assertEquals("mike", name, "Id 1 == mike");
                     break;
                  case 2:
                     assertEquals("leo", name, "id 2 == leo");
                     break;
                  }
               }
            });
      }
   }
   
   @Test
   public void testResultSetMapper() throws Exception
   {
      try (DatabaseWrapper db = new DatabaseWrapper("SELECT * FROM person"))
      {
         List<Person> results = db.executeQuery(new ResultSetMapper<Person>()
            {
               @Override
               public Person processRow(ResultSet rs) throws SQLException
               {
                  Person temp = new Person();
                  temp.id = rs.getInt("id");
                  temp.name = rs.getString("name");
                  return temp;
               }
            });
         
         assertEquals(true, results.size() >= 2, "At least two Person objects returned");
         db.newCommand("SELECT * FROM person");
         results = db.executeQuery(new ResultSetMapper<Person>()
         {
            @Override
            public Person processRow(ResultSet rs) throws SQLException
            {
               Person temp = new Person();
               temp.id = rs.getInt("id");
               temp.name = rs.getString("name");
               return temp;
            }
         });
      
      assertEquals(true, results.size() >= 2, "At least two Person objects returned");
      }
   }
   
   @Test
   public void testBadQueryWithMapper() throws Exception
   {
      assertThrows(SQLException.class, () -> {
         try (DatabaseWrapper db = new DatabaseWrapper("SELECT * FROM person"))
         {
            db.executeQuery(new ResultSetMapper<Person>()
               {
                  @Override
                  public Person processRow(ResultSet rs) throws SQLException
                  {
                     rs.getString("bad_column");
                     return new Person();
                  }
               });
         }
      });
   }
   
   @Test
   public void testBadQueryWithResultProcessor() throws Exception
   {
      assertThrows(SQLException.class, () -> {
         try (DatabaseWrapper db = new DatabaseWrapper("SELECT * FROM person"))
         {
            db.executeQuery(new ResultSetProcessor()
               {               
                  @Override
                  public void processRow(ResultSet rs) throws SQLException
                  {
                     rs.getString("bad_column");
                  }
               });
         }
      });
   }
   
   /** 
    * this test method kinda sucks as sql lite does not support
    * all datatypes, but the code will still execute for coverage
    * 
    * its a hack... big deal.
    */
   @Test
   public void testSetArguments() throws Exception
   {
      String insertCommand = "INSERT INTO Person VALUES (?, ?)";
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 103, 1100L))
      {
          db.executeNonQuery();
      }
      catch (Exception ex) { fail("Long values should be allowed."); }
      
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 104, BigDecimal.ZERO))
      {
          db.executeNonQuery();
      }
      catch (Exception ex) { fail("BigDecimal values should be allowed."); }
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 105, true))
      {
          db.executeNonQuery();
      }
      catch (Exception ex) {fail("Boolean values should be allowed.");}
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, 106, new Date()))
      {
          db.executeNonQuery();
      }
      catch (Exception ex) {fail("Date values should be allowed.");}
      try (DatabaseWrapper db = new DatabaseWrapper(
            insertCommand, new StringBuilder(), new Date()))
      {
          db.executeNonQuery();
          fail("StringBuilder is not a valid argument type.");
      }
      catch (Exception ex) {}
   }
}

class Person
{
   public int id = 0;
   public String name = "";
}
