import React, {useContext, useState} from 'react';
import { Link, Redirect} from 'react-router-dom';
import axios from 'axios';
// import logo from "../images/blabber-logo.png"
import {AuthContext} from "../Authentication/AuthContext"
import "../common/Button.css"
import "../common/containers.css"
import "./Login.css"

export default function Login(props){

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [submitted, setSubmitted] = useState(false);
    const auth = useContext(AuthContext);

    let handleUserChange = (e)=>{
        setUsername(e.target.value);
    };
    let handlePasswordChange = (e) =>{
        setPassword(e.target.value);
    };

    let postToLogin = (e)=>{
        e.preventDefault();
        // TODO post to the backend + redirect to new page
        //TODO confirm form of the expected dto to backend
        // console.log(userInput);
        axios.post("http://localhost:8081/login", {
            username : username,
            password: password
        })
        .then(response => {
          console.log(response);
          auth.setAuth({
            isAuthenticated: true,
            user: username,
            token: response.data,
          });
          console.log(auth);
          setSubmitted(true);
        })
        .catch(e => console.log(e));
        
    }

    return(
        <div class="page LoginPage">
            {/* TODO make a centered container around signup-form container a fluid container with clamped min width and height */}
            {/* <div className="signup-container"> */}
            {/* TODO include a blur in the background of signup-form */}
            {/* <div className="container--glass"> */}
                <form className="login-form" onSubmit={postToLogin}>
                    <h1>
                        Login
                    </h1>
                    {/* TODO replace the element type of the labels from p to something else */}
                    <p className="login-form__label">username:</p>
                    <input className="login-form__input" onChange={handleUserChange}  type="text" name="username" required>
                    </input>
                    <p className="login-form__label">password:</p>
                    <input className="login-form__input" onChange={handlePasswordChange} type="text" name="password" required>

                    </input>
                    <button className="button--med button--pink login-form__button" type="submit" onClick={postToLogin}>
                        Login!
                    </button>
                    {/* TODO add underline to link */}
                    <Link to="/signup">
                        Not signed up? Create an account
                    </Link>
                </form>
            {/* </div> */}
                
            {/* </div> */}
            {submitted && <Redirect to="/"></Redirect>}
        </div>
    )

    
}