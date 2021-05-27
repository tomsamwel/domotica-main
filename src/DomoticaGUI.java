import javax.swing.*;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.*;


public class DomoticaGUI extends TimerTask {
    private final JFrame f;
    private final Database db = new Database();
    private User user;
    private JTable domoticaTable;
    private JPanel panel;
    private JTabbedPane tabs;

    DomoticaGUI() {
        // Frame initialization
        f = new JFrame();
        f.setTitle("Domotica");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//specifying close operation
        // Frame Size
        f.setSize(500, 200);

        initUserSelect();

        // Frame Visible = true
        f.setVisible(true);
    }

    @Override
    public void run() {
        updateMeasurementsTable();
    }

    //instantiate the measurements table
    void initMeasurementsTable() {
        String[][] data = {
                {"Temperatuur", "-", "", ""},
                {"Lichtsterkte", "-", "", ""},
                {"Luchtdruk", "-", "", ""},
                {"Luchtvochtigheid", "-", "", ""}
        };

        // Column Names
        String[] columnNames = {"Naam", "Waarde", "Laatst gemeten"};
        domoticaTable = new JTable(data, columnNames);

        // adding it to JScrollPane
        JScrollPane sp = new JScrollPane(domoticaTable);

        // add a timer schedule to update the data every 5s
        Timer timer = new Timer();
        timer.schedule(this, 0, 5000);

        tabs.addTab("Overzicht", sp);

        f.revalidate();
    }

    //Update all measurements in the table
    public void updateMeasurementsTable() {
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

    // create the user selection panel
    void initUserSelect() {
        f.setTitle("Welkom! Selecteer de gebruiker");

        // load all users and display them as buttons
        List<User> users = db.getUsers();
        users.forEach(user -> {
            JButton userButton = new JButton(user.getName());

            // random color per user for style
            Random rand = new Random();
            userButton.setForeground(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));

            // select a user and switch to domotica panel
            userButton.addActionListener(e -> {
                this.user = user;
                f.remove(panel);
                f.setTitle("Domotica - " + user.getName());
                initDomotica();
            });
            panel.add(userButton);
        });

        // fields for a new user
        JTextField newUserInput = new JTextField("Nieuwe gebruiker");
        JButton newUserButton = new JButton("Maak aan");

        // insert new user and use as current user
        newUserButton.addActionListener(e -> {
            user = new User();
            db.newUser(newUserInput.getText());
            user.loadUser(newUserInput.getText());
            f.remove(panel);
            f.setTitle("Domotica - " + newUserInput.getText());
            initDomotica();
        });

        panel.add(newUserInput);
        panel.add(newUserButton);
        f.add(panel);

    }

    void initDomotica() {
        tabs = new JTabbedPane();
        initMeasurementsTable();

        JPanel tempPane = new JPanel();
        JPanel lightPane = new JPanel();
        JPanel humidityPane = new JPanel();
        JPanel pressurePane = new JPanel();

        JTextField tempInput = new JTextField(String.valueOf(user.getTemperature()));
        JTextField lightInput = new JTextField(String.valueOf(user.getLight()));
        JTextField humidityInput = new JTextField(String.valueOf(user.getHumidity()));
        JTextField pressureInput = new JTextField(String.valueOf(user.getPressure()));

        DimensionUIResource inputDimension = new DimensionUIResource(100, 30);

        tempInput.setPreferredSize(inputDimension);
        lightInput.setPreferredSize(inputDimension);
        humidityInput.setPreferredSize(inputDimension);
        pressureInput.setPreferredSize(inputDimension);

        JButton tempButton = new JButton("Opslaan");
        JButton lightButton = new JButton("Opslaan");
//        JButton humidityButton = new JButton("Opslaan");
//        JButton pressureButton = new JButton("Opslaan");

        tempButton.addActionListener(e -> user.setTemperatureConfig(Integer.parseInt(tempInput.getText())));
        lightButton.addActionListener(e -> user.setLightConfig(Integer.parseInt(lightInput.getText())));

        tempPane.add(tempInput);
        tempPane.add(tempButton);

        lightPane.add(lightInput);
        lightPane.add(lightButton);

//        humidityPane.add(humidityInput);
//        humidityPane.add(humidityButton);
//
//        pressurePane.add(pressureInput);
//        pressurePane.add(pressureButton);

        tabs.addTab("Temperatuur", tempPane);
        tabs.addTab("Licht", lightPane);
        tabs.addTab("Luchtvochtigheid", humidityPane);
        tabs.addTab("Luchtdruk", pressurePane);

        List<String> columns = new ArrayList<>();
        columns.add("Waarde");
        columns.add("tijdstip");

        TableModel tempTableModel = new DefaultTableModel(db.queryArray("SELECT `temperature`, `created_at` FROM temperature ORDER BY id DESC").toArray(new Object[][]{}), columns.toArray());
        JTable tempTable = new JTable(tempTableModel);
        tempTable.setPreferredSize(new DimensionUIResource(450, 200));
        tempPane.add(tempTable);

        TableModel lightTableModel = new DefaultTableModel(db.queryArray("SELECT `light`, `created_at` FROM light ORDER BY id DESC").toArray(new Object[][]{}), columns.toArray());
        JTable lightTable = new JTable(lightTableModel);
        lightTable.setPreferredSize(new DimensionUIResource(450, 200));
        lightPane.add(lightTable);

        TableModel humidityTableModel = new DefaultTableModel(db.queryArray("SELECT `humidity`, `created_at` FROM humidity ORDER BY id DESC").toArray(new Object[][]{}), columns.toArray());
        JTable humidityTable = new JTable(humidityTableModel);
        humidityTable.setPreferredSize(new DimensionUIResource(450, 200));
        humidityPane.add(humidityTable);

        TableModel pressureTableModel = new DefaultTableModel(db.queryArray("SELECT `pressure`, `created_at` FROM pressure ORDER BY id DESC").toArray(new Object[][]{}), columns.toArray());
        JTable pressureTable = new JTable(pressureTableModel);
        pressureTable.setPreferredSize(new DimensionUIResource(450, 200));
        pressurePane.add(pressureTable);

        tabs.setForeground(Color.black);
        f.add(tabs);
        f.revalidate();

    }

}
