package pub.tbc.open.sync.task.mapper.master;



import pub.tbc.open.sync.task.entity.SyncTask;

import java.util.List;

/**
 * @author tbc by 2020-07-22
 */
public interface SyncTaskMapper {

    int insert(SyncTask task);

    int update(SyncTask task);

    int execTaskUpdateStatus(int taskId);

    int finishTaskUpdateStatus(int taskId);

    List<SyncTask> findEnableTasks();

    List<SyncTask> findList(Integer taskStatus);

    int enable(int taskId);

    int disable(int taskId);

}
