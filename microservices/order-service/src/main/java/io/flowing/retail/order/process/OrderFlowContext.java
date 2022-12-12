package io.flowing.retail.order.process;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class OrderFlowContext {

    private String traceId;
    private String orderId;
    private String pickId;
    private String shipmentId;

    public static OrderFlowContext fromMap(Map<String, Object> values) {
        OrderFlowContext context = new OrderFlowContext();
        context.traceId = (String) values.get("traceId");
        context.orderId = (String) values.get("orderId");
        context.pickId = (String) values.get("pickId");
        context.shipmentId = (String) values.get("shipmentId");
        return context;
    }

    public Map<String, String> asMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("traceId", traceId);
        map.put("orderId", orderId);
        map.put("pickId", pickId);
        map.put("shipmentId", shipmentId);
        return map;
    }
}
