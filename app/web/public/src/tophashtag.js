var HashTag = React.createClass({
  getInitialState: function() {
    console.log('getInitialState');
    return { hashtags :  [ { tag : 'UNGA'},{tag : 'IND'}]};
  },

  componentDidMount: function() {
    var xhr = createCORSRequest('GET', this.props.source);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      var result = JSON.parse(xhr.responseText);
      this.setState({
              hashtags: result
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
      <h4> {this.props.data.tag} </h4>
      <ul>
          {
            this.props.data.urls.map(function(url){
              return   <li> <a href={url}>{url}</a>  </li>
            })
          }
       </ul>
       </div>
     )
  }
});

var TopHashTag = React.createClass({
  getInitialState: function() {
    return { hashtags :  [ { tag : 'UNGA', urls: ["https://t.co/UbMutXNKmV","https://t.co/OAaVh4bsCt"]},
                           { tag : 'IND',  urls: ["https://t.co/N9ksSWZITG","https://t.co/zCtRSzwEjj"]}]};
  },

  componentDidMount: function() {
    var xhr = createCORSRequest('GET', this.props.source);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      var result = JSON.parse(xhr.responseText);
      this.setState({
              hashtags: result
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
          this.state.hashtags.map(function(hashtag) {
            return <HashTag key={hashtag.tah} data={hashtag}/>;
          })
        }
      </div>
    );
  }
});

ReactDOM.render(
  <TopHashTag source="http://localhost:8080/hashtags/top/10" />,
  document.getElementById('hashtag')
);
