import React, {useState} from 'react';
import './App.css';
import "./common/Variables.css";
import {  BrowserRouter as Router} from "react-router-dom";
import ModalSwitch from './ModalSwitch';
import {AuthContext} from "./Authentication/AuthContext" 



function App() {
  // TODO try to replace the auth object with anonymous from AuthenticationContext
  const [auth, setAuth] = useState({
    auth : {
      isAuthenticated: false,
      user: false,
      token: false,
    }});
  let logout = ()=>{
    console.log("logout called");
    setAuth({auth: {
      isAuthenticated: false,
      user: null,
      token: null,
    }});
  };
  return (
    <div className="App">
      {/* <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header> */}
      <AuthContext.Provider value={{auth: auth, logout: logout}}>
        <Router>
          <div id="appContainer">
              <ModalSwitch/>
          </div>
        </Router>
      </AuthContext.Provider>
      
    </div>
  );
}

export default App;
