import React, { useContext, useState } from "react";
import {Link, Redirect} from "react-router-dom";

import {AuthContext} from "../Authentication/AuthContext"
import "./Button.css"
import "./Navbar.css"
export default function Navbar(props){
    const auth = useContext(AuthContext);

    let userSection;
    if(!auth.isAuthenticated){
        userSection = (
            <div>
                <Link to="/signup">
                    <button className="button--small button--inv-aqua">
                        Sign up
                    </button>
                </Link>
                <Link  to="/login">
                    <button className="button--small button--aqua">
                        Login
                    </button>
                </Link>
            </div>
        );
    }
    


    return(
        <nav className="navbar navbar-font">
{/*             Logo section
                <div className="navbar-section">
                    <Link exact to="/">
                        <img className="Logo" src={logo} alt="Blabber"></img>    
                    </Link>
                    
                </div>
                 */}
                <div className="navbar-section">
                    <Link className=" navbar-item" to="/about">About</Link>
                </div>

                {userSection}
                {/* <div>
                    <button className="button--inv-aqua ">
                        Login
                    </button>
                    <button className="button--aqua ">
                        Signup
                    </button>

                </div> */}
            </nav>
    );
}