import React from 'react';
import {render} from 'react-dom';
import LoginForm from './LoginForm';
import InstrumentList from './InstrumentList';
import Rates from './Rates';

class App extends React.Component {

  constructor(props) {
    super(props);

    this.state = {
      isLoggedIn: false,
      selectedInstruments: {}

    }
  }  

  handleLoggedIn = () => {
  	this.setState({
  			isLoggedIn: true
  		})  	
  }

  handleSelectionChange = (instr) => {

    let selectedInstruments = this.state.selectedInstruments;

  	const num = parseInt(Object.keys(instr)[0]);

  	if (!isNaN(num)){
  		if (num >= 1) {        
  			selectedInstruments[num] = instr[num];
  		} else {
  			if (num <= -1){
  				if (!( Object.keys(selectedInstruments).length === 0 && selectedInstruments.constructor === Object )) {
  					delete selectedInstruments[Math.abs(num)];
  				}
  			}
  		}

      this.setState({ selectedInstruments : selectedInstruments});
  	}
  }

  handleInstrRemove = id => {
    let selectedInstruments = this.state.selectedInstruments;
    delete selectedInstruments[id];
    this.setState({ selectedInstruments : selectedInstruments});
  }

  render () {
  	const isLoggedIn = this.state.isLoggedIn;
  	
  	let MustLogIn = null;
  	let Instrs    = null;
  	let Prices    = null;

  	if (!isLoggedIn) {
  		MustLogIn = <LoginForm loggedIn={this.handleLoggedIn}/>

  	} else {
  		Instrs = <InstrumentList selectedInstruments={this.state.selectedInstruments} instrSelectionChange={this.handleSelectionChange}/>
  		Prices = <Rates selectedInstruments={this.state.selectedInstruments} instrRemove={this.handleInstrRemove}/>
  	}


    return (
    	<div>
    		<h3>ExactPro Systems</h3>
    
    		{MustLogIn}

    		{Instrs}

    		{Prices}
    
    	</div>
    	)
  }
}

render(<App/>, document.getElementById('app'));

