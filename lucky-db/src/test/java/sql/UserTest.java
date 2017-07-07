package sql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 11:33 2017/7/3
 */
@Getter
@Setter
@Table(name = "user_test")
public class UserTest {

    @Id
    @Column(name = "msg_id")
    private long msgId;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "msg")
    private String msg;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "msg_type")
    private int msgType;
    @Column(name = "node_id")
    private long nodeId;
    @Column(name = "instance_id")
    private long instanceId;
    @Column(name = "condition_index")
    private int conditionIndex;
}
