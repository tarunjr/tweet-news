var Article = React.createClass({



  render: function() {
    return (
      <div><h3><a href={this.props.data.url}>{this.props.data.title}</a></h3>
      <p>{this.props.data.summary} </p>
      </div>
     )
  }
});

var Articles = React.createClass({
  getInitialState: function() {
    console.log('getInitialState');
    return { "articles":
    [
      {
        "url": "https://t.co/Ey2QvkSbdy",
        "keywords": [ "indulging","shown"],
        "summary": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "Indisciplined Party Workers Will Be Shown The Door"
      },
      {
        "url": "https://t.co/Ey2QvkSbdy12",
        "keywords": [ "indulging","shown"],
        "summary": "SECOND Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"indiscipline\" in the party will be shown the door.\nAkhilesh Yadav will form the next government,\" he said, adding that all was well in the party.\n\"The SP government has done a lot of good work...people are wise and will vote it back to power.\n\"All those indulging in indiscipline and wrong practices will be shown the door... those against whom reports were received that they were involved in land grabbing and other such deeds, have been expelled,\" Mr Yadav, who arrived in Etawah to a rousing reception from his supporters, told reporters.On being asked about a number of leaders leaving the party-fold following the feud in the Samajwadi Party, he said the ruling party has been strengthened with \"parting of ways\" with such leaders.",
        "beginning": "Samajwadi Party Uttar Pradesh unit President Shivpal Yadav today said that all those indulging in \"i",
        "title": "SECOND: Indisciplined Party Workers Will Be Shown The Door"
      }
    ]
  }
  },
  componentDidMount: function() {
    var xhr = createCORSRequest('GET', this.props.source);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      console.log(xhr.responseText);
      var result = JSON.parse(xhr.responseText);
      this.setState({
              articles : result
      });
    }.bind(this);
    xhr.onerror = function() {
      console.log('There was an error!');
    };
    xhr.send();
  },
  render: function() {
    return (
      <div>
        {
          this.state.articles.map(function(article) {
            return <Article key={article.url} data={article}/>;
          })
        }
      </div>
    );
  }
});

ReactDOM.render(
  <Articles source="http://localhost:8081/api/v1/articles" />,
  document.getElementById('articles')
);
