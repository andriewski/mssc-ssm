package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.services.PaymentServiceImpl;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Component;

@Component
public class StateMachineGuardsConfig {

    public Guard<PaymentState, PaymentEvent> paymentIdGuard() {
        return context -> {
            boolean shouldPassGuard = context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null;

            if (!shouldPassGuard) {
                System.out.println("Guard protection activated! :p");
            }

            return shouldPassGuard;
        };
    }
}
