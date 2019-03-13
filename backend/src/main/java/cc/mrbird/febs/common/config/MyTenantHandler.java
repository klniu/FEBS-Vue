package cc.mrbird.febs.common.config;

import cc.mrbird.febs.common.domain.TenantTable;
import cc.mrbird.febs.common.runner.CacheInitRunner;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.domain.User;
import com.baomidou.mybatisplus.extension.plugins.tenant.TenantHandler;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;

public class MyTenantHandler implements TenantHandler {

    @Override
    public Expression getTenantId() {
        // 从当前系统上下文中取出当前请求的服务商ID，通过解析器注入到SQL中。
        User user = FebsUtil.getCurrentUser();
        if (null == user) {
            throw new RuntimeException("getTenantId error.");
        }
        Integer tenantId = user.getTenantId();
        return new LongValue(tenantId);
    }

    @Override
    public String getTenantIdColumn() {
        return TenantTable.getSystemTenantId();
    }

    @Override
    public boolean doTableFilter(String tableName) {
        // 当应用启动过程中，不要使用tenant， 以便加载缓存
        if (!CacheInitRunner.isFinished()) {
            return true;
        }
        // 如果当前用户没有token，说明是登陆，其数据库记录不使用tenant
        if  (FebsUtil.getCurrentUser() == null) {
            return true;
        }
        // 通过是否包含tenant_id来判断
        return !TenantTable.isTenantTable(tableName);

    }
}
