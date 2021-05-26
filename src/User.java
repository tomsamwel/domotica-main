import java.util.ArrayList;

public class User {
    private Long id;
    private String name;
    private int light;
    private int pressure;
    private float temperature;
    private int humidity;

    private Database db = new Database();

    public User() {
    }
    ;

    User(int userId) {
        loadUser(userId);
    }

    void loadUser(int userId) {
        try {
            Object[] result = db.queryOne("SELECT * FROM `users` WHERE id = ?", String.valueOf(userId));
            id = (Long) result[0];
            name = result[1] != null ? (String) result[1] : null;
            if (result[2] != null) light = (int) result[2];
            if (result[3] != null) pressure = (int) result[3];
            if (result[4] != null) temperature = (int) result[4];
            if (result[5] != null) humidity = (int) result[5];
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void loadUser(String name) {
        try {
            Object[] result = db.queryOne("SELECT * FROM `users` WHERE name = ?", name);
            id = (Long) result[0];
            name = result[1] != null ? (String) result[1] : null;
            if (result[2] != null) light = (int) result[2];
            if (result[3] != null) pressure = (int) result[3];
            if (result[4] != null) temperature = (int) result[4];
            if (result[5] != null) humidity = (int) result[5];
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public void setTemperatureConfig(int temperature) {
        int result = db.updateOne("UPDATE `users` SET temperature = " + String.valueOf(temperature) + " WHERE `id` = " + String.valueOf(id));
        if (result == 1) this.temperature = temperature;
    }

    public void setLightConfig(int light) {
        int result = db.updateOne("UPDATE `users` SET light = " + String.valueOf(light) + " WHERE `id` = " + String.valueOf(id));
        if (result == 1) this.light = light;
    }

    public void setPressureConfig(int pressure) {
        int result = db.updateOne("UPDATE `users` SET pressure = " + String.valueOf(pressure) + " WHERE `id` = " + String.valueOf(id));
        if (result == 1) this.pressure = pressure;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLight() {
        return light;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return getName();
    }
}
