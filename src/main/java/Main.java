import com.mysql.fabric.jdbc.FabricMySQLDriver;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.sql.*;
import java.util.Vector;

public class Main extends JFrame {

    private static final String URL = ("jdbc:mysql://localhost:3306/new_schema");
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2015";

    public static void main(String[] args) throws SQLException {

// ПОДКЛЮЧЕНИЕ К БАЗЕ ДАННЫХ MySQL

        Connection connection;

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);

            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            if (!connection.isClosed()) {
                System.out.println("connection is OPEN");
            }
// ВЫПОЛНЕНИЕ СТАТИЧЕСКИХ ЗАПРОСОВ К БАЗЕ ДАННЫХ MySQL

            Statement statement = connection.createStatement();

            statement.execute("DELETE FROM new_table;");
            statement.execute("INSERT  INTO new_table (new_tablecol) VALUE ('theExecutionOf.EXECUTE');");

            statement.addBatch("INSERT INTO new_table (new_tablecol) VALUE ('theExecutionOf.ADDBATCH1');");
            statement.addBatch("INSERT INTO new_table (new_tablecol) VALUE ('theExecutionOf.ADDBATCH2');");
            statement.addBatch("INSERT INTO new_table (new_tablecol) VALUE ('theExecutionOf.ADDBATCH3');");
            statement.addBatch("INSERT INTO new_table (new_tablecol) VALUE ('theExecutionOf.ADDBATCH4');");
            statement.addBatch("INSERT INTO new_table (new_tablecol) VALUE ('theExecutionOf.ADDBATCH5');");

            statement.executeBatch();

// ВЫВОД ДАННЫХ ТАБЛИЦЫ НА ЭКРАН

            String query = ("SELECT * FROM new_table;");
            ResultSet result = statement.executeQuery(query);


//            //ВАРИАНТ #1
            ResultSetMetaData resultSMD = result.getMetaData();
            int x = result.getMetaData().getColumnCount();
//
//            while (result.next()) {
//                for (int i = 1; i <= x; i++) {
//                    System.out.print(result.getString(i) + "\t");
//                }
//                System.out.println();
//            }
//
//            //ВАРИАНТ #2
//            while (result.next()) {
//
//                int id = result.getInt("idnew_table");
//                String name = result.getString("new_tablecol");
//
//                System.out.println("ID: " + id + " Name: " + name);
//            }

            //ВАРИАНТ #3

            DefaultTableModel tableMod = new DefaultTableModel();

            Vector column_name = new Vector();
            for (int i = 1; i <= x; i++) {
                column_name.addElement(resultSMD.getColumnName(i));
            }
            tableMod.setColumnIdentifiers(column_name);
            System.out.println(column_name);

            while (result.next()) {
                Vector data_rows = new Vector();                   // !!!  Location is important!
                for (int j = 1; j <= x; j++) {
                    data_rows.addElement((result.getString(j)));
                }
                tableMod.addRow((data_rows));
                System.out.println(data_rows);
            }

            final JTable myTable = new JTable();
            myTable.setModel(tableMod);
            myTable.setAutoCreateRowSorter(true);
//            myTable.setEnabled(false);                           // How to make the JTable Uneditable


            JFrame frame = new JFrame("MySQL output");
            frame.setPreferredSize(new Dimension(400, 200));
            frame.setLocationRelativeTo(null);
            frame.setDefaultLookAndFeelDecorated(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(myTable);


            JScrollPane scrollPane = new JScrollPane(myTable);      // !!!  Location is important!
            frame.add(scrollPane);

            JButton button = new JButton("Print");
            ActionListener printAction = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        myTable.print();
                    } catch (PrinterException pe) {
                        System.err.println("Error printing: " + pe.getMessage());
                    }
                }
            };
            button.addActionListener(printAction);
            frame.add(button, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Перехвачено исключение!" + e.getMessage());
        }
    }
}
// http://www.java2s.com/Tutorial/Java/0240__Swing/1060__JTableHeader.htm
// http://www.java2s.com/Tutorial/Java/0240__Swing/PrintingTablesSample.htm
// http://stackoverflow.com/questions/22238641/create-vector-for-defaulttablemodel
// http://www.coderanch.com/t/332367/GUI/java/DefaultTableModel-Vector
// http://www.java2s.com/Code/Java/Swing-JFC/DisplayResultSetinTableJTable.htm
// http://www.java2s.com/Tutorial/Java/0240__Swing/publicJTableVectorrowDataVectorcolumnNames.htm
// http://da2i.univ-lille1.fr/doc/tutorial-java/uiswing/components/table.html
// http://alvinalexander.com/java/jwarehouse/hsqldb/src/org/hsqldb/util/GridSwing.java.shtml
// http://stackoverflow.com/questions/21898053/display-records-from-mysql-database-using-jtable-in-java