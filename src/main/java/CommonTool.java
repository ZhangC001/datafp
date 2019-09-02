import com.mysql.cj.jdbc.result.ResultSetImpl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonTool {

    /**
     * 下划线转小驼峰
     * @param column
     * @return
     */
    public static String camel(String column) {
        Pattern pattern = Pattern.compile("_(\\w)");
        column = column.toLowerCase();
        Matcher matcher = pattern.matcher(column);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
