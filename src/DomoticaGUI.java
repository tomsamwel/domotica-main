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

        initMeasurementsTable();


        // Frame Size
        f.setSize(500, 200);
        // Frame Visible = true
        f.setVisible(true);

    }

    @Override
    public void run() {
        updateMeasurementsTable();
    }

    void initMeasurementsTable(){
        String[][] data = {
                { "Temperatuur", "-", "" },
                { "Lichtsterkte", "-", "" },
                { "Luchtdruk", "-", "" },
                { "Luchtvochtigheid", "-", "" }
        };

        // Column Names
        String[] columnNames = { "Naam", "Waarde", "Laatst gemeten" };
        domoticaTable = new JTable(data, columnNames);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(domoticaTable);
        f.add(sp);
    }

    public void updateMeasurementsTable(){
        System.out.println("updating data...");
        try {
            Object[] temperature = db.getTemperature();
            domoticaTable.setValueAt(temperature[1].toString(), 0, 1);
            domoticaTable.setValueAt(temperature[2].toString(), 0, 2);
        } catch (Exception e){
            System.out.println("Failed to retrieve temperature");
        }

        try {
            Object[] light = db.getLight();
            domoticaTable.setValueAt(light[1].toString(), 1, 1);
            domoticaTable.setValueAt(light[2].toString(), 1, 2);
        } catch (Exception e){
            System.out.println("Failed to retrieve light");
        }

        try {
            Object[] pressure = db.getPressure();
            domoticaTable.setValueAt(pressure[1].toString(), 2, 1);
            domoticaTable.setValueAt(pressure[2].toString(), 2, 2);
        } catch (Exception e){
            System.out.println("Failed to retrieve ressure");
        }

        try {
            Object[] humidity = db.getHumidity();
            domoticaTable.setValueAt(humidity[1].toString(), 3, 1);
            domoticaTable.setValueAt(humidity[2].toString(), 3, 2);
        } catch (Exception e){
            System.out.println("Failed to retrieve humidity");
        }

    }


}
