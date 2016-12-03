var Article = React.createClass({
  render: function() {
    return (
      <tr>
      <td>
      <div className="email">
        <div><h4><a href={this.props.id} target="_blank">{this.props.title}</a></h4></div>
        <div className="body" dangerouslySetInnerHTML={{__html: this.props.summary}}></div>
      </div>
      </td>
      </tr>
    );
  }
});

var ArticleListItem = React.createClass({
  render: function() {
    return (
      <tr onClick={this.props.on_click.bind(null)}>
        <td>{this.props.source}</td>
        <td>{this.props.title}</td>
      </tr>
    );
  }
});

var ArticleList = React.createClass({
  render: function() {
    var article_list = this.props.articles.map(function(article) {
      return (
        /*
        <ArticleListItem key={article.id}
                       source={article.source}
                       title={article.title}
                       beginning={article.beginning}
                       on_click={this.props.onSelectArticle.bind(null, article.url)} />
                       */
         <Article key={article.id} source={article.source} title={article.title} id={article.id} summary={article.summary}/>
      );
    }.bind(this));

    return (
      <table className="email-list table table-striped table-condensed">
        <thead>
          <tr>
            <th>Trending Articles</th>
          </tr>
        </thead>
        <tbody>
          {article_list}
        </tbody>
      </table>
    );
  }
});
var TweetListItem = React.createClass({
  render: function() {
    return (
      <tr>
        <tr>
          <td><h5>{this.props.text}</h5></td>
        </tr>
        <tr>
          <td>Source: {this.props.screenname}, Count: {this.props.popularitycount}</td>
          <td></td>
        </tr>
        <tr>
          <td></td>
          <td></td>
        </tr>
      </tr>
    );
  }
});
var TweetList = React.createClass({
  render: function() {
    var tweet_list = this.props.tweets.map(function(tweet) {
      return (
        <TweetListItem key={tweet.id}
                       screenname={tweet.screenname}
                       popularitycount={tweet.popularitycount}
                       text={tweet.text}/>
      );
    }.bind(this));

    return (
      <table className="email-list table table-condensed">
        <thead>
          <tr>
            <th>Trending Tweets</th>
          </tr>
        </thead>
        <tbody>
          {tweet_list}
        </tbody>
      </table>
    );
  }
});

var NoneSelected = React.createClass({
  render: function() {
    return (
      <div className="none-selected alert alert-warning" role="alert">
        <span>No {this.props.text} selected.</span>
      </div>
    );
  }
});

var Hashtagbox = React.createClass({
  getInitialState: function(){
    return { article_id: null };
  },

  handleSelectArticle: function(id) {
    this.setState({ article_id: id });
  },

  render: function() {
    var article_id = this.state.article_id;
    if (article_id) {
      var article = this.props.articles.filter(function(article) {
        return article.url == article_id;
      })[0];
      selected_article = <Article id={article.url}
                              title={article.title}
                              source={article.source}
                              beginning={article.beginning}
                              summary={article.summary} />;
    } else {
      selected_article = <NoneSelected text="article" />;
    }

    return (
      <div>
        <h3>#{this.props.hashtag}</h3>
        <TweetList tweets={this.props.tweets}/>
        <ArticleList articles={this.props.articles}
                   onSelectArticle={this.handleSelectArticle} />
        <div className="email-viewer">
          {selected_article}
        </div>
      </div>
    );
  }
});
var HashtagboxList = React.createClass({
  render: function() {
    var hashtagbox_list = this.props.hashtagboxes.map(function(hashtagbox) {
      return (
        <li className="list-group-item"
            key={hashtagbox.tag}
            onClick={this.props.onSelectHashtagbox.bind(null, hashtagbox.tag)}>
          <span className="badge">
            {hashtagbox.count}
          </span>
          <h5>#{hashtagbox.tag}</h5>
        </li>
      );
    }.bind(this));

    return (
      <div className="col-md-2">
        <ul className="mailboxes list-group">
          {hashtagbox_list}
        </ul>
      </div>
    );
  }
});

var App = React.createClass({
  getInitialState: function(){
    return { hastagbox_id: null ,
             hashtagboxes : [{"tag":"","count":0,"articles":[], "urls":[], "topTweets":[]}]
           };
  },

  handleSelectHashtagbox: function(id) {
    this.setState({ hastagbox_id: id,
                    hashtagboxes: this.state.hashtagboxes});
  },
  componentDidMount: function() {
    var xhr = createCORSRequest('GET', this.props.source);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      var result = JSON.parse(xhr.responseText);
      // TODO get the article details
      //result.map(function(hashtagbox) {
      //    hashtagbox['articles'] = [];
      //});
      // END TODO

      this.setState({
            hastagbox_id: result[0].tag,
            hashtagboxes: result
      });
    }.bind(this);
    xhr.onerror = function() {
      console.log('There was an error!');
    };
    xhr.send();
  },

  render: function() {
    var hastagbox_id = this.state.hastagbox_id;
    if (hastagbox_id) {
      var hastagbox = this.state.hashtagboxes.filter(function(hashtagbox) {
        return hashtagbox.tag == hastagbox_id;
      })[0];
      selected_hastagbox = <Hashtagbox key={hastagbox.tag}
                                  articles={hastagbox.articles}
                                  tweets={hastagbox.topTweets}
                                  hashtag={hastagbox.tag} />;
    } else {
      selected_hastagbox = <NoneSelected text="hashtag" />;
    }

    return (
      <div className="app row">
        <HashtagboxList hashtagboxes={this.state.hashtagboxes}
                     onSelectHashtagbox={this.handleSelectHashtagbox} />
        <div className="mailbox col-md-10">
          <div className="panel panel-default">
            <div className="panel-body">
              {selected_hastagbox}
            </div>
          </div>
        </div>
      </div>
    );
  }
});
var fixtures = [{"tag":"Baramulla","count":77,"articles":[], "urls":["https://t.co/rcaBr6XdPZ","https://t.co/zacMIDoZsD","https://t.co/GWa8O9E9x1","https://t.co/UPYbHVdqTq","https://t.co/4griCCnAHC","https://t.co/pgKMbrT0UL","https://t.co/xoBYQSBwtM","https://t.co/Up8cdPCjZ0","https://t.co/gnXvyC7N19","https://t.co/YuMHJNxSim","https://t.co/PTsi13sh9D","https://t.co/QolAqBhMxZ","https://t.co/NcJHuz8VJ1","https://t.co/moxM0IL2n2","https://t.co/3IyInXXJaB","https://t.co/ryCPkt5WzM","https://t.co/R2hFOK0jwR","https://t.co/XUoxfatMed","https://t.co/pW859sspSV","https://t.co/68kpRD5qKe","https://t.co/SMte1sUu9r","https://t.co/KCHtmzt6MU","https://t.co/2JL42doIBp","https://t.co/nTsuThxmTS"],"topTweets":[{"id":782658416191778816,"hashtag":"Baramulla","text":"#UPDATE #Baramulla incident: Combing operations on, 2 terrorists neutralised, confirms army","screenname":"TimesNow","popularitycount":669},{"id":782656902861815809,"hashtag":"Baramulla","text":"#Baramulla Incident situation contained and under control: Army https://t.co/UPYbHVdqTq","screenname":"TimesNow","popularitycount":466},{"id":782670492171837440,"hashtag":"Baramulla","text":"#Baramulla incident: Situation under control https://t.co/il4MXxzFZx","screenname":"TimesNow","popularitycount":359},{"id":782651029745569792,"hashtag":"Baramulla","text":"#UPDATE #Baramulla firing: Heavy exchange of fire between terrorists and forces. Reports say, two BSF jawans injured","screenname":"TimesNow","popularitycount":265},{"id":782651177708101632,"hashtag":"Baramulla","text":"#UPDATE #Baramulla firing: Report suggest army camp security not breached","screenname":"TimesNow","popularitycount":246},{"id":782654498820853760,"hashtag":"Baramulla","text":"#UPDATE #Baramulla firing: Reports say, Home Minister, Chief of the Army Staff and National Security Agency briefed on the attack","screenname":"TimesNow","popularitycount":232}]},{"tag":"UPDATE","count":27, "articles":[],"urls":["https://t.co/fOVzLQTAzp","https://t.co/Nr4TFMc7XT"],"topTweets":[{"id":782658416191778816,"hashtag":"UPDATE","text":"#UPDATE #Baramulla incident: Combing operations on, 2 terrorists neutralised, confirms army","screenname":"TimesNow","popularitycount":669},{"id":782647176258170881,"hashtag":"UPDATE","text":"#UPDATE Baramulla firing: Quick response team at spot. Reports say, two terrorists neutralised by forces","screenname":"TimesNow","popularitycount":594},{"id":782639817360146433,"hashtag":"UPDATE","text":"#UPDATE Grenades lobbed near army camp in Baramulla. Heavy exchange of fire between terrorists and forces","screenname":"TimesNow","popularitycount":497},{"id":782651029745569792,"hashtag":"UPDATE","text":"#UPDATE #Baramulla firing: Heavy exchange of fire between terrorists and forces. Reports say, two BSF jawans injured","screenname":"TimesNow","popularitycount":265},{"id":782651177708101632,"hashtag":"UPDATE","text":"#UPDATE #Baramulla firing: Report suggest army camp security not breached","screenname":"TimesNow","popularitycount":246},{"id":782654498820853760,"hashtag":"UPDATE","text":"#UPDATE #Baramulla firing: Reports say, Home Minister, Chief of the Army Staff and National Security Agency briefed on the attack","screenname":"TimesNow","popularitycount":232}]}];
React.render(
  <App source="http://sequoia-590611473.us-east-1.elb.amazonaws.com/trends/api/v1/hashtags/top/3"/>,
  document.body
);
