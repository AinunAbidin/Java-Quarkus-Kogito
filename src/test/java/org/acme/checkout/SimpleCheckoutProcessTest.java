package org.acme.checkout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.junit.jupiter.api.Test;
import org.kie.kogito.Model;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
class SimpleCheckoutProcessTest {

    @Inject
    @Named("simple_checkout")
    Process<? extends Model> simpleCheckout;

    @Test
    void should_complete_checkout_process() {
        CheckoutData data = new CheckoutData();
        data.setUserId("user-123");
        data.setCartId("cart-123");
        List<CheckoutData.Item> items = new ArrayList<>();
        items.add(new CheckoutData.Item("Item-1", 2, BigDecimal.valueOf(10_000)));
        items.add(new CheckoutData.Item("Item-2", 1, BigDecimal.valueOf(25_000)));
        data.setItems(items);

        Model model = simpleCheckout.createModel();
        model.fromMap(Map.of("data", data));

        ProcessInstance<? extends Model> instance = simpleCheckout.createInstance(model);
        instance.start();

        CheckoutData result = (CheckoutData) instance.variables().toMap().get("data");

        assertEquals(ProcessInstance.STATE_COMPLETED, instance.status());
        assertEquals(BigDecimal.valueOf(45_000), result.getTotal());
        assertNotNull(result.getOrderId());
        assertTrue(result.getOrderId().startsWith("ORD-"));
        assertEquals("CREATED", result.getStatus());
    }
}
