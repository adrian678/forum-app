import React, {useState} from 'react';
import { Redirect} from 'react-router-dom';
import axios from 'axios';
// import logo from "../images/blabber-logo.png"
import "../common/Button.css"
import "../common/containers.css"
import "../Authentication/Signup.css"

export default function Signup(props){
    const [userInput, setUserInput] = useState({
        email: 'email', //TODO remove email field?
        username: 'username',
        password: 'password',
        submitted: false
    });
    let handleUserChange = (e)=>{
        setUserInput({username: e.target.value});
    };
    let handlePasswordChange = (e) =>{
        setUserInput({password: e.target.value});
    };

    let postToLogin = (e)=>{
        e.preventDefault();
        console.log("uncomment and implement the postToLogin function");
        // TODO post to the backend + redirect to new page
        //TODO confirm form of the expected dto to backend

        axios.post("http://localhost:8081/sign-up", {
            username : this.state.username,
            password: this.state.password
        })
        .then(response => {
          console.log(response);
          setUserInput({submitted:true});
        })
        .catch(e => console.log(e));
        
    }

    return(
        <div class="SignupPage">
            {/* TODO make a centered container around signup-form container a fluid container with clamped min width and height */}
            {/* <div className="signup-container"> */}
            {/* TODO include a blur in the background of signup-form */}
            {/* <div className="container--glass"> */}
                <form className="signup-form" onSubmit={postToLogin}>
                    <p>username</p>
                    <input className="signup-form__input" onChange={handleUserChange}  type="text" name="username" required>
                    </input>
                    <p>password</p>
                    <input className="signup-form__input" onChange={handlePasswordChange} type="text" name="password" required>

                    </input>
                    <button className="button--med button--pink" type="submit">
                        Sign Up!
                    </button>
                </form>
            {/* </div> */}
                
            {/* </div> */}
            {userInput.submitted && <Redirect to="/"></Redirect>}
        </div>
    )

    
}