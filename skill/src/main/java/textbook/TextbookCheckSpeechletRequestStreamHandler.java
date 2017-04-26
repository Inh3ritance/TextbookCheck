package textbook;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

/**
 * This class could be the handler for an AWS Lambda function powering an Alexa Skills Kit
 * experience. To do this, simply set the handler field in the AWS Lambda console to
 * "helloworld.HelloWorldSpeechletRequestStreamHandler" For this to work, you'll also need to build
 * this project using the {@code lambda-compile} Ant task and upload the resulting zip file to power
 * your function.
 */
public final class TextbookCheckSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds.add("amzn1.ask.skill.ad4d9e03-a661-4bd5-8a77-78bc990ddd1d");
    }

    public TextbookCheckSpeechletRequestStreamHandler() {
    	//supportedApplicationIds.add("amzn1.echo-sdk-ams.app.amzn1.ask.skill.ad4d9e03-a661-4bd5-8a77-78bc990ddd1d");
        super(new TextbookCheckSpeechlet(), supportedApplicationIds);
    }
}
