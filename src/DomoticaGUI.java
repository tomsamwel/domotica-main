import javax.swing.*;
import java.util.*;
import java.util.Timer;


public class DomoticaGUI extends TimerTask {
    JFrame f;
    private JTable domoticaTable;
    private JPanel panel;
    private JList userList;



    Database db = new Database();
    User user;

    DomoticaGUI() {
        // Frame initialization
        f = new JFrame();
        f.setTitle("Domotica");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation

        initUserList();


        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);

        user = new User(1);

    }

    @Override
    public void run() {
        updateMeasurementsTable();
    }

    void initMeasurementsTable() {
        String[][] data = {
                {"Temperatuur", "-", "", ""},
                {"Lichtsterkte", "-", "", ""},
                {"Luchtdruk", "-", "", ""},
                {"Luchtvochtigheid", "-", "", ""}
        };

        // Column Names
        String[] columnNames = {"Naam", "Waarde", "Laatst gemeten", "grenswaarde"};
        domoticaTable = new JTable(data, columnNames);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(domoticaTable);
        f.add(sp);

        Timer timer = new Timer();
        timer.schedule(this, 0, 5000);
        f.revalidate();

        domoticaTable.setValueAt(String.valueOf(user.getTemperature()), 0, 3);
        domoticaTable.setValueAt(String.valueOf(user.getLight()), 1, 3);
        domoticaTable.setValueAt(String.valueOf(user.getPressure()), 2, 3);
        domoticaTable.setValueAt(String.valueOf(user.getHumidity()), 3, 3);

        domoticaTable.addPropertyChangeListener(evt -> {
            if (!domoticaTable.isEditing()){
                int row = domoticaTable.getEditingRow();
                int column = domoticaTable.getEditingColumn();

                if (row == 0) user.setTemperatureConfig(Integer.parseInt((String) domoticaTable.getValueAt(row, column)) );
                if (row == 1) user.setLightConfig(Integer.parseInt((String) domoticaTable.getValueAt(row, column)) );

            }



        });
    }

    public void updateMeasurementsTable() {
        System.out.println("updating data...");
        try {
            Object[] temperature = db.getTemperature();
            domoticaTable.setValueAt(temperature[1].toString(), 0, 1);
            domoticaTable.setValueAt(temperature[2].toString(), 0, 2);
        } catch (Exception e) {
            System.out.println("Failed to retrieve temperature");
        }

        try {
            Object[] light = db.getLight();
            domoticaTable.setValueAt(light[1].toString(), 1, 1);
            domoticaTable.setValueAt(light[2].toString(), 1, 2);
        } catch (Exception e) {
            System.out.println("Failed to retrieve light");
        }

        try {
            Object[] pressure = db.getPressure();
            domoticaTable.setValueAt(pressure[1].toString(), 2, 1);
            domoticaTable.setValueAt(pressure[2].toString(), 2, 2);
        } catch (Exception e) {
            System.out.println("Failed to retrieve ressure");
        }

        try {
            Object[] humidity = db.getHumidity();
            domoticaTable.setValueAt(humidity[1].toString(), 3, 1);
            domoticaTable.setValueAt(humidity[2].toString(), 3, 2);
        } catch (Exception e) {
            System.out.println("Failed to retrieve humidity");
        }



    }

    void initUserList(){
        userList = new JList(db.getUsers().toArray());
        userList.addListSelectionListener(e -> {
            user = (User) userList.getSelectedValue();
            f.remove(userList);
            f.setTitle("Domotica - " + user.getName());
            initMeasurementsTable();
        });
        f.add(userList);

    }


}
