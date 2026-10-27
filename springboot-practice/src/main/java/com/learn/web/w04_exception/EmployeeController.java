package com.learn.web.w04_exception;

import com.learn.web.common.AddGroup;
import com.learn.web.common.EditGroup;
import com.learn.web.common.R;
import com.learn.web.w04_exception.domain.EmployeeBo;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * w04 练习 4.3：在 Controller 上开启参数校验
 *
 * <p>注意这里的方法都【没有】try-catch，也没有 if 判断参数：
 * 参数不合法 → 校验框架抛异常；业务不允许 → Service 抛 ServiceException；
 * 这些异常统一交给 GlobalExceptionHandler（练习 4.1）转换成 R.fail(...)。Controller 只管“正常流程”。
 *
 * <p>类上的 {@code @Validated}：开启“方法参数校验”，这样直接写在 @PathVariable / @RequestParam 参数上的 @Min 等注解才会生效
 * （RuoYi-Vue-Plus 的 Controller 类上都有它）。它已经写好了。
 */
@Validated
@RestController
@RequestMapping("/w04/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 4.3 POST /w04/employees → 新增员工，返回 R.ok(id)。按【新增】分组校验请求体。
     */
    @PostMapping
    // TODO 4.3 给参数加上 @Validated 注解，指定 AddGroup 分组
    public R<Long> add(@RequestBody EmployeeBo bo) {
        return R.ok(employeeService.add(bo));
    }

    /**
     * 4.3 PUT /w04/employees → 修改员工，返回 R.ok()。按【修改】分组校验请求体。
     */
    @PutMapping
    // TODO 4.3 给参数加上 @Validated 注解，指定 EditGroup 分组
    public R<Void> edit(@RequestBody EmployeeBo bo) {
        employeeService.edit(bo);
        return R.ok();
    }

    /**
     * 4.3 GET /w04/employees/{id} → 查询员工。id 必须 &gt;= 1，否则校验失败，message 为 "id必须大于0"。
     * 员工不存在时 Service 会抛 ServiceException("员工不存在", 404)。
     */
    @GetMapping("/{id}")
    // TODO 4.3 给 id 参数加上 @Min 校验
    public R<EmployeeBo> getById(@PathVariable Long id) {
        return R.ok(employeeService.getById(id));
    }

    /**
     * GET /w04/employees/search?keyword=xx（已写好）：keyword 是必传参数，不传时 Spring 抛 MissingServletRequestParameterException
     */
    @GetMapping("/search")
    public R<String> search(@RequestParam String keyword) {
        return R.ok("搜索：" + keyword);
    }

    /**
     * GET /w04/employees/boom（已写好）：模拟一个程序 bug，抛出 NullPointerException
     */
    @GetMapping("/boom")
    public R<Void> boom() {
        String s = null;
        s.length();
        return R.ok();
    }
}
