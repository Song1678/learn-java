package com.learn.web.w04_exception;

import com.learn.web.common.ServiceException;
import com.learn.web.w04_exception.domain.EmployeeBo;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

/**
 * 员工管理（已写好，内存版）。业务上不允许的情况直接抛 ServiceException，不用 return R.fail(...)。
 */
@Service
public class EmployeeService {

    private final Map<Long, EmployeeBo> data = new TreeMap<>();
    private long nextId;

    public EmployeeService() {
        reset();
    }

    /** 恢复初始数据（测试前会调用） */
    public synchronized void reset() {
        data.clear();
        nextId = 1;
        EmployeeBo admin = new EmployeeBo();
        admin.setUsername("admin");
        admin.setName("管理员");
        admin.setPassword("123456");
        admin.setStatus(1);
        add(admin);
    }

    public synchronized Long add(EmployeeBo bo) {
        boolean exists = data.values().stream().anyMatch(e -> e.getUsername().equals(bo.getUsername()));
        if (exists) {
            throw new ServiceException("账号已存在");
        }
        bo.setId(nextId++);
        if (bo.getStatus() == null) {
            bo.setStatus(1);
        }
        data.put(bo.getId(), bo);
        return bo.getId();
    }

    public synchronized void edit(EmployeeBo bo) {
        EmployeeBo old = getById(bo.getId());
        old.setUsername(bo.getUsername());
        old.setName(bo.getName());
        old.setPhone(bo.getPhone());
        if (bo.getStatus() != null) {
            old.setStatus(bo.getStatus());
        }
        if (bo.getPassword() != null) {
            old.setPassword(bo.getPassword());
        }
    }

    /** 不存在：ServiceException("员工不存在", 404) —— 第二个参数是返回给前端的 code */
    public synchronized EmployeeBo getById(Long id) {
        EmployeeBo e = data.get(id);
        if (e == null) {
            throw new ServiceException("员工不存在", 404);
        }
        return e;
    }
}
