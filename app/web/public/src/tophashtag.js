var TopHashTag = React.createClass({
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
    console.log('render');
    return (
      <div>
      <h2> Top Hash Tags </h2>
      <ul>
      {
        this.state.hashtags.map(function(hashtag){
          return <li>#{hashtag.tag}   count: {hashtag.count}</li>;
        })}
       </ul>
       </div>
     )
  }
});

ReactDOM.render(
  <TopHashTag source="http://localhost:8080/hashtags/top/10" />,
  document.getElementById('hashtag')
);
