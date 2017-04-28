package api_call;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.ws.Response;

//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;

import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.aws.apa.jaxws.Item;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import am.ik.aws.apa.jaxws.Items;



public class PAAPI{
    String keyword = "";
    List<Items> items = null;
    
    //Add constructor to take keyword
    public PAAPI(){
    	
    }
    
    public void SetKeyword(String value){
    	keyword = value;
    }
    
    public List<Items> getItemList(){
    	return items;
    }

    //@Test
    public void testItemSearch() throws IllegalArgumentException {
    	AwsApaRequester requester = new AwsApaRequesterImpl();
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
        //assertNotNull(response);
        //assertNotNull(response.getItems());
        //assertTrue(response.getItems().size() > 0);
    }
    
    public String itemSearch(String keyword) throws IllegalArgumentException {
    	AwsApaRequester requester = new AwsApaRequesterImpl();
        ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords("Everythings an Argument");
        request.getResponseGroup().add("OfferSummary");
        request.getResponseGroup().add("Offers");
        System.out.println(request);
        ItemSearchResponse response = requester.itemSearch(request);
        
        //Organizes From Lowest Price to Highest
        Items items = response.getItems().get(0);
        String FIRST_PRICE = "";
        String SECOND_PRICE = "";
        for(int i = 0; i < items.getItem().size(); i++){
        	if(items.getItem().get(i).getOfferSummary() != null){
	        	String LOWEST = items.getItem().get(i).getOfferSummary().getLowestUsedPrice() != null 
	    		? items.getItem().get(i).getOfferSummary().getLowestUsedPrice().getFormattedPrice() 
	    		: items.getItem().get(i).getOfferSummary().getLowestNewPrice().getFormattedPrice();
	        	items.getItem().get(i).setLowestPrice(LOWEST);
        	}
        }
        for(int i = 0; i < items.getItem().size(); i++){
        	for(int j = i + 1; j < items.getItem().size(); j++){		
				if (Float.parseFloat(items.getItem().get(i).getLowestPrice().replace("$", "")) > Float.parseFloat(items.getItem().get(j).getLowestPrice().replace("$", ""))) {
					Collections.swap(items.getItem(), i, j);
					//Item temp = items.getItem().get(j);
					//items.getItem().set(j, items.getItem().get(i));
					//items.getItem().set(i, temp);
				}
    		}
        }
        return items.getItem().get(0).getLowestPrice();
    }
    
    
}
