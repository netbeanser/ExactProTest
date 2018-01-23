import React from 'react';
import {render} from 'react-dom';
import LoginForm from './LoginForm';

class App extends React.Component {
  render () {
    return (
    	<div>
    		<h3>ExactPro</h3>
    		<LoginForm />
    	</div>
    	)
  }
}

render(<App/>, document.getElementById('app'));
