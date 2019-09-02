import com.alibaba.excel.EasyExcel;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件数据生成类
 */
@Slf4j
public class DataGeneration {

    private static TableReader tableReader =
            "MYSQL".equals(Config.DB_TYPE)?MysqlTableReader.mysqlTableReader():OrcleTableReader.orcleTableReader();

    private static String tableHtml = "<yu-xtable-column label=\"%s\" prop=\"%s\" width=\"110\"></yu-xtable-column>\n";
    private static String formHtml = "<yu-xform-item label=\"%s\" ctype=\"%s\" name=\"%s\" %s></yu-xform-item>\n";

    private static  final String[] HIDDEN_COLUMN =
            {"ID", "CREATOR", "REG_DATE", "CREATE_ORG", "CREATE_TIME", "RECENT_REVISER", "RECENT_REVISE_TIME", "RECENT_REVISE_ORG"};
    private static final Map<String, String> TYPE_MAP = new HashMap<>();

    static {
        TYPE_MAP.put("INT", "num");
        TYPE_MAP.put("INTEGER", "num");
        TYPE_MAP.put("BIGINT", "num");
        TYPE_MAP.put("DOUBLE", "num");
        TYPE_MAP.put("DECIMAL", "num");
        TYPE_MAP.put("DATE", "datepicker");
        TYPE_MAP.put("DATETIME", "datepicker");
    }

    /**
     * 使用传入的表列信息创建模板Excel
     * @param list
     */
    public static void generationExcel(List<Column> list) {
        String path = Config.OUT_PATH + File.separator + list.get(0).getTableEn();
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
        log.info("---->开始创建" + list.get(0).getTableEn() + "表excel文件,文件路径：" + path + File.separator + "template.xlsx");
        EasyExcel.write( path + File.separator + "template.xlsx", Column.class).sheet(list.get(0).getTableEn()).doWrite(list);
    }

    /**
     * 生成单表的Excel模板
     * @param table
     */
    public static void generationTableExcel(String table) {
        generationExcel(tableReader.getColumns(table));
    }

    /**
     * 生成满足正则的表明的excel模板
     * @param regx
     */
    public static void generationTablesExcel(String regx) {
        tableReader.getTableNames(regx).forEach(s -> generationTableExcel(s));
    }

    /**
     * 生成传入列信息的json模拟数据
     * @param list
     */
    public static void generationJsonData(List<Column> list) {
        String path =  getPath(list.get(0).getTableEn());
        mkDirs(path);

        log.info("---->开始创建" + list.get(0).getTableEn() + "表json文件,文件路径：" + path + File.separator
                + CommonTool.camel(list.get(0).getTableEn()) + ".json");
        String[] data = new String[5];
        boolean flag = true;
        for (Column cl:list) {
            if (flag) {
                data[0] = "'" + cl.getFieldName() + "': '" + cl.getFieldName() + "01'";
                data[1] = "'" + cl.getFieldName() + "': '" + cl.getFieldName() + "02'";
                data[2] = "'" + cl.getFieldName() + "': '" + cl.getFieldName() + "03'";
                data[3] = "'" + cl.getFieldName() + "': '" + cl.getFieldName() + "04'";
                data[4] = "'" + cl.getFieldName() + "': '" + cl.getFieldName() + "05'";
                flag = false;
            } else {
                data[0] += ", '" + cl.getFieldName() + "': '" + cl.getFieldName() + "01'";
                data[1] += ", '" + cl.getFieldName() + "': '" + cl.getFieldName() + "02'";
                data[2] += ", '" + cl.getFieldName() + "': '" + cl.getFieldName() + "03'";
                data[3] += ", '" + cl.getFieldName() + "': '" + cl.getFieldName() + "04'";
                data[4] += ", '" + cl.getFieldName() + "': '" + cl.getFieldName() + "05'";
            }
        }
        try (FileWriter writer = new FileWriter(path + File.separator + CommonTool.camel(list.get(0).getTableEn()) + ".json")) {
            writer.write("[\n");
            writer.write("{" + data[0] + "},\n");
            writer.write("{" + data[1] + "},\n");
            writer.write("{" + data[2] + "},\n");
            writer.write("{" + data[3] + "},\n");
            writer.write("{" + data[4] + "}\n");
            writer.write("]");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成传入table的json模拟数据
     * @param table
     */
    public static void generationTableJson(String table) {
        generationJsonData(tableReader.getColumns(table));
    }

    /**
     * 生成满足正则的表的json模拟数据
     * @param regx
     */
    public static void generationTablesJson(String regx) {
        tableReader.getTableNames(regx).forEach(s -> generationTableJson(s));
    }

    public static void generationHtml(List<Column> list, String[] columns) {
        String path =  getPath(list.get(0).getTableEn());
        mkDirs(path);
        log.info("---->开始创建" + list.get(0).getTableEn() + "表html,文件路径：" + path + File.separator
                + CommonTool.camel(list.get(0).getTableEn()) + ".txt");
        try (FileWriter writer = new FileWriter(path + File.separator + CommonTool.camel(list.get(0).getTableEn()) + ".txt")) {
            writer.write(generationYuTableHtml(list));
            writer.write("\n\n\n");
            writer.write(generationYuFormHtml(list));
            if (columns != null) {
                writer.write("\n\n\n");
                writer.write(generationYuFormHtml(list, columns));
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generationTableHtml(String table) {
        generationHtml(tableReader.getColumns(table), null);
    }


    public static void generationTablesHtml(String regx) {
        tableReader.getTableNames(regx).forEach(s -> generationTableHtml(s));
    }

    public static void generationTableHtml(String table, String[] column) {
        generationHtml(tableReader.getColumns(table), column);
    }

    private static String generationYuTableHtml(List<Column> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(item -> stringBuilder.append(String.format(tableHtml, item.getNameCn(), item.getFieldName())));
        return stringBuilder.toString();
    }

    private static String generationYuFormHtml(List<Column> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(item -> stringBuilder.append(String.format(formHtml, item.getNameCn(),fmtType(item) ,item.getFieldName(), other(item))));
        return stringBuilder.toString();
    }

    private static String generationYuFormHtml(List<Column> list, String[] column) {
        StringBuilder stringBuilder = new StringBuilder();
        list.forEach(item -> {
            if (Arrays.asList(column).contains(item.getNameEn())) {
                stringBuilder.append(String.format(formHtml, item.getNameCn(),fmtType(item) ,item.getFieldName(), other(item)));
            }
        });
        return stringBuilder.toString();
    }

    private static String getPath (String table) {
        return Config.OUT_PATH + File.separator + table;
    }

    private static void mkDirs(String path) {
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

    private static String fmtType (Column column) {
        String ctype = "input";
        if (TYPE_MAP.get(column.getType()) != null) {
            ctype = TYPE_MAP.get(column.getType());
        }
        if (column.getType().equals("CHAR(8)") || column.getType().equals("CHAR(14)")) {
            ctype = "datepicker";
        }
        if (column.getNameCn().equals("STATUS")) {
            ctype = "select";
        }
        if(column.getType().equals("VARCHAR")&&Integer.valueOf(column.getColumnSize()) > 100) {
            ctype = "textarea";
        }
        return ctype;
    }

    private static String other (Column column) {
        String other = Arrays.asList(HIDDEN_COLUMN).contains(column.getNameEn())?":hidden=\"true\"":"";
        if(column.getType().equals("VARCHAR")&&Integer.valueOf(column.getColumnSize()) > 100) {
            other = ":rows=\"3\" :colspan=\"24\" placeholder=\"2000个字符以内\"";
        }
        return other;
    }
}
