package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void 상품주문() {

        //Given
        Member member = createMember();
        Book orderItem = createBook("JPA Book", 3000, 10);
        int orderCount = 2;

        //When
        Long orderId = orderService.order(member.getId(), orderItem.getId(), orderCount);

        //Then
        Order getOrder = orderRepository.findOne(orderId);

        Assert.assertEquals("상품주문시 상태는 ORDER이다.", OrderStatus.ORDER, getOrder.getStatus());
        Assert.assertEquals("상품Item 수가 정확해야한다.", 1, getOrder.getOrderItems().size());
        Assert.assertEquals("주문가격은 상품수*가격이다.", orderCount*orderItem.getPrice(), getOrder.getTotalPrice());
        Assert.assertEquals("주문 수량만큼 재고가 줄어야 한다.", 8, orderItem.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() {
        //Given
        Member member = createMember();
        Book orderItem = createBook("JPA Book", 3000, 10);
        int orderCount = 12;//재고보다 많은 수량

        //When
        Long orderId = orderService.order(member.getId(), orderItem.getId(), orderCount);

        //Then
        fail("재고 수량 부족 예외가 발생해야 한다.");
    }

    @Test
    public void 주문취소() {
        //Given
        Member member = createMember();
        Book orderItem = createBook("JPA Book", 3000, 10);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), orderItem.getId(), orderCount);

        //When
        orderService.cancelOrder(orderId);

        //Then
        Order getOrder = orderRepository.findOne(orderId);
        Assert.assertEquals("주문 상태는 취소상태이어야 한다.", OrderStatus.CANCEL, getOrder.getStatus());
        Assert.assertEquals("주문 취소 상품은 재고가 증거되어야 한다.", 10, orderItem.getStockQuantity());

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