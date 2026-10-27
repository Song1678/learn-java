package com.learn.web.b04_tx;

import com.learn.web.common.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * b04 练习 4.4 ~ 4.6：找 bug —— @Transactional 不生效的三个经典场景
 *
 * <p>三个方法都是“批量导入分类”：逐个插入，遇到名字是 "ERROR" 的就失败。要求失败时一个都不能导入。
 * 现在的代码看起来都加了 @Transactional，但测试会发现失败时前面的分类还是留在了数据库里。找出原因并修好。
 */
@Service
public class TxPitfalls {

    private final TxMapper txMapper;

    public TxPitfalls(TxMapper txMapper) {
        this.txMapper = txMapper;
    }

    /**
     * 4.4 失败时抛出的是 IOException（受检异常）。
     * <p>提示：@Transactional 默认只在 RuntimeException 和 Error 时回滚，受检异常照样提交！
     * 修法：@Transactional(rollbackFor = Exception.class) —— RuoYi-Vue-Plus 里几乎都这么写。
     */
    // @swap
    @Transactional(rollbackFor = Exception.class)
    //> @Transactional
    // @end
    public void importChecked(List<String> names) throws IOException {
        for (String name : names) {
            if ("ERROR".equals(name)) {
                throw new IOException("导入失败：" + name);
            }
            txMapper.insertCategory(name);
        }
    }

    /**
     * 4.5 失败时把异常 catch 住，只打印了一下。
     * <p>Spring 只有“看到”异常从方法里抛出来才会回滚，你自己 catch 掉了，它就以为一切正常，照样提交。
     * 修法：catch 之后要重新抛出：throw new ServiceException("导入失败：" + e.getMessage())
     */
    @Transactional(rollbackFor = Exception.class)
    public void importSwallow(List<String> names) {
        try {
            for (String name : names) {
                if ("ERROR".equals(name)) {
                    throw new IllegalArgumentException("非法名称 " + name);
                }
                txMapper.insertCategory(name);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("导入出错：" + e.getMessage());
            // @hide 4.5 这里少了什么？
            throw new ServiceException("导入失败：" + e.getMessage());
            // @end
        }
    }

    /**
     * 4.6 外部调用的是 importSelfCall（没有 @Transactional），它再调用本类的 doImport（有 @Transactional）。
     * <p>事务是靠“代理对象”实现的：外部调用 txPitfalls.importSelfCall(...) 时，txPitfalls 是代理对象；
     * 但在方法内部调用 this.doImport(...)，this 是原始对象，绕过了代理，@Transactional 不生效。
     * 修法：最简单的是把 @Transactional 加到外部调用的 importSelfCall 上。
     */
    // @hide 4.6 这里少了什么？
    @Transactional(rollbackFor = Exception.class)
    // @end
    public void importSelfCall(List<String> names) {
        this.doImport(names);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doImport(List<String> names) {
        for (String name : names) {
            if ("ERROR".equals(name)) {
                throw new ServiceException("导入失败：" + name);
            }
            txMapper.insertCategory(name);
        }
    }
}
