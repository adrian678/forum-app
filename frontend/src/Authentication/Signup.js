import React, {useState} from 'react';
import { Redirect} from 'react-router-dom';
import axios from 'axios';
// import logo from "../images/blabber-logo.png"
import "../common/Button.css"
import "../common/containers.css"
import "../Authentication/Signup.css"

export default function Signup(props){

    const [username, setUsername] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [submitted, setSubmitted] = useState(false);

    let handleUserChange = (e)=>{
        setUsername(e.target.value);
    };
    let handleEmailChange = (e)=>{
       setEmail(e.target.value);
    };
    let handlePasswordChange = (e) =>{
        setPassword(e.target.value);
    };

    let postToSignUp = (e)=>{
        e.preventDefault();
        console.log("uncomment and implement the postToLogin function");
        // TODO post to the backend + redirect to new page
        //TODO confirm form of the expected dto to backend
        // console.log(userInput);
        axios.post("http://localhost:8081/sign-up", {
            username : username,
            password: password,
            email: email
        })
        .then(response => {
          console.log(response);
          setSubmitted(true);
        })
        .catch(e => console.log(e));
        
    }

    return(
        <div class="SignupPage">
            {/* TODO make a centered container around signup-form container a fluid container with clamped min width and height */}
            {/* <div className="signup-container"> */}
            {/* TODO include a blur in the background of signup-form */}
            {/* <div className="container--glass"> */}
                <form className="signup-form" onSubmit={postToSignUp}>
                    <h1>
                        Create an account
                    </h1>
                    <p>
                        Find people who share your interests!
                        Give your opinions on a post by commenting!

                    </p>
                    {/* TODO replace the element type of the labels from p to something else */}
                    <p className="signup-form__label">username:</p>
                    <input className="signup-form__input" onChange={handleUserChange}  type="text" name="username" required>
                    </input>
                    <p className="signup-form__label">email:</p>
                    <input className="signup-form__input" onChange={handleEmailChange}  type="text" name="email" required>
                    </input>
                    <p className="signup-form__label">password:</p>
                    <input className="signup-form__input" onChange={handlePasswordChange} type="text" name="password" required>

                    </input>
                    <button className="button--med button--pink" type="submit" onClick={postToSignUp}>
                        Sign Up!
                    </button>
                </form>
            {/* </div> */}
                
            {/* </div> */}
            {submitted && <Redirect to="/"></Redirect>}
        </div>
    )

    
}