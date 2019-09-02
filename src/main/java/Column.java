import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 表信息
 */
@Data
public class Column {

    //模块英文名
    @ExcelProperty(value = "模块英文名")
    private String modelEn;
    //模块中文名
    @ExcelProperty(value = "模块中文名")
    private String modelCn;
    //表英文名
    @ExcelProperty(value = "表英文名")
    private String tableEn;
    //表中文名
    @ExcelProperty(value = "表中文名")
    private String tableCn;

    //字段英文名
    @ExcelProperty(value = "字段英文名")
    private String nameEn;
    //字段中文名
    @ExcelProperty(value = "字段中文名")
    private String nameCn;
    //字段类型
    @ExcelProperty(value = "字段类型")
    private String type;
    //是否允许空
    @ExcelProperty(value = "是否为空")
    private String isNull;
    //是否主键
    @ExcelProperty(value = "是否主键")
    private String isPk;
    //字典类型
    @ExcelProperty(value = "字典类型")
    private String codeType;
    //是否查询条件
    @ExcelProperty(value = "是否查询条件")
    private String isFilter;

    //小驼峰列明
    //@ExcelIgnore
    @ExcelProperty(value = "小驼峰列明")
    private String fieldName;

    @ExcelIgnore
    private String typeName;

    @ExcelIgnore
    private String columnSize;

}
