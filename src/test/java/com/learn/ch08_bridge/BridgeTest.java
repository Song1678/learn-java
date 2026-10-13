package com.learn.ch08_bridge;

import com.learn.ch08_bridge.ioc.Autowired;
import com.learn.ch08_bridge.ioc.Component;
import com.learn.ch08_bridge.ioc.MiniContainer;
import com.learn.ch08_bridge.ioc.demo.DishController;
import com.learn.ch08_bridge.ioc.demo.DishMapper;
import com.learn.ch08_bridge.ioc.demo.DishMapperImpl;
import com.learn.ch08_bridge.ioc.demo.DishService;
import com.learn.ch08_bridge.lambda.Dish;
import com.learn.ch08_bridge.lambda.LambdaExercises;
import com.learn.ch08_bridge.stream.Order;
import com.learn.ch08_bridge.stream.Order.Item;
import com.learn.ch08_bridge.stream.Order.Status;
import com.learn.ch08_bridge.stream.StreamExercises;
import com.learn.ch08_bridge.time.TimeExercises;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ch08 衔接篇")
class BridgeTest {

    private static BigDecimal bd(String s) {
        return new BigDecimal(s);
    }

    @Nested
    @DisplayName("练习1：Lambda")
    class LambdaTest {
        final List<Dish> dishes = List.of(
                new Dish("宫保鸡丁", "川菜", bd("28"), 100, true),
                new Dish("水煮鱼", "川菜", bd("58"), 80, true),
                new Dish("米饭", "主食", bd("2"), 500, true),
                new Dish("佛跳墙", "粤菜", bd("188"), 5, false),
                new Dish("鱼香肉丝", "川菜", bd("28"), 120, true));

        @Test
        void filterAndMap() {
            assertEquals(List.of(2, 4), LambdaExercises.filter(List.of(1, 2, 3, 4), x -> x % 2 == 0));
            assertEquals(List.of(1, 4, 9), LambdaExercises.map(List.of(1, 2, 3), x -> x * x));
            assertEquals(List.of("A", "B"), LambdaExercises.map(List.of("a", "b"), String::toUpperCase));
        }

        @Test
        void affordable() {
            assertEquals(List.of("宫保鸡丁", "米饭", "鱼香肉丝"), LambdaExercises.affordableOnSaleNames(dishes, 28));
            assertEquals(List.of(), LambdaExercises.affordableOnSaleNames(dishes, 1));
        }

        @Test
        void sorted() {
            List<String> names = LambdaExercises.sorted(dishes).stream().map(Dish::name).toList();
            assertEquals(List.of("佛跳墙", "水煮鱼", "鱼香肉丝", "宫保鸡丁", "米饭"), names);
            assertEquals("宫保鸡丁", dishes.get(0).name(), "不能修改原列表");
        }

        @Test
        void removeOffSale() {
            List<Dish> list = new ArrayList<>(dishes);
            LambdaExercises.removeOffSale(list);
            assertEquals(4, list.size());
            assertTrue(list.stream().allMatch(Dish::onSale));
        }

        @Test
        void discount() {
            Function<BigDecimal, BigDecimal> f = LambdaExercises.discount(bd("0.8"));
            assertEquals(0, bd("80").compareTo(f.apply(bd("100"))));
            assertEquals(0, bd("8").compareTo(f.apply(bd("10"))));
        }
    }

    @Nested
    @DisplayName("练习2：Stream")
    class StreamTest {
        final LocalDateTime d1 = LocalDateTime.of(2024, 5, 1, 12, 0);
        final LocalDateTime d2 = LocalDateTime.of(2024, 5, 2, 18, 30);
        final List<Order> orders = List.of(
                new Order(1L, "tom", Status.COMPLETED, bd("60"), d1,
                        List.of(new Item("宫保鸡丁", 1), new Item("米饭", 2))),
                new Order(2L, "amy", Status.COMPLETED, bd("90.5"), d2,
                        List.of(new Item("水煮鱼", 1), new Item("米饭", 3))),
                new Order(3L, "tom", Status.CANCELLED, bd("200"), d1.plusHours(1),
                        List.of(new Item("佛跳墙", 5))),
                new Order(4L, "bob", Status.PENDING_PAYMENT, bd("30"), d2.plusHours(1),
                        List.of(new Item("宫保鸡丁", 1))),
                new Order(5L, "tom", Status.COMPLETED, bd("28"), d1.minusHours(2),
                        List.of(new Item("宫保鸡丁", 1))));

        @Test
        void revenue() {
            assertEquals(0, bd("178.5").compareTo(StreamExercises.completedRevenue(orders)));
            assertEquals(0, BigDecimal.ZERO.compareTo(StreamExercises.completedRevenue(List.of())));
        }

        @Test
        void userNames() {
            assertEquals(List.of("amy", "bob", "tom"), StreamExercises.userNames(orders));
        }

        @Test
        void countByStatus() {
            assertEquals(Map.of(Status.COMPLETED, 3L, Status.CANCELLED, 1L, Status.PENDING_PAYMENT, 1L),
                    StreamExercises.countByStatus(orders));
        }

        @Test
        void topDishes() {
            assertEquals(List.of("米饭", "宫保鸡丁"), StreamExercises.topDishes(orders, 2));
            assertEquals(List.of("米饭", "宫保鸡丁", "水煮鱼"), StreamExercises.topDishes(orders, 10));
        }

        @Test
        void optional() {
            assertEquals(3L, StreamExercises.maxAmountOrder(orders).orElseThrow().id());
            assertTrue(StreamExercises.maxAmountOrder(List.of()).isEmpty());
            assertEquals(5L, StreamExercises.firstOrderIdOf(orders, "tom"));
            assertEquals(-1L, StreamExercises.firstOrderIdOf(orders, "nobody"));
        }

        @Test
        void revenueByDate() {
            Map<LocalDate, BigDecimal> m = StreamExercises.revenueByDate(orders);
            assertEquals(List.of(LocalDate.of(2024, 5, 1), LocalDate.of(2024, 5, 2)), new ArrayList<>(m.keySet()));
            assertEquals(0, bd("88").compareTo(m.get(LocalDate.of(2024, 5, 1))));
            assertEquals(0, bd("90.5").compareTo(m.get(LocalDate.of(2024, 5, 2))));
        }

        @Test
        void joinIds() {
            assertEquals("1,2,3,4,5", StreamExercises.joinIds(orders));
            assertEquals("", StreamExercises.joinIds(List.of()));
        }
    }

    @Nested
    @DisplayName("练习3：日期时间")
    class TimeTest {
        @Test
        void businessHours() {
            assertTrue(TimeExercises.isBusinessHours(LocalTime.of(9, 0)));
            assertTrue(TimeExercises.isBusinessHours(LocalTime.of(21, 59, 59)));
            assertFalse(TimeExercises.isBusinessHours(LocalTime.of(22, 0)));
            assertFalse(TimeExercises.isBusinessHours(LocalTime.of(8, 59)));
        }

        @Test
        void formatAndParse() {
            LocalDateTime t = LocalDateTime.of(2024, 5, 1, 9, 5, 3);
            assertEquals("2024-05-01 09:05:03", TimeExercises.format(t));
            assertEquals(t, TimeExercises.parse("2024-05-01 09:05:03"));
            assertEquals("202405011230050007", TimeExercises.orderNo(LocalDateTime.of(2024, 5, 1, 12, 30, 5), 7));
        }

        @Test
        void plusAndDuration() {
            LocalDateTime t = LocalDateTime.of(2024, 12, 31, 23, 50);
            assertEquals(LocalDateTime.of(2025, 1, 1, 0, 20), TimeExercises.estimatedDelivery(t, 30));
            assertFalse(TimeExercises.isPaymentTimeout(t, t.plusMinutes(15)));
            assertTrue(TimeExercises.isPaymentTimeout(t, t.plusMinutes(15).plusSeconds(1)));
        }

        @Test
        void dates() {
            assertEquals(29, TimeExercises.daysBetween(LocalDate.of(2024, 2, 1), LocalDate.of(2024, 3, 1)));
            assertEquals(-1, TimeExercises.daysBetween(LocalDate.of(2024, 2, 2), LocalDate.of(2024, 2, 1)));
            assertEquals(List.of(LocalDate.of(2024, 2, 28), LocalDate.of(2024, 2, 29), LocalDate.of(2024, 3, 1)),
                    TimeExercises.datesBetween(LocalDate.of(2024, 2, 28), LocalDate.of(2024, 3, 1)));
            assertEquals(1, TimeExercises.datesBetween(LocalDate.of(2024, 2, 28), LocalDate.of(2024, 2, 28)).size());
            assertThrows(IllegalArgumentException.class,
                    () -> TimeExercises.datesBetween(LocalDate.of(2024, 3, 1), LocalDate.of(2024, 2, 1)));
        }
    }

    @Component
    static class A {
        @Autowired
        B b;
    }

    @Component
    static class B {
        @Autowired
        A a;
    }

    static class NotComponent {
    }

    @Component
    static class AnotherMapper implements DishMapper {
        @Override
        public List<String> selectNames() {
            return List.of();
        }
    }

    @Nested
    @DisplayName("练习4：迷你 IoC 容器")
    class IocTest {
        @Test
        void inject() {
            MiniContainer c = new MiniContainer(DishController.class, DishService.class, DishMapperImpl.class);
            DishController controller = c.getBean(DishController.class);
            assertEquals("菜品: 宫保鸡丁、鱼香肉丝", controller.list());
            assertInstanceOf(DishMapperImpl.class, c.getBean(DishMapper.class), "按接口获取实现类");
        }

        @Test
        void singleton() {
            MiniContainer c = new MiniContainer(DishController.class, DishService.class, DishMapperImpl.class);
            DishController controller = c.getBean(DishController.class);
            assertSame(controller, c.getBean(DishController.class));
            assertSame(c.getBean(DishService.class), controller.getDishService());
            assertSame(c.getBean(DishMapper.class), c.getBean(DishService.class).getDishMapper());
        }

        @Test
        void errors() {
            assertThrows(IllegalArgumentException.class, () -> new MiniContainer(NotComponent.class));
            MiniContainer c = new MiniContainer(DishService.class);
            IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () -> c.getBean(DishController.class));
            assertEquals("找不到 Bean: " + DishController.class.getName(), e.getMessage());
            MiniContainer c2 = new MiniContainer(DishMapperImpl.class, AnotherMapper.class);
            assertThrows(IllegalStateException.class, () -> c2.getBean(DishMapper.class));
            assertNotNull(c2.getBean(AnotherMapper.class));
        }

        @Test
        void circular() {
            MiniContainer c = new MiniContainer(A.class, B.class);
            A a = c.getBean(A.class);
            assertNotNull(a.b);
            assertSame(a, a.b.a);
        }
    }
}
