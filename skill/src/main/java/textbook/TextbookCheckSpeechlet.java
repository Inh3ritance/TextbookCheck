package textbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class TextbookCheckSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(TextbookCheckSpeechlet.class);

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getHelpResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        
        if("TextbookCheck".equals(intentName)){
          String bookslot = intent.getSlot("Book").getValue();
          return getTextbookLowestPrice(bookslot);
        } else if("AMAZON.NoIntent".equals(intentName) || "AMAZON.CancelIntent".equals(intentName) || "AMAZON.StopIntent".equals(intentName)){
          return getGoodbyeResponse();
        } else if("AMAZON.HelpIntent".equals(intentName)){
          return getHelpResponse();
        } else {
          throw new SpeechletException("Invalid Intent");
        }
    }
    
    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
    }
    
    private SpeechletResponse getGoodbyeResponse(){
      String speechText = "Goodbye";
      
      SimpleCard card = new SimpleCard();
      card.setTitle(speechText);
      card.setContent(speechText);
      
      PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
      speech.setText(speechText);
      
      return SpeechletResponse.newTellResponse(speech, card);
    }

    private SpeechletResponse getHelpResponse() {
        String speechText = "Say the name or ISBN of a textbook and I will tell you the cheapest price available on Amazon.";

        SimpleCard card = new SimpleCard();
        card.setTitle("Help");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
    
    private SpeechletResponse getTextbookLowestPrice(String textbook) {
     /*List<Items> items = null;
     try {
      items = api_call.itemSearch(textbook);
     } catch(Exception e){
       
     }
        String speechText = "book is priced at " + items.get(0).getItem().get(0).getOfferSummary().getLowestUsedPrice().getFormattedPrice(); */
      
        String speechText = textbook + " this is the book you want.";
      
        SimpleCard card = new SimpleCard();
        card.setTitle("TextbookCheck");
        card.setContent(speechText);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

}
