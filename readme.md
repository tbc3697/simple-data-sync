简单的数据同步工具：

这是一个半成品工具类库，使用需要自己提供数据源配置、读取数据及保存数据的 SQL；


组件说明：

1. 数据抓取器：从管理器中获取 position，按 position 从源库中抓取数据；

2. 数据管理器：
    * 管理数据 position；向数据抓取器提供 position;
    * 缓存所有抓取的数据；
    * 接受消费者的订阅，将数据推送给消费者；
    * 追踪消费状态，当所有消费者完成消费后，刷新 position;
3. 数据消费者：向管理器订阅数据，收到数据后进行处理；


线程池：

1. 定时任务调度线程池（spring) [ST]；
2. 抓取任务线程池 （EP)；
3. 处理任务线程池  (PP)；

流程：
1. [ST] 定时任务调度器调度数据抓取器，创建抓取任务，并提交 EP 进行处理；
2. [EP] 抓取任务从数据管理器中获取 position，从源库中抓取数据，向管理器发送数据；
3. [EP] 管理器缓存数据，提取新的 position，包装数据消费任务，提交给 PP 处理；
4. [PP] 处理完成后，回调函数回写 position；


需自行实现：
0. 数据源配置；
1. 具体数据抓取逻辑（SQL）；
2. 具体数据处理逻辑；

示例：

