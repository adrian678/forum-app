import React, { useContext, useState } from "react";
import {Link, Redirect} from "react-router-dom";
import {FaUserCircle} from 'react-icons/fa'

import {AuthContext} from "../Authentication/AuthContext"
import "./Button.css"
import "./Navbar.css"
export default function Navbar(props){
    const {auth} = useContext(AuthContext);


    let userSection;
    if(!auth.isAuthenticated){
        userSection = (
            <div className="navbar-section">
                <Link to="/signup" className="navbar__item">
                    <button className="button--small button--ghost-violet">
                        Sign up
                    </button>
                </Link>
                <Link  to="/login" className="navbar__item ">
                    <button className="button--small button--ghost-yellow">
                        Login
                    </button>
                </Link>
            </div>
        );
    } else {
        userSection = (
            <div className="navbar-section">
                    <FaUserCircle size="32"/>
                    <span style={{fontSize:"1.4em"}}>{auth.user}</span>
                
                    <Link  to="/login" className="navbar__item ">
                        <button className="button--small button--ghost-red" onClick={auth.logout}>
                            Logout
                        </button>
                    </Link>
            </div>
        );
    }
    
    console.log(auth);

    return(
        <nav className="navbar navbar-font">
{/*             Logo section
                <div className="navbar-section">
                    <Link exact to="/">
                        <img className="Logo" src={logo} alt="Blabber"></img>    
                    </Link>
                    
                </div>
                 */}
                 {/* experiment on fonts */}
                <div className="navbar-section">
                    <Link className="navbar__item navbar__about" to="/about">About</Link>
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