import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private Properties props = new Properties();

    
    public ConfigLoader(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            props.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading config file: " + e.getMessage());
        }
    }

   
    public int getInt(String key, int defaultValue) {
        return Integer.parseInt(props.getProperty(key, String.valueOf(defaultValue)));
    }

    
    public double getDouble(String key, double defaultValue) {
        return Double.parseDouble(props.getProperty(key, String.valueOf(defaultValue)));
    }

    
    public long getLong(String key, long defaultValue) {
        return Long.parseLong(props.getProperty(key, String.valueOf(defaultValue)));
    }
}