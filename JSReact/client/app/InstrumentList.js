import React, { Component } from 'react';
import PropTypes from 'prop-types';
//import "../pages/css/style.css";

class InstrumentList extends Component {

  constructor(props) {
    super(props);
    this.state = { instrumentList : {} };
  }

  componentDidMount() {

    fetch(this.props.url)
      .then(res => { 
        return res = res.json();  
    })
      .then(res => this.setState( {instrumentList : res} ))
      .catch((err) => console.error(err));          
  }

  handleChkClick = event => {    
    let id = event.target.id;
    let tr = document.getElementById('tr_'+id);
    const isChecked = event.target.checked;
    if (isChecked) {
      tr.className='selected';  
      this.doInstrSelectionChange(id);
    } else {
      tr.className='ordinal';
      this.doInstrSelectionChange(-id);
    }
  
  }  

  doInstrSelectionChange = (num) => {
    let instrumentList = this.state.instrumentList;
    const id = parseInt(num);

    if (!isNaN(id)){
      let instr = {};
      let a_id = Math.abs(id);
      let instrName = instrumentList[a_id];
      instr[id] = instrName;
      this.props.instrSelectionChange(instr);
    }
  }


  render() {
      let instrList = this.state.instrumentList;
      let instrNums = Object.keys(instrList);
      let selList = this.props.selectedInstruments;
      let selNums = Object.keys(selList);

      return (
        <div>
          <h4>InstrumentList</h4>
            <div className='v-scroll'>            
              <table className='InstrumentsTable'>                       
              <tbody>              
                {instrNums.map((instrNum) => 
                <tr key={instrNum} id={'tr_'+instrNum} className={selNums.includes(instrNum) ? 'selected' : 'ordinal'}>
                {selNums.includes(instrNum) ?                
                    <td><input type='checkbox' id={instrNum} checked onClick={this.handleChkClick} /></td>
                  : <td><input type='checkbox' id={instrNum} onClick={this.handleChkClick} /></td>
                }
                  <td>{instrNum}</td>
                  <td>{instrList[instrNum]}</td>
                </tr>)}     
              </tbody>
              </table>  
            </div>


        </div>
      );
  }


}

InstrumentList.propTypes = {
    url : PropTypes.string
 }

InstrumentList.defaultProps = {
    url : 'http://localhost:9099/instrumentList'
}

export default InstrumentList;