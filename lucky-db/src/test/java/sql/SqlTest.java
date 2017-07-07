package sql;

import com.lucky.db.executor.ConditionType;
import com.lucky.db.executor.DataBase;
import com.lucky.db.executor.DataBaseFactory;
import com.lucky.db.executor.Executor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 10:57 2017/7/3
 */
public class SqlTest {


    public static void main(String[] args) {

        selectList();
    }


    public static void selectList() {
        DataBase db = DataBaseFactory.open("");
        List<UserTest> userTests = db.select("msg").FROM("user_test").result().all(UserTest.class);
        System.out.println(userTests);
    }


    public static void selectTest() {
        DataBase db = DataBaseFactory.open("");
        UserTest userTest = db.select(UserTest.class).WHERE("msg_id", ConditionType.EQ, "2").result().one(UserTest.class);
        System.out.println(userTest.getMsg());
    }


    public static void selectObj() {
        DataBase db = DataBaseFactory.open("");
        UserTest userTest = db.select("msg_id", "instance_id").FROM("user_test").WHERE("msg_id", ConditionType.EQ, "2").result().one(UserTest.class);
        System.out.println(userTest);
    }


    //事务方法异常测试
    public static void transactionExText() {
        DataBase db = DataBaseFactory.open("");
        UserTest userTest = new UserTest();
        userTest.setMsgId(99972);
        userTest.setConditionIndex(1);
        userTest.setCreateTime("sdf");
        UserTest userTest2 = new UserTest();
        userTest2.setMsgId(99991);
        userTest2.setConditionIndex(1);
        userTest2.setCreateTime("sdf");
        db.begin(tx -> {
            tx.insert(userTest).result();
            tx.insert(userTest2).result();
        });
    }

    //事务方法测试信息
    public static void transactionTest() {
        DataBase db = DataBaseFactory.open("");
        UserTest userTest = new UserTest();
        userTest.setMsgId(99991);
        userTest.setConditionIndex(1);
        userTest.setCreateTime("sdf");
        UserTest userTest2 = new UserTest();
        userTest2.setMsgId(99998);
        userTest2.setConditionIndex(1);
        userTest2.setCreateTime("sdf");
        db.begin(tx -> {
            tx.insert(userTest).result();
            tx.insert(userTest2).result();
        });

    }

    //删除对象测试
    public static void deleteObj() {
        Executor db = DataBaseFactory.open("");
        UserTest userTest = new UserTest();
        userTest.setMsgId(2);
        userTest.setConditionIndex(1);
        userTest.setCreateTime("哈哈");
        userTest.setInstanceId(2323423);
        userTest.setMsg("sadff");
        userTest.setUserName("sdf");
        int result = db.delete(userTest).result().getAffectedCount();
        System.out.println(result);
    }

    //原生的更新语句测试
    public static void updateTest() {
        Executor db = DataBaseFactory.open("");
        int result = db.update("user_test").columns("create_time", "msg").values("啦啦", "lal").where("msg_id", ConditionType.EQ, "1").result().getAffectedCount();
        System.out.println(result);
    }

    //更新对象测试
    public static void updateObj() {
        UserTest userTest = new UserTest();
        userTest.setMsgId(2);
        userTest.setConditionIndex(1);
        userTest.setCreateTime("哈哈哈哈");
        userTest.setInstanceId(2323423);
        userTest.setMsg("啊啊啊");
        userTest.setUserName("sdf");
        int result = DataBaseFactory.open("").update(userTest).result().getAffectedCount();
        System.out.println(result);
    }

    //查询测试信息
    public static void queryTest() {
        DataBaseFactory.open("").select(UserTest.class).WHERE("msg_id", ConditionType.EQ, "28077").result();
    }


    //原生的插入sql语句
    public static void insert() {
        Executor db = DataBaseFactory.open("");
        int result = db.insert("user_test").columns("msg_id", "user_id").values("9999", "haha").result().getAffectedCount();
        System.out.println(result);
    }

    //插入测试信息
    public static void insertTest() {

        long currentTime = System.currentTimeMillis();


        for (int i = 5000; i < 6000; i++) {
            UserTest userTest = new UserTest();
            userTest.setMsgId(i);
            userTest.setConditionIndex(1);
            userTest.setCreateTime("sdf");
            userTest.setInstanceId(2323);
            userTest.setMsg("sadff");
            userTest.setUserName("sdf");
            int result = DataBaseFactory.open("").insert(userTest).result().getAffectedCount();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("所花费时间" + (endTime - currentTime) / 1000);
    }

    //插入集合信息
    public static void insertList() {

        List<Object> testObj = new ArrayList<>();
        for (int i = 20000; i < 30000; i++) {
            UserTest userTest = new UserTest();
            userTest.setMsgId(i);
            userTest.setConditionIndex(1);
            userTest.setCreateTime("sdf");
            userTest.setInstanceId(2323);
            userTest.setMsg("sadff");
            userTest.setUserName("sdf");
            testObj.add(userTest);
        }
        long currentTime = System.currentTimeMillis();
        DataBaseFactory.open("").insert(testObj).result();
        long endTime = System.currentTimeMillis();
        System.out.println("所花费时间" + (endTime - currentTime) / 1000);
    }
}
