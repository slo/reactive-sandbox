package sl.testapp.otherservice;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService{
	
	private List<String> greetings = Arrays.asList("WitajPL","WitajEN","WitajDE","WitajFR","WitajGB","WitajCZ");

	@Override
	public String getGreeting() throws InterruptedException {
		Random r = new Random();
		Thread.sleep(500);
		return greetings.get(r.nextInt(greetings.size()));
	}
	
	

}
