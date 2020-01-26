package guru.springframework.msscssm.config;

import guru.springframework.msscssm.domain.PaymentEvent;
import guru.springframework.msscssm.domain.PaymentState;
import guru.springframework.msscssm.services.PaymentServiceImpl;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class StateMachineActionsConfig {

    public Action<PaymentState, PaymentEvent> preAuthAction() {
        return context -> {
            System.out.println("PreAuth was called!!!");
            Message<PaymentEvent> msg;
            Object messageHeader = context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER);

            if (new Random().nextInt(10) < 8) {
                System.out.println("Approved");

                msg = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_APPROVED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, messageHeader)
                        .build();
            } else {
                System.out.println("Declined!!!");

                msg = MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, messageHeader)
                        .build();
            }

            context.getStateMachine().sendEvent(msg);
        };
    }


    public Action<PaymentState, PaymentEvent> authAction() {
        return context -> {
            System.out.println("Auth was called!!!");
            Message<PaymentEvent> msg;
            Object messageHeader = context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER);

            if (new Random().nextInt(10) < 8) {
                System.out.println("Approved!!!");

                msg = MessageBuilder.withPayload(PaymentEvent.AUTH_APPROVED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, messageHeader)
                        .build();
            } else {
                System.out.println("Declined!!!");

                msg = MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED)
                        .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, messageHeader)
                        .build();
            }

            context.getStateMachine().sendEvent(msg);
        };
    }

    public Action<PaymentState, PaymentEvent> preAuthApprovedAction() {
        return context -> System.out.println("PreAuthApprovedAction has been called!");
    }


    public Action<PaymentState, PaymentEvent> preAuthDeclinedAction() {
        return context -> System.out.println("PreAuthDeclinedAction has been called!");
    }

    public Action<PaymentState, PaymentEvent> authApprovedAction() {
        return context -> System.out.println("AuthApprovedAction has been called!");
    }

    public Action<PaymentState, PaymentEvent> authDeclinedAction() {
        return context -> System.out.println("AuthDeclinedAction has been called!");
    }
}
