import React, { Component } from "react";
import PropTypes from 'prop-types';
//import "../pages/css/style.css";

class LoginForm extends Component {
  constructor(props) {
    super(props);
    this.state = {
      email: "",
      password: ""
    };
  }

  doLogin = () => {
    this.props.loggedIn();
  }

  validateForm() {
    return this.state.email.length > 3 && this.state.password.length > 3;
  }

  handleChange = event => {
    this.setState({
      [event.target.id]: event.target.value
    });
   
  }

  handleSubmit = event => {
    event.preventDefault();

    this.doLogin();

  }

  render() {
 
    return (
      <div className="Login">
        <form onSubmit={this.handleSubmit} method="post" action={this.props.loginUrl}>
        <table className="loginForm">
        <tbody>
          <tr>
            <td>Email:</td>
            <td><input id="email" name="email" type="text"  value={this.state.email} onChange={this.handleChange}/></td>            
          </tr>
          <tr>
            <td>Password:</td>
            <td><input id="password" name="password" type="password" value={this.state.password} onChange={this.handleChange} /></td>            
          </tr>
        </tbody>  
        </table>
          <button            
            disabled={!this.validateForm()}
            type="submit">
            Login
          </button>
        </form>
      </div>
    );
  }
}

LoginForm.propTypes = {
    loginUrl : PropTypes.string
 }

LoginForm.defaultProps = {
    loginUrl : '/login'
}

export default LoginForm;