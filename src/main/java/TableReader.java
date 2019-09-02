import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Map;


/**
 * 读取数据库表信息接口
 */
public interface TableReader {

    /**
     * 读取配置文件获取连接
     * @return
     */
    default Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(Config.DRIVER_CLASS_NAME);
            con = DriverManager.getConnection(Config.URL, Config.USER_NAME, Config.PASS_WORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }  catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return con;
    }

    /**
     * 关闭连接
     * @param con
     */
    default void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取表明
     * @return
     */
    List<String> getTableNames(String regx);

    /**
     * 读取表字段信息
     * @return
     */
    List<Column> getColumns(String table);

    /**
     * 读取表字段
     * @param tables
     * @return
     */
    Map<String, List<Column>> getColumns(List<String> tables);

}
