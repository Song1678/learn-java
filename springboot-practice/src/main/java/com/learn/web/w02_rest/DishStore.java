package com.learn.web.w02_rest;

import com.learn.web.w02_rest.domain.Dish;
import com.learn.web.w02_rest.domain.DishQuery;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 内存版“数据库”（已写好）—— 就是 ch09 你写的 InMemoryMapper，本章专心练 HTTP 接口。
 * 下一阶段（b01 起）会换成真正的 MySQL。
 */
@Component
public class DishStore {

    private final Map<Long, Dish> data = new TreeMap<>();
    private long nextId;

    public DishStore() {
        reset();
    }

    /** 恢复成初始的 6 道菜（测试前会调用） */
    public synchronized void reset() {
        data.clear();
        nextId = 1;
        add("宫保鸡丁", 1L, "28.00", 1, "经典川菜", "2024-04-05T10:10:00");
        add("鱼香肉丝", 1L, "26.00", 1, "酸甜微辣", "2024-04-05T10:20:00");
        add("回锅肉", 1L, "32.00", 0, null, "2024-04-06T10:50:00");
        add("拍黄瓜", 2L, "12.00", 1, null, "2024-04-06T11:10:00");
        add("口水鸡", 2L, "32.00", 1, null, "2024-04-07T11:30:00");
        add("米饭", 3L, "2.00", 1, null, "2024-04-07T11:50:00");
    }

    private void add(String name, Long categoryId, String price, int status, String desc, String time) {
        Dish d = new Dish();
        d.setName(name);
        d.setCategoryId(categoryId);
        d.setPrice(new BigDecimal(price));
        d.setStatus(status);
        d.setDescription(desc);
        d.setCreateTime(LocalDateTime.parse(time));
        insert(d);
    }

    /** 插入，自动生成 id，返回 id。createTime 为空时设为当前时间 */
    public synchronized Long insert(Dish dish) {
        dish.setId(nextId++);
        if (dish.getCreateTime() == null) {
            dish.setCreateTime(LocalDateTime.now().withNano(0));
        }
        data.put(dish.getId(), dish);
        return dish.getId();
    }

    /** 不存在返回 null */
    public synchronized Dish findById(Long id) {
        return data.get(id);
    }

    /** 全部，按 id 升序 */
    public synchronized List<Dish> findAll() {
        return new ArrayList<>(data.values());
    }

    /** 条件查询：名称包含 name、分类等于 categoryId、状态等于 status，为 null 的条件忽略；按 id 升序 */
    public synchronized List<Dish> query(DishQuery q) {
        return data.values().stream()
                .filter(d -> q.getName() == null || q.getName().isBlank() || d.getName().contains(q.getName()))
                .filter(d -> q.getCategoryId() == null || q.getCategoryId().equals(d.getCategoryId()))
                .filter(d -> q.getStatus() == null || q.getStatus().equals(d.getStatus()))
                .toList();
    }

    /** 不为 null 的字段才更新。id 不存在返回 false */
    public synchronized boolean update(Dish dish) {
        Dish old = data.get(dish.getId());
        if (old == null) {
            return false;
        }
        if (dish.getName() != null) old.setName(dish.getName());
        if (dish.getCategoryId() != null) old.setCategoryId(dish.getCategoryId());
        if (dish.getPrice() != null) old.setPrice(dish.getPrice());
        if (dish.getStatus() != null) old.setStatus(dish.getStatus());
        if (dish.getDescription() != null) old.setDescription(dish.getDescription());
        return true;
    }

    /** 返回实际删除的条数 */
    public synchronized int deleteByIds(List<Long> ids) {
        int n = 0;
        for (Long id : ids) {
            if (data.remove(id) != null) {
                n++;
            }
        }
        return n;
    }
}
