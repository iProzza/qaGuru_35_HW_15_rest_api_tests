package demoqa.config;


import org.aeonbits.owner.ConfigFactory;

public class ConfigReader {


    private static final String ENV_PROPERTY = "env";
    private static final String DEFAULT_ENV = "local";

    private static volatile WebConfig instance;

    private ConfigReader() {
        // приватный конструктор для запрета создания экземпляров
    }

    public static WebConfig getInstance() {
        WebConfig localInstance = instance;
        if (localInstance == null) {
            synchronized (ConfigReader.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = createConfig();
                }
            }
        }
        return localInstance;
    }

    private static WebConfig createConfig() {
        // Устанавливаем значение для ${env} в @Config.Sources
        String env = System.getProperty(ENV_PROPERTY, DEFAULT_ENV);
        System.setProperty(ENV_PROPERTY, env);

        return ConfigFactory.create(
                WebConfig.class,
                System.getProperties(),
                System.getenv()
        );
    }
}
