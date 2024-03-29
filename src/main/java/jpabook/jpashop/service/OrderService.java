package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class OrderService {

    private final MemberRepository memberRepository;
    private final ItemRepository   itemRepository;
    private final OrderRepository  orderRepository;

    /** 주문 **/
    @Transactional
    public Long order (Long memberId, Long itemId, int count){
        // 엔터티 조회
        Member member = memberRepository.findOne(memberId);
        Item   item   = itemRepository.findOne(itemId);

        //배송정보 조회
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.READY);
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);

        return  order.getId();
    }

    /** 주문취소 **/
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔터티 조회
        Order order = orderRepository.findOne(orderId);

        //취소
        order.cancel();
    }

    /** 주문 검색 */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);

    }


}
