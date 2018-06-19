/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Huobi;

import Http.Request;
import Http.RequestException;
import Http.RequestResponse;
import com.google.gson.JsonObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

/**
 *
 * @author X
 */
public class HuobiRest {

    private static final String API_URL = "api.huobipro.com";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final String apiKey;
    private final String secret;

    private static final String GET = "GET";
    private static final String POST = "POST";

    public static final String BUY_LIMIT = "buy-limit";
    public static final String SELL_LIMIT = "sell-limit";
    public static final String BUY_MARKET = "buy-market";
    public static final String SELL_MARKET = "sell-market";
    public static final String BUY_IOC = "buy-ioc";
    public static final String SELL_IOC = "sell-ioc";

    public HuobiRest(String apiKey, String secret) {
        this.apiKey = apiKey;
        this.secret = secret;

        //  set timezone to UTC/GMT | required
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Map<String, String> getBody() {
        Map<String, String> qs = new HashMap<>();
        qs.put("AccessKeyId", apiKey);
        qs.put("SignatureMethod", "HmacSHA256");
        qs.put("SignatureVersion", "2");
        qs.put("Timestamp", dateFormat.format(new Date()));
        return qs;
    }

    private String signSHA(String method, String path, Map<String, String> data) {
        try {
            TreeMap<String, String> body = new TreeMap<>(data);
            String query = "";
            query = body.entrySet().stream().map((entry) -> "&" + urlEncodeUTF8(entry.getKey()) + "=" + urlEncodeUTF8(entry.getValue())).reduce(query, String::concat);

            query = query.substring(1);
            String meta = method + "\n"
                    + API_URL + "\n"
                    + path + "\n"
                    + query;
            byte[] hashWords = Utils.HMAC.generateHMAC(meta, secret, Utils.HMAC.HMAC_SHA256);
            String hash = Base64.getEncoder().encodeToString(hashWords);
            return query + "&Signature=" + urlEncodeUTF8(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException ex) {
        }
        return "";
    }

    private String urlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    private RequestResponse callApi(String method, String path, String query, String body) {
        String url = "https://" + API_URL + path + "?" + query;
        Http.Request request = new Request(url);
        try {
            if (method.equals(GET)) {
                return request.get();
            } else {
                return request.post(body);
            }
        } catch (RequestException ex) {
        }
        return new RequestResponse(-1, "unknown error");
    }

    private static String fmt(double d) {
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setMaximumFractionDigits(340); //340 = DecimalFormat.DOUBLE_FRACTION_DIGITS
        return df.format(d);
    }

    public RequestResponse getAccount() {
        String path = "/v1/account/accounts";
        Map<String, String> body = getBody();
        String query = signSHA(GET, path, body);
        return callApi(GET, path, query, null);
    }

    public RequestResponse getBalance(String accountId) {
        String path = "/v1/account/accounts/" + accountId + "/balance";
        Map<String, String> body = getBody();
        String query = signSHA(GET, path, body);
        return callApi(GET, path, query, null);
    }

    public RequestResponse getOpenOrder(String symbol) {
        String path = "/v1/order/orders";
        Map<String, String> body = getBody();
        body.put("symbol", symbol);
        body.put("states", "submitted,partial-filled");
        String query = signSHA(GET, path, body);
        return callApi(GET, path, query, null);
    }

    public RequestResponse getOrder(String orderId) {
        String path = "/v1/order/orders/" + orderId;
        Map<String, String> body = getBody();
        String query = signSHA(GET, path, body);
        return callApi(GET, path, query, null);
    }

    public RequestResponse placeOrder(String marketName, String tradeType, double price, double amount, String accountId) {
        String path = "/v1/order/orders/place";
        Map<String, String> body = getBody();
        String query = signSHA(POST, path, body);

        JsonObject data = new JsonObject();
        data.addProperty("account-id", accountId);
        data.addProperty("type", tradeType);
        data.addProperty("amount", fmt(amount));
        data.addProperty("price", fmt(price));
        data.addProperty("symbol", marketName);

        return callApi(POST, path, query, data.toString());
    }

    public RequestResponse cancelOrder(String orderId) {
        String path = "/v1/order/orders/" + orderId + "/submitcancel";
        Map<String, String> body = getBody();
        String query = signSHA(POST, path, body);
        return callApi(POST, path, query, "{}");
    }

    public RequestResponse withdrawal(String address, String symbol, double amount, String paymentId) {
        String path = "/v1/dw/withdraw/api/create";
        Map<String, String> body = getBody();
        String query = signSHA(POST, path, body);

        symbol = symbol.toLowerCase();
        JsonObject data = new JsonObject();
        data.addProperty("address", address);
        data.addProperty("amount", fmt(amount));
        data.addProperty("currency", symbol);

        if (symbol.equals("xrp")) {
            if (paymentId != null && !paymentId.isEmpty()) {
                data.addProperty("addr-tag", paymentId);
            }
        }

        return callApi(POST, path, query, data.toString());
    }
}
