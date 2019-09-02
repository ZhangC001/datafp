import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class MysqlTableReader implements TableReader {

    private MysqlTableReader() { }

    private static MysqlTableReader INSTANCE = new MysqlTableReader();

    public static MysqlTableReader mysqlTableReader() {
        return INSTANCE;
    }

    public List<String> getTableNames(String regx) {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        DatabaseMetaData db = null;
        try {
            //获取数据库的元数据
            db = conn.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try (ResultSet rs = db.getTables(null, null, null, new String[] { "TABLE" });){
            //从元数据中获取到所有的表名
            while(rs.next()) {
                if(Pattern.matches(regx, rs.getString(3))) {
                    tableNames.add(rs.getString(3));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableNames;
    }


    @Override
    public List<Column> getColumns(String table) {
        List<Column> list = new ArrayList<>();
        try{
            Connection con = getConnection();
            DatabaseMetaData db = con.getMetaData();
            ResultSet rsColumns = db.getColumns(null, "%", table, "%");
            while (rsColumns.next()) {
                Column column = new Column();
                column.setTableEn(table);
                column.setNameEn(rsColumns.getString("COLUMN_NAME"));
                column.setNameCn(rsColumns.getString("REMARKS"));
                column.setType(rsColumns.getString("TYPE_NAME") + "(" + rsColumns.getString("COLUMN_SIZE") + ")");
                column.setIsNull(rsColumns.getString("IS_NULLABLE").equals("YES")?"":"N");
                column.setIsPk(rsColumns.getString("IS_AUTOINCREMENT").equals("YES")?"1":"");
                column.setFieldName(CommonTool.camel(rsColumns.getString("COLUMN_NAME")));
                column.setTypeName(rsColumns.getString("TYPE_NAME"));
                column.setColumnSize(rsColumns.getString("COLUMN_SIZE"));
                list.add(column);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public Map<String, List<Column>> getColumns(List<String> tables) {
        Map<String, List<Column>> map = new HashMap<>(tables.size());
        tables.forEach(s -> map.put(s, getColumns(s)));
        return map;
    }
}
