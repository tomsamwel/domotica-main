public class Main {
    public static void main(String[] args) {
//        MusicPlayerGUI gui = new MusicPlayerGUI();
        Database db = new Database();
        db.connect();
        Object[] result = db.queryOne("SELECT * FROM users WHERE id = ?", "1");
        System.out.println(result[1]);

    }
}
