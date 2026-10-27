package com.learn.web.w04_exception.domain;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import com.learn.web.w04_exception.validate.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * w04 练习 4.2：给字段加上校验注解
 *
 * <p>规则（message 必须和下面写的一字不差，测试会检查）：
 * <table>
 *   <tr><th>字段</th><th>新增时</th><th>修改时</th></tr>
 *   <tr><td>id</td><td>不检查</td><td>不能为 null："id不能为空"</td></tr>
 *   <tr><td>username</td><td colspan="2">不能为空白："账号不能为空"；长度 4~20："账号长度必须在4到20之间"</td></tr>
 *   <tr><td>name</td><td colspan="2">不能为空白："姓名不能为空"</td></tr>
 *   <tr><td>password</td><td>不能为空白："密码不能为空"</td><td>不检查（不传表示不改密码）</td></tr>
 *   <tr><td>phone</td><td colspan="2">可以不传；传了必须是 11 位、1 开头、第二位 3~9："手机号格式不正确"</td></tr>
 *   <tr><td>status</td><td colspan="2">可以不传；传了只能是 0 或 1：用自定义注解 @Status，"状态只能是0或1"</td></tr>
 * </table>
 *
 * <p><b>分组</b>：Controller 里写 {@code @Validated(AddGroup.class)} 时，【只】检查 groups 里包含 AddGroup 的注解，
 * 没写 groups 的注解不会检查！所以“新增和修改都要检查”的注解要写 {@code groups = {AddGroup.class, EditGroup.class}}。
 * RuoYi-Vue-Plus 的 Bo 类里到处都是这种写法。
 */
@Data
public class EmployeeBo {

    // @hide 4.2 加校验注解（下同）
    @NotNull(message = "id不能为空", groups = EditGroup.class)
    // @end
    private Long id;

    // @hide
    @NotBlank(message = "账号不能为空", groups = {AddGroup.class, EditGroup.class})
    @Size(min = 4, max = 20, message = "账号长度必须在4到20之间", groups = {AddGroup.class, EditGroup.class})
    // @end
    private String username;

    // @hide
    @NotBlank(message = "姓名不能为空", groups = {AddGroup.class, EditGroup.class})
    // @end
    private String name;

    // @hide
    @NotBlank(message = "密码不能为空", groups = AddGroup.class)
    // @end
    private String password;

    // @hide
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确", groups = {AddGroup.class, EditGroup.class})
    // @end
    private String phone;

    // @hide
    @Status(groups = {AddGroup.class, EditGroup.class})
    // @end
    private Integer status;
}
