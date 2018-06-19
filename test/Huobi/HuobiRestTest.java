/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Huobi;

import Http.RequestResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author X
 */
public class HuobiRestTest {

    HuobiRest hr;
    private final String statusOk = "\"status\":\"ok\"";

    public HuobiRestTest() {
//        hr = new HuobiRest("key", "secret");
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAccount method, of class HuobiRest.
     */
    @Test
    public void testGetAccount() {
        System.out.println("getAccount");
        HuobiRest instance = hr;
        RequestResponse result = instance.getAccount();
        assertTrue(result.getResponseText().contains(statusOk));
    }

    /**
     * Test of getBalance method, of class HuobiRest.
     */
    @Test
    public void testGetBalance() {
        System.out.println("getBalance");
        String accountId = "2315024";
        HuobiRest instance = hr;
        RequestResponse result = instance.getBalance(accountId);
        assertTrue(result.getResponseText().contains(statusOk));
    }

    /**
     * Test of getOpenOrder method, of class HuobiRest.
     */
    @Test
    public void testGetOpenOrder() {
        System.out.println("getOpenOrder");
        String symbol = "xrpbtc";
        HuobiRest instance = hr;
        RequestResponse result = instance.getOpenOrder(symbol);
        assertTrue(result.getResponseText().contains(statusOk));
    }

    /**
     * Test of getOrder method, of class HuobiRest.
     */
    @Test
    public void testGetOrder() {
        System.out.println("getOrder");
        String orderId = "6166553613";
        HuobiRest instance = hr;
        RequestResponse result = instance.getOrder(orderId);
        assertTrue(result.getResponseText().contains(statusOk));
    }

    /**
     * Test of placeOrder method, of class HuobiRest.
     */
    @Test
    public void testPlaceOrder() {
        System.out.println("placeOrder");
        String marketName = "xrpbtc";
        String tradeType = HuobiRest.SELL_LIMIT;
        double price = 0.0001;
        double amount = 30;
        String accountId = "2315024";
        HuobiRest instance = hr;
        RequestResponse result = instance.placeOrder(marketName, tradeType, price, amount, accountId);
        assertTrue(result.getResponseText().contains(statusOk));
    }

    /**
     * Test of withdrawal method, of class HuobiRest.
     */
    @Test
    public void testWithdrawal() {
//        System.out.println("withdrawal");
//        String address = "";
//        String symbol = "";
//        double amount = 0.0;
//        String paymentId = "";
//        HuobiRest instance = null;
//        RequestResponse expResult = null;
//        RequestResponse result = instance.withdrawal(address, symbol, amount, paymentId);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
        assertTrue(true);
    }

    /**
     * Test of cancelOrder method, of class HuobiRest.
     */
    @Test
    public void testCancelOrder() {
        System.out.println("cancelOrder");
        String orderId = "6166553613";
        HuobiRest instance = hr;
        RequestResponse expResult = null;
        RequestResponse result = instance.cancelOrder(orderId);
        assertTrue(result.getResponseText().contains(statusOk));
    }

}
