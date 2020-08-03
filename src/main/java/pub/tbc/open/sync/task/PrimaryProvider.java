package pub.tbc.open.sync.task;

/**
 * 主键提供者
 *
 * @Author tbc by 2020/7/25 3:18 下午
 */
public interface PrimaryProvider<C> {

    C primary();
}
