package com.rootlab.jpashop.service;

import com.rootlab.jpashop.domain.Address;
import com.rootlab.jpashop.domain.Member;
import com.rootlab.jpashop.domain.Order;
import com.rootlab.jpashop.domain.item.Book;
import com.rootlab.jpashop.domain.item.Item;
import com.rootlab.jpashop.domain.status.OrderStatus;
import com.rootlab.jpashop.exception.NotEnoughStockException;
import com.rootlab.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        Order order = orderRepository.findOne(orderId);
        // then
        assertAll(
                () -> assertThat(order.getStatus()).as("상품 주문시 상태는 ORDER")
                        .isEqualTo(OrderStatus.ORDER),

                () -> assertThat(order.getOrderItems().size()).as("주문한 상품 종류 수가 정확해야 한다.")
                        .isEqualTo(1),

                () -> assertThat(order.getTotalPrice()).as("주문 가격은 가격 * 수량이다.")
                        .isEqualTo(10000 * 2),

                () -> assertThat(item.getStockQuantity()).as("주문 수량만큼 재고가 줄어야 한다.")
                        .isEqualTo(8)
        );
    }

    @Test
    public void 상품주문_재고수량초과() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        // when && then
        assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .as("재고 수량 부족 예외가 발생해야 한다.")
                .isInstanceOf(NotEnoughStockException.class)
                .hasMessage("need more stock");
    }

    @Test
    public void 주문취소() {
        // given
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        orderService.cancelOrder(orderId);
        Order order = orderRepository.findOne(orderId);
        // then
        assertAll(
                () -> assertThat(order.getStatus()).as("주문 취소시 상태는 CANCEL 이다.")
                        .isEqualTo(OrderStatus.CANCEL),

                () -> assertThat(item.getStockQuantity()).as("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.")
                        .isEqualTo(10)
        );
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        em.persist(book);
        return book;
    }
}