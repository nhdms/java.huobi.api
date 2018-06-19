/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Http;

import java.io.Serializable;

/**
 *
 * @author great
 */
public class RequestResponse implements Serializable{
    private int responseCode;
    private String responseText;

    public RequestResponse() {
    }

    public RequestResponse(int responseCode, String responseText) {
        this.responseCode = responseCode;
        this.responseText = responseText;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    @Override
    public String toString() {
        return responseCode + " | " + responseText;
    }
    
}
