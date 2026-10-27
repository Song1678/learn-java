package com.learn.w03_ioc;

import com.learn.w03_ioc.config.CartSession;
import com.learn.w03_ioc.config.DictCache;
import com.learn.w03_ioc.config.IdGenerator;
import com.learn.w03_ioc.pay.PayService;
import com.learn.w03_ioc.pay.PayStrategy;
import com.learn.w03_ioc.sms.NotifyService;
import com.learn.web.common.ServiceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * w03 用的是自己的启动类 IocApplication（只扫描 com.learn.w03_ioc 包）。
 * 如果所有测试都报 “Failed to load ApplicationContext”，往下翻报错信息，找 Caused by，先做 3.1。
 */
@SpringBootTest(classes = IocApplication.class)
@ActiveProfiles("test")
@DisplayName("w03 IoC 与依赖注入")
class W03IocTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    NotifyService notifyService;

    @Autowired
    PayService payService;

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Test
    @DisplayName("3.1 @Primary 与 @Qualifier")
    void primaryAndQualifier() {
        assertEquals("aliyun", notifyService.primaryName(), "不加注解的参数应该注入 @Primary 的阿里云");
        assertEquals("tencent", notifyService.backupName(), "backup 应该用 @Qualifier 注入腾讯云");
        assertEquals("[aliyun] 13800000001：您的订单已送达", notifyService.send("13800000001", "您的订单已送达"));
        assertEquals("[tencent] 17000000001：您的订单已送达", notifyService.send("17000000001", "您的订单已送达"),
                "阿里云发送失败时要改用腾讯云");
    }

    @Test
    @DisplayName("3.2 @Component 指定 bean 名字")
    void beanNames() {
        assertTrue(context.containsBean("normal"));
        assertTrue(context.containsBean("discount"), "DiscountPay 的 bean 名字应该是 discount");
        assertTrue(context.containsBean("fullReduction"), "FullReductionPay 的 bean 名字应该是 fullReduction");
        assertEquals(3, context.getBeansOfType(PayStrategy.class).size());
    }

    @Test
    @DisplayName("3.3 PayService：注入 Map")
    void payService() {
        assertEquals(List.of("discount", "fullReduction", "normal"), payService.supportedTypes());
        assertEquals(0, bd("88.00").compareTo(payService.pay("discount", bd("100.00"))));
        assertEquals(0, bd("80.00").compareTo(payService.pay("fullReduction", bd("100.00"))));
        assertEquals(0, bd("99.99").compareTo(payService.pay("fullReduction", bd("99.99"))));
        assertEquals(0, bd("50").compareTo(payService.pay("normal", bd("50"))));

        ServiceException e = assertThrows(ServiceException.class, () -> payService.pay("bitcoin", bd("1")));
        assertEquals("不支持的支付方式：bitcoin", e.getMessage());

        assertEquals("fullReduction", payService.bestType(bd("100.00")));
        assertEquals("discount", payService.bestType(bd("50.00")));
        assertEquals("discount", payService.bestType(bd("0")), "金额都是 0 时取字母序靠前的 discount");
    }

    @Test
    @DisplayName("3.4 @Configuration + @Bean")
    void beanMethod() {
        assertTrue(context.getBeanNamesForType(IdGenerator.class).length > 0, "容器里没有 IdGenerator，检查 AppConfig 的 @Bean 方法");
        IdGenerator gen = context.getBean(IdGenerator.class);
        assertEquals(7, gen.getWorkerId(), "workerId 应该读取 application.yml 里的 takeout.worker-id: 7");
        long a = gen.nextId();
        long b = gen.nextId();
        assertEquals(7, a / 1_000_000);
        assertEquals(1, b - a);
        assertSame(gen, context.getBean(IdGenerator.class), "@Bean 默认也是单例");
    }

    @Test
    @DisplayName("3.5 @PostConstruct")
    void postConstruct() {
        DictCache dict = context.getBean(DictCache.class);
        assertEquals(1, dict.getLoadCount(), "load() 应该由 Spring 自动调用，而且只调用一次");
        assertEquals("待付款", dict.label("order_status", 1));
        assertEquals("已取消", dict.label("order_status", 6));
        assertEquals("停售", dict.label("dish_status", 0));
        assertEquals("未知", dict.label("dish_status", 9));
    }

    @Test
    @DisplayName("3.6 单例与原型")
    void scope() {
        assertSame(context.getBean(PayService.class), context.getBean(PayService.class), "默认是单例");
        CartSession a = context.getBean(CartSession.class);
        CartSession b = context.getBean(CartSession.class);
        assertNotSame(a, b, "CartSession 应该是 prototype，每次 getBean 都是新对象");
        a.add("宫保鸡丁");
        assertEquals(0, b.getItems().size());
    }
}
