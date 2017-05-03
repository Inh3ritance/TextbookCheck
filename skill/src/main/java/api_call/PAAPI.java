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
//Amazon Search API
import am.ik.aws.apa.AwsApaRequester;
import am.ik.aws.apa.AwsApaRequesterImpl;
import am.ik.aws.apa.jaxws.Item;
import am.ik.aws.apa.jaxws.ItemSearchRequest;
import am.ik.aws.apa.jaxws.ItemSearchResponse;
import am.ik.aws.apa.jaxws.Items;
import am.ik.aws.apa.jaxws.Image;

//Ebay Search API
import com.ebay.services.client.ClientConfig;
import com.ebay.services.client.FindingServiceClientFactory;
import com.ebay.services.finding.FindItemsByKeywordsRequest;
import com.ebay.services.finding.FindItemsByKeywordsResponse;
import com.ebay.services.finding.FindingServicePortType;
import com.ebay.services.finding.PaginationInput;
import com.ebay.services.finding.SearchItem;
import com.ebay.services.finding.SortOrderType;



public class PAAPI{
    String keyword = "";
    String eBayKeyword = "";
    String amazonKeyword = "";
    //List of Amazon items
    List<Items> amazonItems = null;
    List<SearchItem> ebayItems = null;
    //Lowest Prices
    double lowestEbay;
    double lowestAmazon;
    
    //URL for each lowest item
    String amazonURL = "";
    String ebayURL = "";

    
    //Set up Configuration for Each Service
	AwsApaRequester requester = new AwsApaRequesterImpl();
	ClientConfig config = new ClientConfig();
	
	Image image = null;
    
    public PAAPI(){

    }
    
    public void setKeyword(String value){
    	keyword = value;
    }
    
    public String getEbayURL(){
    	return ebayURL;
    }
    
    public String getAmazonURL(){
    	return amazonURL;
    }
    
    public String getEbayTitle(){
    	return eBayKeyword;
    }
    
    public String getAmazonTitle(){
    	return amazonKeyword;
    }
    
    public String getAmazonPrice(){
    	String h = "" + lowestAmazon;
    	if(h.length() <= 3){
    		h += "0";
    	}
    	
    	return h;
    }
    
    public String getEbayPrice(){
    	String h = "" + lowestEbay;
    	if(h.length() <= 3){
    		h += "0";
    	}
    	
    	return h;
    }
    
    
    
    //Returns the Mazon Lowest Price image not eBays!
    public Image getAmazonImage(){
    	return image;
    }
    
    public boolean isISBN(){
	    try{  
	    	double d = Double.parseDouble(keyword);  
	    }  
	    catch(NumberFormatException nfe)  {  
	    	return false;  
	    }  
	    return true;
    }
    
    public String lowestPrice(){
    	String a = "";
    	String eBay = "";
    	String amazon = "";
    	try{
    		itemSearchAmazon();
    		itemSearchEbay();
    	}catch(Exception e){
    		System.out.println(e);
    	}
    	eBay = (lowestEbay*100)%10 == 0 ? "0 on eBay." : " on eBay.";
    	amazon = (lowestAmazon*100)%10 == 0 ? "0 on Amazon." : " on Amazon.";
    	System.out.println(lowestEbay);
    	System.out.println(lowestAmazon);
    	
    	
    	if(lowestAmazon != 0 && lowestEbay != 0){
    		if(lowestEbay <= lowestAmazon){
    			a = "I found " + eBayKeyword + " for $" + lowestEbay + eBay;  
    		}else{
    			a = "I found " + amazonKeyword + " for $" + lowestAmazon + amazon; 
    		}
    	}else if(lowestEbay != 0){
			a = "I found " + eBayKeyword + " for $" + lowestEbay + eBay; 
    	}else if(lowestAmazon != 0){
			a = "I found " + amazonKeyword + " for $" + lowestAmazon + amazon; 
    	}else{
    		a = "I could not find that book.";
    	}
    	
    	a += " Do you want to search for another book?";
    	
    	return a;
    }


    public void itemSearchAmazon() throws IllegalArgumentException {
    	lowestAmazon = 0;
    	
        ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords(keyword);
        request.getResponseGroup().add("OfferSummary");
        request.getResponseGroup().add("ItemAttributes");
        
        ItemSearchResponse response = requester.itemSearch(request);
        
        //Organizes From Lowest Price to Highest
        Items items = response.getItems().get(0);
        String FIRST_PRICE = "";
        String SECOND_PRICE = "";
        //Determines if new or low price is lowest
        //System.out.println(items.getItem().size());
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
				if (Double.parseDouble(items.getItem().get(i).getLowestPrice().replace("$", "")) > Double.parseDouble(items.getItem().get(j).getLowestPrice().replace("$", ""))) {
					Collections.swap(items.getItem(), i, j);
				}
    		}
        }
        
        for(int i = 0; i < items.getItem().size(); i++){
        	if(Double.parseDouble(items.getItem().get(i).getLowestPrice().replace("$", "")) > 2.00){
        		lowestAmazon = Double.parseDouble(items.getItem().get(i).getLowestPrice().replace("$", ""));
        		amazonURL = items.getItem().get(i).getDetailPageURL();
        		image = items.getItem().get(i).getMediumImage();
        		amazonKeyword = isISBN() ? items.getItem().get(i).getItemAttributes().getTitle() : keyword;
        		break;
        	}
        }
    }
    
    public void itemSearchEbay() throws Exception{
    	lowestEbay = 0;
    	// initialize service end-point configuration
    	
    	// endpoint address can be overwritten here, by default, production address is used,
    	// to enable sandbox endpoint, just uncomment the following line
    	//config.setEndPointAddress("http://svcs.sandbox.ebay.com/services/search/FindingService/v1");
    	config.setApplicationId("JorgeEst-AlexaTex-PRD-a090fc79c-39149f17");
    	config.setSoapMessageLoggingEnabled(false);
    	config.setHttpHeaderLoggingEnabled(false);
    	//create a service client
        FindingServicePortType serviceClient = FindingServiceClientFactory.getServiceClient(config);
        
        //create request object
        FindItemsByKeywordsRequest request = new FindItemsByKeywordsRequest();
        //set request parameters
        request.setKeywords(keyword);
        request.setSortOrder(SortOrderType.BEST_MATCH);
        PaginationInput pi = new PaginationInput();
        pi.setEntriesPerPage(10);
        
        request.setPaginationInput(pi);
        
        //call service
        FindItemsByKeywordsResponse result = serviceClient.findItemsByKeywords(request);
        
        //output result
        //System.out.println("Ack = "+result.getAck());
        //System.out.println("Find " + result.getSearchResult().getCount() + " items." );
        ebayItems = result.getSearchResult().getItem();
        if(ebayItems.get(0) != null 
        && ebayItems.get(0).getSellingStatus() != null 
        && ebayItems.get(0).getSellingStatus().getConvertedCurrentPrice() != null){
        	lowestEbay = ebayItems.get(0).getSellingStatus().getConvertedCurrentPrice().getValue();
        }
        for(SearchItem item : ebayItems) {
        	//System.out.println(item.getTitle() + " " + item.getSellingStatus().getConvertedCurrentPrice().getValue());
        	if(item.getSellingStatus() != null 
        	&& item.getSellingStatus().getConvertedCurrentPrice() != null
        	&& item.getPrimaryCategory().getCategoryName().toLowerCase().contains("book")
        	&& item.getSellingStatus().getConvertedCurrentPrice().getValue() < lowestEbay){
        		lowestEbay = item.getSellingStatus().getConvertedCurrentPrice().getValue();
        		ebayURL = item.getViewItemURL();
        		eBayKeyword = isISBN() ? item.getTitle() : keyword;
        	}
        }
    }
    
    
}
