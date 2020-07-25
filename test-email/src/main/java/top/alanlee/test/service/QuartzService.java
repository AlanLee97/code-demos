package top.alanlee.test.service;

import top.alanlee.test.dto.MailDTO;

public interface QuartzService{
    void startJob(String time, String jobName, String group, Class job, MailDTO mailDTO);

    /****
     * 暂停一个任务
     * @param triggerName
     * @param triggerGroupName
     */
    void pauseJob(String triggerName, String triggerGroupName);
    /****
     * 暂停重启一个定时器任务
     * @param triggerName
     * @param triggerGroupName
     */
    void resumeJob(String triggerName, String triggerGroupName);

    /****
     * 删除一个定时器任务，删除了，重启就没什么用了
     * @param triggerName
     * @param triggerGroupName
     */
    void deleteJob(String triggerName, String triggerGroupName);

    /***
     * 根据出发规则匹配任务，立即执行定时任务，暂停的时候可以用
     */
    void doJob(String triggerName, String triggerGroupName);

    /***
     * 开启定时器，这时才可以开始所有的任务，默认是开启的
     */
    void startAllJob();

    /**
     * 关闭定时器，则所有任务不能执行和创建
     */
    void shutdown();

}
