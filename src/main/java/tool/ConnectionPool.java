package tool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionPool {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionPool.class);
    private static final HikariDataSource datasource;
    private static final Properties properties = new Properties();

    static {
        logger.info("Initializing connection pool...");

        HikariConfig config = new HikariConfig();

        try (InputStream inputStream = ConnectionPool.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (inputStream == null) {
                throw new IOException("Could not find database.properties");
            }

            properties.load(inputStream);
            logger.info("Configuration file loaded successfully.");
        } catch (IOException e){
            logger.error("Could not load database.properties", e);
            throw new RuntimeException();
        }

        config.setJdbcUrl(properties.getProperty("database.url"));
        config.setUsername(properties.getProperty("database.username"));
        config.setPassword(properties.getProperty("database.password"));
        config.setAutoCommit(false);
        config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("database.maxPoolSize")));


        datasource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return datasource;
    }

    public static void close() {
        datasource.close();
    }
}
