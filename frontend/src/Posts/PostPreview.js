import React, { useContext, useState } from "react";
import {Link, Redirect} from "react-router-dom";
import {FaRegArrowAltCircleUp} from "react-icons/fa";
import "./PostPreview.css"
export default function PostPreview(props){

    return (
        <div className="PostPreview">
            <Link to={"/posts/" + props.postId}>
            <h4>
                {props.author}
            </h4>
            <h3>
                {props.title}
            </h3>
            </Link>
            
        </div>
    )
}