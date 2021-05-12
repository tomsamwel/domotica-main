import javax.swing.*;
import java.util.TimerTask;


public class DomoticaGUI extends TimerTask {
    JFrame f;
    private JTable domoticaTable;
    private JPanel domoticaPanel;

    Database db = new Database();

    DomoticaGUI(){
        // Frame initialization
        f = new JFrame();
        f.setTitle("Domotica");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation

        initTable();

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(domoticaTable);
        f.add(sp);
        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);

        updateTable();
    }

    @Override
    public void run() {
        updateTable();
    }

    void initTable(){
        String[][] data = {
                { "Temperatuur", "-", "" },
                { "Lichtsterkte", "-", "" },
                { "Luchtdruk", "-", "" },
                { "Luchtvochtigheid", "-", "" }
        };
        // Column Names
        String[] columnNames = { "Naam", "Waarde", "Laatst gemeten" };
        domoticaTable = new JTable(data, columnNames);
    }

    public void updateTable(){
        System.out.println("updating data...");
        Object[] temperature = db.getTemperature();
        domoticaTable.setValueAt(temperature[1].toString(), 0, 1);
        domoticaTable.setValueAt(temperature[2].toString(), 0, 2);

        Object[] light = db.getLight();
        domoticaTable.setValueAt(light[1].toString(), 1, 1);
        domoticaTable.setValueAt(light[2].toString(), 1, 2);

        Object[] pressure = db.getPressure();
        domoticaTable.setValueAt(pressure[1].toString(), 2, 1);
        domoticaTable.setValueAt(pressure[2].toString(), 2, 2);

        Object[] humidity = db.getHumidity();
        domoticaTable.setValueAt(humidity[1].toString(), 3, 1);
        domoticaTable.setValueAt(humidity[2].toString(), 3, 2);
    }


}
