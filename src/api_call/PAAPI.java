package api_call;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.ws.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.aws.apa.jaxws.Item;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import am.ik.aws.apa.jaxws.Items;



public class PAAPI {
    protected AwsApaRequester requester = new AwsApaRequesterImpl();
    
    //Add constructor to take keyword
    public PAAPI(){
    	
    }
    
   // public PAAPI(String keyword){
    	
   // }

    @Test
    public void testItemSearch() throws Exception {
        ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords("Everythings an Argument");
        request.getResponseGroup().add("OfferSummary");
        request.getResponseGroup().add("Offers");
        System.out.println(request);
        ItemSearchResponse response = requester.itemSearch(request);
        for(int i = 0; i < 10; i++){
        	System.out.println(response.getItems().get(0).getItem().get(i).getOfferSummary().getLowestUsedPrice().getFormattedPrice());
        }
        assertNotNull(response);
        assertNotNull(response.getItems());
        assertTrue(response.getItems().size() > 0);
    }
    
    public List<Items> itemSearch(String keyword) throws Exception {
        ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords(keyword);
        request.getResponseGroup().add("OfferSummary");
        request.getResponseGroup().add("Offers");
        System.out.println(request);
        ItemSearchResponse response = requester.itemSearch(request);
        for(int i = 0; i < 10; i++){
        	System.out.println(response.getItems().get(0).getItem().get(i).getOfferSummary().getLowestUsedPrice().getFormattedPrice());
        }
        if(response.getItems().size() > 0){
        	return response.getItems();
        }
        return null;
    }
}
