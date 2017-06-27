package lucky.util.lang;

import java.lang.reflect.Field;

/**
 * @Author:chaoqiang.zhou
 * @Description:获取实体得get和set的方法，需要考虑boolean的操作，看下面的user实体类就知道
 * @Date:Create in 13:46 2017/6/27
 */
public class FieldsUtil {

    public static String getGetterName(Field f) {
        String getter = f.getName();
        if (f.getType() != boolean.class) {
            getter = "get" + firstCharacterToUppder(getter);
        } else if (!startWithIs(getter)) {
            getter = "is" + firstCharacterToUppder(getter);
        }
        return getter;
    }

    public static String getSetterName(Field f) {
        String name = f.getName();
        if (f.getType() == boolean.class && startWithIs(name)) {
            name = name.substring(2);
        }
        return "set" + firstCharacterToUppder(name);
    }

    private static boolean startWithIs(String name) {
        return name.startsWith("is") && name.length() > 2 && Character.isUpperCase(name.charAt(2));
    }


    private static String firstCharacterToUppder(String name) {
        char[] methodName = name.toCharArray();
        methodName[0] = toUpperCase(methodName[0]);
        return String.valueOf(methodName);
    }

    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    public static char toUpperCase(char chars) {
        if (97 <= chars && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    public static void main(String[] args) {
//        firstCharacterToUppder("sssd");
        System.out.println(firstCharacterToUppder("WEsdf"));
        System.out.println(firstCharacterToUppder("sfdaSDDF"));
    }

//    public class User {
//
//        private String name;
//
//        private String userNameName;
//
//        private boolean isTestTest;
//
//        private boolean iStea;
//        private boolean lala;
//        private boolean Laasdf;
//
//        private boolean ISASDF;
//
//        private String Ddasd;
//
//
//        public String getName() {
//            return name;
//        }
//
//        public String getUserNameName() {
//            return userNameName;
//        }
//
//        public boolean isTestTest() {
//            return isTestTest;
//        }
//
//        public boolean isiStea() {
//            return iStea;
//        }
//
//        public boolean isLala() {
//            return lala;
//        }
//
//        public boolean isLaasdf() {
//            return Laasdf;
//        }
//
//        public boolean isISASDF() {
//            return ISASDF;
//        }
//
//        public String getDdasd() {
//            return Ddasd;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public void setUserNameName(String userNameName) {
//            this.userNameName = userNameName;
//        }
//
//        public void setTestTest(boolean testTest) {
//            isTestTest = testTest;
//        }
//
//        public void setiStea(boolean iStea) {
//            this.iStea = iStea;
//        }
//
//        public void setLala(boolean lala) {
//            this.lala = lala;
//        }
//
//        public void setLaasdf(boolean laasdf) {
//            Laasdf = laasdf;
//        }
//
//        public void setISASDF(boolean ISASDF) {
//            this.ISASDF = ISASDF;
//        }
//
//        public void setDdasd(String ddasd) {
//            Ddasd = ddasd;
//        }
//    }
}
