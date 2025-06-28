import java.sql.*;

public class RDSConnector {

    static Connection conn;

    // 1. Connect to RDS
    public static void connect() {
        String url = "jdbc:mysql://<your-rds-endpoint>:3306/<your-db-name>";
        String username = "<your-username>";
        String password = "<your-password>";

        try {
            conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to RDS");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. Drop tables
    public static void drop() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS stockprice");
            stmt.execute("DROP TABLE IF EXISTS company");
            System.out.println("Tables dropped");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. Create tables
    public static void create() {
        try (Statement stmt = conn.createStatement()) {

            String company = "CREATE TABLE company (" +
                    "id INT PRIMARY KEY," +
                    "name VARCHAR(50)," +
                    "ticker CHAR(10)," +
                    "annualRevenue DECIMAL(12,2)," +
                    "numEmployees INT" +
                    ")";

            String stockPrice = "CREATE TABLE stockprice (" +
                    "companyId INT," +
                    "priceDate DATE," +
                    "openPrice DECIMAL(10,2)," +
                    "highPrice DECIMAL(10,2)," +
                    "lowPrice DECIMAL(10,2)," +
                    "closePrice DECIMAL(10,2)," +
                    "volume INT," +
                    "PRIMARY KEY (companyId, priceDate)," +
                    "FOREIGN KEY (companyId) REFERENCES company(id)" +
                    ")";

            stmt.execute(company);
            stmt.execute(stockPrice);
            System.out.println("Tables created");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. Insert sample data
    public static void insert() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("INSERT INTO company VALUES (1, 'Amazon', 'AMZN', 500000000000.00, 1000000)");
            stmt.executeUpdate("INSERT INTO stockprice VALUES (1, '2024-01-01', 100.25, 105.50, 99.00, 102.75, 1500000)");
            System.out.println("Data inserted");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5. Delete example
    public static void delete() {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM stockprice WHERE companyId = 1");
            System.out.println("Deleted stockprice record");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 6. Query example
    public static void query() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM company")) {
            while (rs.next()) {
                System.out.println("Company: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        connect();
        drop();
        create();
        insert();
        query();
        delete();
    }
}

