package textbook;

import api_call.PAAPI;
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
    private PAAPI api_call = new PAAPI();
    
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
        return getLaunchResponse();
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
        } else if("AMAZON.StopIntent".equals(intentName) || "AMAZON.CancelIntent".equals(intentName) || "AMAZON.NoIntent".equals(intentName)){
          return getGoodbyeResponse();
        } else if("AMAZON.YesIntent".equals(intentName)){
          return getLaunchResponse();
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
    
    private SpeechletResponse getLaunchResponse() {
        String speechText = "Say the name or ISBN of a textbook and I will try to find the lowest price available online.";
        
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }
    
    private SpeechletResponse getHelpResponse() {
        String speechText = "To use this skill say text book check followed by the book name or ISBN to search for a book, say help to get guidance on how to use this skill, say cancel or stop to end the session. Would you like to find a book? Say yes or no.";
        
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt);
    }
    
    private SpeechletResponse getTextbookLowestPrice(String textbook) {
        api_call.setKeyword(textbook);
        String speechText = api_call.lowestPrice();
        
        String card = api_call.getCardInfo();
        SimpleCard a = new SimpleCard();
        a.setTitle("Book Info");
        a.setContent(card);

        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);
        if(!card.isEmpty()){
         return SpeechletResponse.newTellResponse(speech, a);
        }
        return SpeechletResponse.newTellResponse(speech);
    }

}
