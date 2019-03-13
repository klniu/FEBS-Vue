package cc.mrbird.febs.common.domain;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.TableInfoHelper;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Order
public class TenantTable {
    public static String getSystemTenantId() {
        return SYSTEM_TENANT_ID;
    }

    private static final String SYSTEM_TENANT_ID = "tenant_id";
    private static Set<String> tenantTables = new HashSet<>();

    static {
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        for (TableInfo info : tableInfos) {
            if (info.getFieldList().stream().anyMatch(f -> f.getColumn().equals(SYSTEM_TENANT_ID))) {
                tenantTables.add(info.getTableName());
            }
        }
    }

    public static boolean isTenantTable(String tableName) {
        return tenantTables.contains(tableName);
    }
}
