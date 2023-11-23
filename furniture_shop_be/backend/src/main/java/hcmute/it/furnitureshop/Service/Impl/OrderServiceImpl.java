package hcmute.it.furnitureshop.Service.Impl;

import hcmute.it.furnitureshop.Common.RankEnum;
import hcmute.it.furnitureshop.DTO.OrderDTO;
import hcmute.it.furnitureshop.DTO.OrderRequestDTO;
import hcmute.it.furnitureshop.Entity.Notification;
import hcmute.it.furnitureshop.Entity.Order;
import hcmute.it.furnitureshop.Entity.Product;
import hcmute.it.furnitureshop.Entity.User;
import hcmute.it.furnitureshop.Repository.NotificationRepository;
import hcmute.it.furnitureshop.Repository.OrderRepository;
import hcmute.it.furnitureshop.Repository.ProductRepository;
import hcmute.it.furnitureshop.Repository.UserRepository;
import hcmute.it.furnitureshop.Service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public <S extends Order> void save(User user, OrderRequestDTO orderRequestDTO,Integer productId) {
        Order order=new Order();
        Optional<Product> product=productRepository.findById(productId);
        order.setUser(user);
        order.setProduct(product.get());
        order.setState("processing");
        order.setDate(new Date());
        order.setCount(orderRequestDTO.getCount());
        order.setPaid(orderRequestDTO.getPaid());
        order.setNowDelivery(orderRequestDTO.getNowDelivery());
        ///
        Notification notification=new Notification();
        notification.setState(false);
        notification.setDescription("Đặt hàng thành công");
        notification.setUser(user);
        notification.setDate(new Date());
        List<Notification> notifications=new ArrayList<>();
        notifications.add(notification);
        //// trừ số lượng khi đặt hàng
        product.get().setQuantity(product.get().getQuantity()-orderRequestDTO.getCount());
        productRepository.save(product.get());
        //
        order.setNotification(notifications);
        orderRepository.save(order);
        notification.setOrder(order);
        notificationRepository.save(notification);
    }

    @Override
    public String CancelOrder(Integer orderId) {
        Optional<Order> order=orderRepository.findById(orderId);
        if(order.isPresent()){
            if(order.get().getState().equals("processing")) {
                Product product = order.get().getProduct();
                if(product != null){
                    order.get().setState("canceled");
                    Notification notification = new Notification();
                    notification.setState(false);
                    notification.setDescription("Huỷ đơn hàng thành công");
                    notification.setUser(order.get().getUser());
                    notification.setDate(new Date());
                    notification.setOrder(order.get());
                    notificationRepository.save(notification);
                    orderRepository.save(order.get());
                    product.setQuantity(product.getQuantity() + order.get().getCount());
                    productRepository.save(product);
                    return "canceled success";
                }else {
                    return "canceled fail";
                }
            }else {
                return "this order can't canceled";
            }
        }else{
            return "canceled fail";
        }
    }

    @Override
    public String UpdateOrder(Integer orderId) {
        Optional<Order> order=orderRepository.findById(orderId);

        String message = null;
        if (order.isPresent() && !order.get().getState().isEmpty())
        {
            switch (order.get().getState()) {
                case "processing" -> {
                    order.get().setState("processed");
                    message = "Đơn hàng đã được xác nhận";
                }
                case "processed" -> {
                    order.get().setState("delivering");
                    message = "Đơn hàng đang được vận chuyển";
                }
                case "delivering" -> {
                    order.get().setState("delivered");
                    message = "Đơn hàng đã được giao";
                    order.get().setPaid(true);
                }
            }
            if (order.get().getPaid())
                pointCalculate(order.get().getUser().getUserId(), orderId);
            /// Tạo thông báo
            Notification notification=new Notification();
            notification.setState(false);
            notification.setDescription(message);
            notification.setUser(order.get().getUser());
            notification.setDate(new Date());
            notification.setOrder(order.get());
            notificationRepository.save(notification);
            return "Chuyển trạng thái thành công";
        }
        else return "Chuyển trạng thái thất bại";
    }

    @Override
    public String RestoreOrder(Integer orderId) {
        Optional<Order> order=orderRepository.findById(orderId);
        if(order.isPresent()){
            if(order.get().getState().equals("canceled")){
                Product product= order.get().getProduct();
                if(product.getQuantity()>0){
                    order.get().setState("processing");
                    order.get().setDate(new Date());
                    orderRepository.save(order.get());

                    Notification notification=new Notification();
                    notification.setState(false);
                    notification.setDescription("Đặt lại đơn hàng thành công");
                    notification.setUser(order.get().getUser());
                    notification.setDate(new Date());
                    notification.setOrder(order.get());

                    notificationRepository.save(notification);

                    product.setQuantity(product.getQuantity()-order.get().getCount());
                    productRepository.save(product);
                    return "restore success";
                }else{
                    return "not enough product quantity";
                }
            }else{
                return "this order can't restore";
            }
        }else{
            return "restore fail";
        }
    }

    @Override
    public Iterable<Order> findByUser(User user) {
        return orderRepository.findOrdersByUserOrderByDateDesc(user);
    }

    @Override
    public Optional<Order> findById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Iterable<Order> findOrderByUserAndState(String username,String state) {
        Optional<User> user=userRepository.findByUsername(username);
        if(user.isPresent()) {
            Iterable<Order> orders = orderRepository.findOrdersByUserOrderByDateDesc(user.get());
            ArrayList<Order> ordersReturn = new ArrayList<>();
            orders.forEach(order -> {
                if (order.getState().equals(state)) {
                    ordersReturn.add(order);
                }
            });
            return ordersReturn;
        }
        else{
            return new ArrayList<>();
        }
    }

    private void pointCalculate(Integer userId, Integer orderId)
    {
        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent())
        {
            Optional<Order> order = orderRepository.findById(orderId);
            int point = (int)(user.get().getPoint() + order.get().getCount()*(order.get().getProduct().getPrice())/100000);
            user.get().setPoint(point);
            if (point >0 && point <= 100)
                user.get().setRankUser(RankEnum.BRONZE);
            else if (point<=200)
                user.get().setRankUser(RankEnum.SILVER);
            else if (point<=300)
                user.get().setRankUser(RankEnum.GOLD);
            else if (point<=400)
                user.get().setRankUser(RankEnum.PLATINUM);
            else if (point<=500)
                user.get().setRankUser(RankEnum.DIAMOND);
        }
    }

}
