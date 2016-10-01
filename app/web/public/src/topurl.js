var TopUrl = React.createClass({
  getInitialState: function() {
    console.log('getInitialState');
    return { urls :  [ { url : 'wwww.google.com'}]};
  },

  componentDidMount: function() {
    var xhr = createCORSRequest('GET', this.props.source);
    if (!xhr) {
      throw new Error('CORS not supported');
    }
    xhr.onload = function() {
      var result = JSON.parse(xhr.responseText);
      this.setState({
              urls: result
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
      <h2> Top URLs </h2>
      <ul>
      {
        this.state.urls.map(function(url){
          return <li>
                    <div>
                      <a href={url.url}>{url.url}</a>
                         count: {url.count}
                    </div>
                </li>;
        })}
       </ul>
       </div>
     )
  }
});

ReactDOM.render(
  <TopUrl source="http://localhost:8080/urls/top/10"/>,
  document.getElementById('topurl')
);
