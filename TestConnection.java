import java.sql.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // Carregar o driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Carregar propriedades
            Properties props = loadProperties();
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");
            
            System.out.println("Tentando conectar com:");
            System.out.println("URL: " + url);
            System.out.println("User: " + user);
            System.out.println("Password: " + (password != null ? "***" : "null"));
            
            // Tentar conexão
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Conexão bem-sucedida!");
            
            // Testar uma query simples
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1 as test");
            if (rs.next()) {
                System.out.println("✅ Query de teste executada com sucesso: " + rs.getInt("test"));
            }
            
            conn.close();
            
        } catch (Exception e) {
            System.out.println("❌ Erro na conexão:");
            e.printStackTrace();
        }
    }
    
    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o arquivo db.properties: " + e.getMessage());
        }
    }
}
