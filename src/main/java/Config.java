import java.util.ResourceBundle;

/**
 * 配置信息
 */
public class Config {

    private static ResourceBundle bundle = ResourceBundle.getBundle("config");
    public final static String DRIVER_CLASS_NAME =  bundle.getString("driver-class-name");
    public final static String URL = bundle.getString("url");
    public final static String USER_NAME = bundle.getString("username");
    public final static String PASS_WORD = bundle.getString("password");
    public final static String OUT_PATH = bundle.getString("out-path");
    public final static String DB_TYPE = bundle.getString("db-type").toUpperCase();

}
