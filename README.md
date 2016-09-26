# Tweet-News: Trending news articles aggregated and summarized from Twitter feed.

### Vision:

Provide and easy way to discover and read popular news articles on the go.

### Problem:

Given the busy nature of work and life, sometimes it is difficult to find time to read about that is happening
around the world. There is a need for simple interface to get the summary of important news articles to read anywhere
anytime.

### Solution:
Tweet-News is a simple interface application which provides a list of top news articles mined out of twitter feeds
and presented in an easy card like interface to get the summary of the article content.

### System Architecture

* Overview

System is designed as near realtime stream processing pipeline using Twitter4J, Kafka and Apache Storm. Redis provides the serving DB. Microservices developed in Spring.Boot and Node.JS provide minimalist API for the application to present 

#### System Architecture Diagram:

#### Data Design:

Following domain entities will be modelled in the system.

* Tweet : A tweet from Twitter system.
* HashTag: A token using in Tweet to classify/represent topic for aggregation.
* URL: A HTTP URL for a web resource.
* Article: A web page containing a story published by news agency.
* Card: UX for displaying news article summary for user.

In the streaming pipeline data is represented in Avro format.

#### Services Design:

Following are the runtime services component of the system.

##### Trends Service:

Manages the trending HashTag, URL entites.Developed as a Micrsservice using Sprint.Boot(Java) and Redis(DB).

##### Information Extraction Service:

Provides the functionality to download, parse and extract information from the news article. Developed in Django (Python) and used Newspaper library hosted @ https://github.com/codelucas/newspaper for actual information extraction.

##### Articles Service:

Provides the capability to cache and serve extracted article information. Uses the Extraction Service to do the actual inforamtion retrieval. Developed in Node.JS and uses MongoDB to cache extracted article summary for fast access.

#### Analytics Design:

Following are the analytics components. Apache Storm used as the stream processing framework, Kafka as data ingestion and Redis as the serving DB. Twitter4J streaming is used as the source of Tweets.

##### Window based aggregation of HashTag counts:

A simple Storm topplogy to perform tick based time window aggreation of HashTag's parsed out of twitter feed coming from Kafka topic.

##### Window based aggregation of URL counts:

A simple Storm topplogy to perform tick based time window aggreation of URL's parsed out of twitter feed coming from Kafka topic.

#### Application Design (In Development):

Application components are implemented a native android applications interacting with only the services component.

* Article Cards:

User interface for consumer to discover trending article and get summary view for quick reading.


 
