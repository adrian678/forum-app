import React, { useContext, useState } from "react";
import {Link, Redirect} from "react-router-dom";
import Navbar from "../common/Navbar";
import {BsFillPersonFill} from "react-icons/bs";
import "./Comment.css"

export default function Comment(props){

    

    return(
        <div className="Comment">
            {/* upper section: display flex with space in between */}
            <div className="Comment__header"> 
                {/* left section containing avatar, username, timestamp */}
                <div className="Comment__details">
                    <BsFillPersonFill size="1.5em"/>
                    {/* Link should be underlined on hover- give special class? */}
                    <Link to={"/users/" + props.author}>
                        {props.author}
                    </Link>
                </div>
                {/* The report may require a separate component for itself */}
                <div className="Comment_report">
                    {/* Make this a button? */}
                    report
                    {/* {props.points} */}
                    {/* {props.timestamp} TODO add timestamp to comment dto */}
                </div>
            </div>
            <div className="Comment_content">
                {/* should be indented or padded left */}
                {props.content}
            </div>
            
        </div>

    );
}