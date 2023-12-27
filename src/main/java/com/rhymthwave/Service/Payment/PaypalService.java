package com.rhymthwave.Service.Payment;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.rhymthwave.entity.payment.CompletedOrder;
import com.rhymthwave.entity.payment.Payment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PaypalService {
	
	@Autowired
    PayPalHttpClient payPalHttpClient;
	
	public Payment createPayment(Float fee,Integer subscription, String email ,HttpServletRequest req, String pathReturn, String pathCancel,String packages) {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        
        AmountWithBreakdown amountBreakdown = new AmountWithBreakdown().currencyCode("USD").value(fee.toString()); //this is cost
        
        PurchaseUnitRequest purchaseUnitRequest = new PurchaseUnitRequest().amountWithBreakdown(amountBreakdown);
        
        orderRequest.purchaseUnits(List.of(purchaseUnitRequest));
        
        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(applicationUrl(req,pathReturn)) //return url front end. If it is success, you will be redirected to the captureUrl with two request params. “token” and “PayerID”.
                .cancelUrl(applicationUrl(req,pathCancel)); //return url. If authorization is failed, you will be redirected to the cancelUrl
        orderRequest.applicationContext(applicationContext);
        OrdersCreateRequest ordersCreateRequest = new OrdersCreateRequest().requestBody(orderRequest);

        try {
            HttpResponse<Order> orderHttpResponse = payPalHttpClient.execute(ordersCreateRequest);
            Order order = orderHttpResponse.result();

            String redirectUrl = order.links().stream()
                    .filter(link -> "approve".equals(link.rel()))
                    .findFirst()
                    .orElseThrow(NoSuchElementException::new)
                    .href();

            return new Payment("success",order.id(), redirectUrl);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new Payment("fail","error","");
        }
    }
	
	public CompletedOrder completePayment(String token) {
        OrdersCaptureRequest ordersCaptureRequest = new OrdersCaptureRequest(token);
        try {
            HttpResponse<Order> httpResponse = payPalHttpClient.execute(ordersCaptureRequest);
            if (httpResponse.result().status() != null) {
                return new CompletedOrder("success", token);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new CompletedOrder("error");
    }
	
	private String applicationUrl(HttpServletRequest req, String path) {
		return "http://" + req.getServerName() + ":" + req.getServerPort() + path;
	}
}
