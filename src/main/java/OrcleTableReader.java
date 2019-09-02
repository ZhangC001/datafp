import java.util.List;
import java.util.Map;

public class OrcleTableReader implements TableReader {

    private OrcleTableReader() { }

    private static OrcleTableReader INSTANCE = new OrcleTableReader();

    public static OrcleTableReader orcleTableReader() {
        return INSTANCE;
    }

    @Override
    public List<String> getTableNames(String regx) {
        return null;
    }

    @Override
    public List<Column> getColumns(String table) {
        return null;
    }

    @Override
    public Map<String, List<Column>> getColumns(List<String> tables) {
        return null;
    }
}
