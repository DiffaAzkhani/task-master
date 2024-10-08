package com.taskmaster.taskmaster.service;

import com.midtrans.Config;
import com.midtrans.httpclient.error.MidtransError;
import com.taskmaster.taskmaster.Util.InvoiceUtil;
import com.taskmaster.taskmaster.Util.SignatureUtil;
import com.taskmaster.taskmaster.Util.TaxUtil;
import com.taskmaster.taskmaster.Util.TimeUtil;
import com.taskmaster.taskmaster.configuration.midtrans.MidtransConfiguration;
import com.taskmaster.taskmaster.entity.Order;
import com.taskmaster.taskmaster.entity.OrderItem;
import com.taskmaster.taskmaster.entity.Study;
import com.taskmaster.taskmaster.entity.User;
import com.taskmaster.taskmaster.enums.OrderStatus;
import com.taskmaster.taskmaster.enums.StudyType;
import com.taskmaster.taskmaster.mapper.OrderMapper;
import com.taskmaster.taskmaster.model.request.AfterPaymentsRequest;
import com.taskmaster.taskmaster.model.request.ItemDetailsRequest;
import com.taskmaster.taskmaster.model.request.MidtransTransactionRequest;
import com.taskmaster.taskmaster.model.response.CheckoutMidtransResponse;
import com.taskmaster.taskmaster.model.response.GetOrdersResponse;
import com.taskmaster.taskmaster.repository.OrderRepository;
import com.taskmaster.taskmaster.repository.StudyRepository;
import com.taskmaster.taskmaster.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService{

    private final UserRepository userRepository;

    private final StudyRepository studyRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final MidtransConfiguration midtransConfiguration;

    private final ValidationService validationService;

    @Override
    @Transactional
    public CheckoutMidtransResponse completeCheckout(MidtransTransactionRequest request) throws MidtransError {
        User user = userRepository.findByEmail(request.getCustomer_details().getEmail())
            .orElseThrow(() -> {
                log.info("User with email:{}, not found!", request.getCustomer_details().getEmail());
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        validationService.validateUser(user.getUsername());

        Order order = Order.builder()
            .id(InvoiceUtil.invoiceGenerator())
            .status(OrderStatus.PROCESSING)
            .createdAt(TimeUtil.getFormattedLocalDateTimeNow())
            .user(user)
            .build();

        List<OrderItem> orderItems = createOrderItems(request, user, order);

        order.setOrderItems(new HashSet<>(orderItems));

        int totalTransfer = calculateTotalTransfer(orderItems);
        order.setTotalTransfer(totalTransfer);

        orderRepository.save(order);
        log.info("Order saved with ID: {}", order.getId());

        CheckoutMidtransResponse createdToken = midtransConfiguration.createTransactionToken(order, request);
        return orderMapper.toMidtransTransactionResponse(createdToken);
    }

    private List<OrderItem> createOrderItems(MidtransTransactionRequest request, User user, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (ItemDetailsRequest itemDetailsRequest : request.getItem_details()) {
            Study study = studyRepository.findById(itemDetailsRequest.getId())
                .orElseThrow(() -> {
                    log.info("Study with code:{}, not found!", itemDetailsRequest.getId());
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study code not found!");
                });

            if (userRepository.existsByUsernameAndStudies(user.getUsername(), study)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already ordered this study!");
            }

            OrderItem orderItem = OrderItem.builder()
                .quantity(itemDetailsRequest.getQuantity())
                .price(study.getPrice())
                .study(study)
                .build();

            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    private int calculateTotalTransfer(List<OrderItem> orderItems) {
        int totalTransfer = 0;

        for (OrderItem item : orderItems) {
            int ppn = TaxUtil.countPPN(item.getStudy());
            int totalPrice = item.getPrice() + ppn;
            totalTransfer += totalPrice * item.getQuantity();
        }

        return totalTransfer;
    }

    @Override
    @Transactional
    public void cancelOrder(String orderId) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
               log.info("User with username:{}, not found!", currentUser);
               return new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found!");
            });

        Order userOrder = orderRepository.findByUserAndId(user, orderId)
            .orElseThrow(() -> {
                log.info("Order with id:{} in user with username:{}, not found!", orderId, currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Order in user not found!");
            });

        if (!Objects.isNull(userOrder.getCompletedAt())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already finish this order payment!");
        }

        if (!userOrder.getStatus().equals(OrderStatus.PROCESSING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order already cancelled!");
        }

        userOrder.setStatus(OrderStatus.CANCELED);

        orderRepository.save(userOrder);
        log.info("Success to cancel order!");
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetOrdersResponse> getAllUserOrders(int page, int size) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
               log.info("User with username:{}, not found!", currentUser);
               return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });


        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findByUser(user, pageRequest);

        List<GetOrdersResponse> getOrdersRespons = orderPage.getContent().stream()
            .map(orderMapper::toGetAllUserOrdersResponse)
            .collect(Collectors.toList());

        log.info("Success to get all user orders!");

        return new PageImpl<>(getOrdersRespons, pageRequest, orderPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetOrdersResponse> getUserOrdersAdmin(Long userId, int page, int size) {
        log.info("User roles: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        User user = userRepository.findById(userId)
            .orElseThrow(() -> {
                log.info("User with id:{}, not found!", userId);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });


        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findByUser(user, pageRequest);

        List<GetOrdersResponse> getOrdersRespons = orderPage.getContent().stream()
            .map(orderMapper::toGetAllUserOrdersResponse)
            .collect(Collectors.toList());

        log.info("Success to get all user orders!");

        return new PageImpl<>(getOrdersRespons, pageRequest, orderPage.getTotalElements());
    }

    @Override
    @Transactional
    public void handleAfterPayments(AfterPaymentsRequest request) {
        String midtransServerKey = Config.getGlobalConfig().getServerKey();
        String signatureKey = SignatureUtil.generateSHA512Signature(request.getOrder_id(), request.getStatus_code(), request.getGross_amount(), midtransServerKey);

        if (!signatureKey.equals(request.getSignature_key())) {
            log.warn("Invalid signature key for orderId: {}", request.getOrder_id());
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid signature key!");
        }

        if (!request.getTransaction_status().equals("CAPTURE")) {
            log.warn("Transaction not captured for orderId: {}", request.getOrder_id());
            throw  new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED,"Transaction not captured!");
        }

        Order order = orderRepository.findById(request.getOrder_id())
            .orElseThrow(() -> {
                log.info("Order for orderId:{}, not found!", request.getOrder_id());
                return new ResponseStatusException(HttpStatus.NOT_FOUND,"Orderid not found!");
            });

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletedAt(TimeUtil.getFormattedLocalDateTimeNow());
        log.info("Success to update order after callback!");
    }

    @Override
    @Transactional
    public void enrollFreeStudies(String studyCode) {
        String currentUser = validationService.getCurrentUser();
        validationService.validateUser(currentUser);

        User user = userRepository.findByUsername(currentUser)
            .orElseThrow(() -> {
                log.info("User with username:{}, not found!", currentUser);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
            });

        Study study = studyRepository.findByCode(studyCode)
            .orElseThrow(() -> {
                log.info("Study with studyCode:{}, not found!", studyCode);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Study not found!");
            });

        if (userRepository.existsByUsernameAndStudies(user.getUsername(), study)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already enroll this study!");
        }

        if (!study.getType().equals(StudyType.FREE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Study type must be Free!");
        }

        user.getStudies().add(study);

        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GetOrdersResponse> getOrdersForAdmin(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepository.findAll(pageRequest);

        List<GetOrdersResponse> ordersResponseList = orderPage.stream()
            .map(orderMapper::toGetOrdersResponse)
            .collect(Collectors.toList());

        return new PageImpl<>(ordersResponseList, pageRequest, orderPage.getTotalElements());
    }

    @Override
    @Transactional
    public void deleteOrderById(String orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found!"));

        orderRepository.delete(order);
    }

}
