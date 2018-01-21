import React, { Component } from 'react';
import PropTypes from 'prop-types';
const  W3CWebSocket = require('websocket').w3cwebsocket;


class Rates extends Component {

  constructor(props){
    super(props);
    this.state = { instrPerRow : 1,
      wsOpen : false,
      ws : undefined,
      prices : {},
      intervalId : undefined
   };
  }

  handleWsError = (err) => {
    console.error("Ws error: "+error);
  }

  handleWsOpen = () => {
    console.log('WebSocket is open');
    this.setState( {wsOpen: true});               
  }

  handleWsClose = () => {
    console.log("WS conection closed");
    this.setState( {wsOpen: false });
    
  }

  handleWsMessage = (evt) => {
    if (typeof evt.data === 'string'){
      let newPrices = evt.data;
      console.log("DATA="+newPrices);
      let prices = this.state.prices;
      let ids = Object.keys(newPrices);
      console.log("in handleWsMessage IDS="+JSON.stringify(ids));

      ids.map(id => {
        console.log("ID in OnMessage="+id);
        let instrName = this.props.selectedInstruments[id];
        let priceItem = {};
        priceItem.instrName = instrName;
        priceItem.price = newPrices[id]["price"];
        prices[id] = priceItem;
        console.log("In handleWsMessage: priceItem="+JSON.stringify(priceItem));
        console.log("In handleWsMessage: prices="+JSON.stringify(prices));   
      });
      this.setState({prices : prices});    
    }
  }

 

  componentWillReceiveProps(nextProps){

    let prices = this.state.prices;

    const oldInstrIds = Object.keys(this.state.prices);
    const newInstrIds = Object.keys(nextProps.selectedInstruments);
    const OmN = oldInstrIds.filter(id => !newInstrIds.includes(id));
    const NmO = ( oldInstrIds.length > 0 ) ? newInstrIds.filter(id => !oldInstrIds.includes(id)) : Object.keys(nextProps.selectedInstruments);   

    if (Object.keys(prices).length !== 0)  OmN.map((id) => {delete prices[id]});
    this.setState({prices : prices});

    NmO.map((id) => {
      let instrName = nextProps.selectedInstruments[id];

      fetch(this.props.url + id)      
      .then(res => {
        return res = res.json();
      })
      .then(res => {
          let priceItem = {};
          priceItem.instrName = instrName;
          priceItem.price = res["price"];
          prices[id] = priceItem;
          this.setState({prices : prices});
      })
      .catch(err => console.error(err));      
    })
    if (this.state.ws !== undefined &&  this.state.ws.readyState === this.state.ws.OPEN ){
      this.state.ws.send(JSON.stringify(Object.keys(nextProps.selectedInstruments)));
    }
  }

  fetchData = () => {
    let prices = this.state.prices;
    let ids = Object.keys(prices);
    ids.map(id => {
      console.log("ID in fetch="+id);
      let instrName = this.props.selectedInstruments[id];
      fetch(this.props.url + id)
      .then(res => {
        return res = res.json();
      })
      .then(res => {
          let priceItem = {};
          priceItem.instrName = instrName;
          priceItem.price = res["price"];
          prices[id] = priceItem;
          console.log("PriceItem="+JSON.stringify(priceItem));
          this.setState({prices : prices});        

      })
      .catch(err => console.error(err));
      const timerId = setTimeout(function() {return;}, 1000);
      clearTimeout(timerId);
    })
  }

  componentWillUnmount() {
    clearInterval(this.state.intervalId);
    this.state.ws.close();
  }

  componentWillMount() {
    const ws = new W3CWebSocket(this.props.urlR,"json");
    ws.onerror = this.handleWsError;
    ws.onclose = this.handleWsClose;
    ws.onmessage = this.handleWsMessage;
    ws.onopen = this.handleWsOpen;
    this.setState({ws : ws});
  } 

  componentDidMount() {
      if (this.state.intervalId !== undefined) {clearInterval(this.state.intervalId);}
  //    let intervalId = setInterval(this.fetchData, 60000);
  //    this.setState({ intervalId : intervalId });
   }

  doInstrRemove = event => {
    const id_b = event.target.id.substr(2);
    const id = parseInt(id_b);
    if (!isNaN(id)) this.props.instrRemove(id);
  }

  handleRadioClick = event => {
    let instrPerRow = 1;

    let id = event.target.id;
    switch (id) {
      case 'one': instrPerRow = 1;
      break;
      case 'two': instrPerRow = 2;
      break;
      case 'three': instrPerRow = 3;
      break;
      case 'four': instrPerRow = 4;
      break;      
      default : 1;            
    }
    this.setState({ instrPerRow : instrPerRow});
  }

  createPriceTbl = () => {
      const prices = this.state.prices;
      const instrNums = Object.keys(prices);

      if ( instrNums.length > 1) {

        let totRows = 0;
        if ( instrNums.length <= this.state.instrPerRow)  {
          totRows = 1;

        } else {
          totRows+=Math.trunc(instrNums.length/this.state.instrPerRow);      
          totRows+= ( (instrNums.length % this.state.instrPerRow) >0 ) ? 1 : 0;
        }

        const arrOfArr = [];

        for (let i=0;  i< totRows; i++) {
          arrOfArr.push(instrNums.slice(i*this.state.instrPerRow,(i+1)*this.state.instrPerRow));
        }

        return (
         arrOfArr.map(row => {
            return (
            <tr key={row.toString()}>
              {row.map(col => {
                  return [                 
                    <td key={col+21700} className='no-right-border'>{prices[col].instrName+' '}</td>,
                    <td key={col+21701} className='no-left-border'><button id={'b_'+col} onClick={this.doInstrRemove}>X</button></td>,
                    <td key={col+21702}>{prices[col].price}</td>
                  ]
              })
            }
             </tr>
             )
        })
        ) 

     }
  }


  render() {

      const prices = this.state.prices;
      const instrNums = Object.keys(prices);

      return (
        <div>
          <h4>WebSocket Server: <span className='msg'>{this.state.wsOpen?'connected':'disconnected'}</span></h4>             
          <h4>Prices</h4>
            <div>       
            <table className='PriceTable-f'>
            <tbody>
              {this.createPriceTbl()}
            </tbody>
            </table>
            </div>
            <div>
              <p>Number of instruments per row</p>
                <input type="radio" id="one" name="contact" value='1' defaultChecked  onClick={this.handleRadioClick}/>
                <label htmlFor="one">One</label>

                <input type="radio" id="two"  name="contact" value='2' onClick={this.handleRadioClick}/>
                <label htmlFor="two">Two</label>

                <input type="radio" id="three" name="contact" value='3' onClick={this.handleRadioClick}/>
                <label htmlFor="three">Three</label>

                <input type="radio" id="four" name="contact" value='4' onClick={this.handleRadioClick}/>
                <label htmlFor="four">Four</label>                
            </div>                                  
          </div>
      );
  }


}

Rates.propTypes = {
    url : PropTypes.string.isRequired,
    urlR : PropTypes.string.isRequired
 }

Rates.defaultProps = {
    url : 'http://localhost:9099/rate?id=',
    urlR : 'ws://localhost:9099/realtime',

}

export default Rates;


       