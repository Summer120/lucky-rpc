package builder;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 13:42 2017/6/27
 */
public class User {

    private String name;

    private String userNameName;

    private boolean isTestTest;

    private boolean lala;
    public String getName() {
        return name;
    }

    public String getUserNameName() {
        return userNameName;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setUserNameName(String userNameName) {
        this.userNameName = userNameName;
    }

    public boolean isTestTest() {
        return isTestTest;
    }

    public void setTestTest(boolean testTest) {
        isTestTest = testTest;
    }

    public boolean isLala() {
        return lala;
    }


    public void setLala(boolean lala) {
        this.lala = lala;
    }
}
