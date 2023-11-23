package hcmute.it.furnitureshop.Service;

import hcmute.it.furnitureshop.DTO.OrderRequestDTO;
import hcmute.it.furnitureshop.Entity.Order;
import hcmute.it.furnitureshop.Entity.User;

import java.util.Optional;

public interface OrderService {
    public <S extends Order> void save(User user, OrderRequestDTO orderRequestDTO, Integer productId);
    public String CancelOrder(Integer orderId);
    String UpdateOrder(Integer orderId);
    public String RestoreOrder(Integer orderId);
    public Iterable<Order> findByUser(User user);
    public Optional<Order> findById(Integer orderId);
    public Iterable<Order> findOrderByUserAndState(String username,String state);
}
