package tarun.learning.org.TwitterKafkaProducer.impl;

import tarun.learning.org.TwitterKafkaProducer.interfaces.Filter;
import twitter4j.Status;
import java.util.Set;

public class LanguageAndScreenNameFilter implements Filter {
    private final String language;
    private final Set<String> acceptedScreenNames;

    public LanguageAndScreenNameFilter(String language, Set<String> acceptedScreenNames) {
        this.language = language;
        this.acceptedScreenNames = acceptedScreenNames;
    }
    @Override
    public boolean filter(Status status) {
      try {
        // add the tweet into the queue buffer
        String lang = status.getLang();
        String screenname = status.getUser().getScreenName();
        System.out.println(screenname);
        if (screenname != null && screenname.length() > 0 &&
            acceptedScreenNames.contains(screenname.toLowerCase())) {
                System.out.println("----FOUND------");
              if (lang != null && lang.length() > 1 && lang.equals(language)) {
                return true;
              }
        }
     }catch(Exception ex) {
       return false;
     }
     return false;
    }
}
