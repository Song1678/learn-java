package com.learn.ch04_reflection;

/**
 * 用户实体（已写好），用来做反射练习的“小白鼠”。
 */
public class SysUser extends BaseEntity {

    /** 静态字段不属于对象，反射处理字段时一般要跳过 */
    public static final String TABLE_NAME = "sys_user";

    private Long userId;
    private String userName;
    private String nickName;
    private int age;
    private String password;

    public SysUser() {
    }

    public SysUser(Long userId, String userName, String nickName, int age, String password) {
        this.userId = userId;
        this.userName = userName;
        this.nickName = nickName;
        this.age = age;
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public int getAge() {
        return age;
    }

    public String greet(String other) {
        return nickName + " 向 " + other + " 打招呼";
    }

    public int add(int a, int b) {
        return a + b;
    }

    /** 私有方法：正常情况下外部调不到，反射 + setAccessible(true) 可以 */
    private String secret() {
        return "密码是 " + password;
    }
}
