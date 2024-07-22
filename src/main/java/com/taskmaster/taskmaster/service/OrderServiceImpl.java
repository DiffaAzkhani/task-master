package com.taskmaster.taskmaster.service;

import com.taskmaster.taskmaster.Util.InvoiceUtil;
import com.taskmaster.taskmaster.Util.TaxUtil;
import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.OrderStatus;
import com.taskmaster.taskmaster.mapper.OrderMapper;
import com.taskmaster.taskmaster.model.request.CreateOrderRequest;
import com.taskmaster.taskmaster.model.response.OrderResponse;
import com.taskmaster.taskmaster.repository.OrderRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;

    private final StudyRepository studyRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", request.getUsername());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "username not found!");
            });

        Study study = studyRepository.findByCode(request.getStudyCode())
            .orElseThrow(() -> {
                log.info("Study with code:{}, not found!", request.getStudyCode());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study code not found!");
            });

        if (orderRepository.existsByUserAndStudy(user, study)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already ordered this study!");
        }

        Double ppn = TaxUtil.countPPN(study);
        Double totalTransfer = study.getPrice() + ppn;

        Order order = Order.builder()
            .id(InvoiceUtil.invoiceGenerator())
            .status(OrderStatus.PROCESSING)
            .createdAt(TimeUtil.getFormattedLocalDateTimeNow())
            .paymentMethod(request.getPaymentMethod())
            .paymentDue(TimeUtil.generatePaymentDue(request.getPaymentMethod()))
            .user(user)
            .study(study)
            .totalTransfer(totalTransfer)
            .build();

        orderRepository.save(order);
        log.info("Success to create new order!");

        return orderMapper.toOrderResponse(order);
    }

}
