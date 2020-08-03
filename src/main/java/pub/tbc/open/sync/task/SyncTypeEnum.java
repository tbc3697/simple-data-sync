package pub.tbc.open.sync.task;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author tbc by 2020/7/16 7:57 下午
 */
@Getter
@AllArgsConstructor
public enum SyncTypeEnum {
    AGENT_INFO,

    USER_INFO,
    USER_SETTINGS,

    VIRTUAL_CAPITAL_OPERATION,
    USER_VIRTUAL_WALLET,

    OTC_USER_BILL,
    OTC_USER_BALANCE
}
