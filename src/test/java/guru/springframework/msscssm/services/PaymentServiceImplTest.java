package guru.springframework.msscssm.services;

import guru.springframework.msscssm.domain.Payment;
import guru.springframework.msscssm.domain.PaymentRepository;
import guru.springframework.msscssm.domain.PaymentState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PaymentServiceImplTest {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    private Payment payment;

    @BeforeEach
    void setup() {
        payment = Payment.builder().amount(new BigDecimal("12.99")).build();
    }

    @Transactional
    @Test
    void preAuthPayment() {
        Payment savedPayment = paymentService.newPayment(payment);
        assertEquals(savedPayment.getState(), PaymentState.NEW);

        System.out.println("---");
        paymentService.preAuthPayment(savedPayment.getId());
        System.out.println("---");

        Payment preAuthPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(preAuthPayment.getState());
    }

    @Transactional
    @Test
    void authPayment() {
        payment.setState(PaymentState.PRE_AUTH);
        Payment savedPayment = paymentRepository.save(payment);

        paymentService.authorizePayment(savedPayment.getId());

        Payment authPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println("Should be AUTH or AUTH_ERROR");
        System.out.println(authPayment.getState());
    }

    @Transactional
    @Test
    void viaPaymentProcess() {
        Payment savedPayment = paymentService.newPayment(payment);
        assertEquals(savedPayment.getState(), PaymentState.NEW);

        paymentService.preAuthPayment(savedPayment.getId());

        Payment preAuthPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println("Should be PRE_AUTH or PRE_AUTH_ERROR");
        System.out.println(preAuthPayment.getState());
        assertEquals(preAuthPayment.getState(), PaymentState.PRE_AUTH);

        paymentService.authorizePayment(savedPayment.getId());

        Payment authPayment = paymentRepository.getOne(savedPayment.getId());
        System.out.println("Should be AUTH or AUTH_ERROR");
        System.out.println(authPayment.getState());
    }
}