public class Application {
    public static void main(String[] args) {

        String table = "pldg_guar_type_first", regx = "^pldg_guar_type_.*";

        //tableGeneration(table, null);

        tablesGeneration(regx);


    }

    public static void tableGeneration(String table, String[] columns) {
        //创建传入表名的Excel模板
        DataGeneration.generationTableExcel(table);
        //创建单标模拟数据
        DataGeneration.generationTableJson(table);
        //创建表html
        DataGeneration.generationTableHtml(table, columns);
    }

    public static void tablesGeneration(String regx) {
        //创建表名满足传入正则表达的excel模板，示例：^rs_t_.* 所有已rs_t_开头的表
        DataGeneration.generationTablesExcel(regx);
        //创建表名满足传入正则表达的模拟数据
        DataGeneration.generationTablesJson(regx);
        //创建表html
        DataGeneration.generationTablesHtml(regx);
    }
}
