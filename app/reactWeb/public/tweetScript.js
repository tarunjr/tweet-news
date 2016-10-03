var Article = React.createClass({
  render: function() {
    return (
      <div className="email">
        <dl className="meta dl-horizontal">
          <dt>Source</dt>
          <dd>{this.props.source}</dd>

          <dt>Title</dt>
          <dd>{this.props.title}</dd>

          <dt>Article Link</dt>
          <dd><a href={this.props.id}>Full Article</a></dd>

        </dl>
        <div className="body" dangerouslySetInnerHTML={{__html: this.props.summary}}></div>
      </div>
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
        <ArticleListItem key={article.id}
                       source={article.source}
                       title={article.title}
                       beginning={article.beginning}
                       on_click={this.props.onSelectArticle.bind(null, article.url)} />
      );
    }.bind(this));

    return (
      <table className="email-list table table-striped table-condensed">
        <thead>
          <tr>
            <th>Source</th>
            <th>Title</th>
          </tr>
        </thead>
        <tbody>
          {article_list}
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
            key={hashtagbox.id}
            onClick={this.props.onSelectHashtagbox.bind(null, hashtagbox.id)}>
          <span className="badge">
            {hashtagbox.articles.length}
          </span>
          {hashtagbox.name}
        </li>
      );
    }.bind(this));

    return (
      <div className="col-md-2">
        <div>Popular</div>
        <ul className="mailboxes list-group">
          {hashtagbox_list}
        </ul>
      </div>
    );
  }
});

var App = React.createClass({
  getInitialState: function(){
    return { hastagbox_id: null };
  },

  handleSelectHashtagbox: function(id) {
    this.setState({ hastagbox_id: id });
  },

  render: function() {
    var hastagbox_id = this.state.hastagbox_id;
    if (hastagbox_id) {
      var hastagbox = this.props.hashtagboxes.filter(function(hashtagbox) {
        return hashtagbox.id == hastagbox_id;
      })[0];
      selected_hastagbox = <Hashtagbox key={hastagbox.id}
                                  articles={hastagbox.articles} />;
    } else {
      selected_hastagbox = <NoneSelected text="hashtag" />;
    }

    return (
      <div className="app row">
        <HashtagboxList hashtagboxes={this.props.hashtagboxes}
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
var fixtures = [
  {
    id: 1,
    name: "UNGA",
    articles: [
      {
        "url": "https://t.co/Ey2QvkSbdy1",
        "source": "India TV",
        "keywords": [ "indulging","shown"],
        "summary": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "Indisciplined Party Workers Will Be Shown The Door"
      },
      {
        "url": "https://t.co/Ey2QvkSbdy122",
         "source": "ZeeNews",
        "keywords": [ "indulging","shown"],
        "summary": "SECOND Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "SECOND: Indisciplined Party Workers Will Be Shown The Door"
      }
    ]
  },
  {
    id: 2,
    name: "500Test",
    articles: [
      {
        "url": "https://t.co/Ey2QvkSbdy3",
        "source": "TimesNow",
        "keywords": [ "indulging","shown"],
        "summary": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "India win the first test"
      },
      {
        "url": "https://t.co/Ey2QvkSbdy124",
        "source": "NDTV",
        "keywords": [ "indulging","shown"],
        "summary": "SECOND Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "Rahane does the trick"
      }
    ]
  }
];
React.render(
  <App hashtagboxes={fixtures} />,
  document.body
);
