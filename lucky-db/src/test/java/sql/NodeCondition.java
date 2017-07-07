package sql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:55 2017/7/3
 */
@Getter
@Setter
@Table(name = "node_condition")
public class NodeCondition {
    @Id
    @Column(name = "condition_id")
    private long conditionId;
    @Column(name = "node_id")
    private long nodeId;
    @Column(name = "content")
    private String content;
    @Column(name = "user_ids")
    private String userIds;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "approval_rule")
    private int approvalRule;
    @Column(name = "node_condition")
    private String condition;
    @Column(name = "condition_index")
    private int conditionIndex; //条件的索引序号
    @Column(name = "rule_condition")
    private String ruleCondition;
    @Column(name = "flag")
    private int flag;          //标签字段，代表当前改条件正在使用中
    @Column(name = "userNames")
    private String userNames;     //待办任务的用户列表信息
}
