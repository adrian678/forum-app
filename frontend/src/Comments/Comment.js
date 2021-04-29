import React, { useContext, useState } from "react";
import {Link, Redirect} from "react-router-dom";
import Navbar from "../common/Navbar";
import {BsFillPersonFill, BsBookmarkPlus, BsBookmarkFill} from "react-icons/bs";
import {AiOutlineLike, AiTwotoneLike} from "react-icons/ai";
import "./Comment.css"
import "../common/Variables.css"

export default function Comment(props){

    const [isLiked, setIsLiked] = useState(false);
    const [isSaved, setIsSaved] = useState(false);

    let replyingTo = (commentId) =>(
        // TODO make the commentId a link
        <div>
            replying to {commentId}
        </div>
    )

    let likeIcon = ()=>{
        if(isLiked){
            return (
                <AiTwotoneLike/>
            )
        }
        return (
            <AiOutlineLike/>
        )
    }

    let bookmarkIcon = ()=>{
        if(isLiked){
            return (
                <BsBookmarkFill/>
            )
        }
        return (
            <BsBookmarkPlus/>
        )
    }

    // let fetchLikeStatus = ()=>{

    // }

    // TODO send a like POST request
    

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
                    {/* TODO convert to a friendlier time format */}
                    {props.createdAt}
                </div>
                {/* The report may require a separate component for itself */}
                <div className="Comment_report">
                    {/* Make this a button? */}
                    report
                    {/* {props.points} */}
                    {/* {props.timestamp} TODO add timestamp to comment dto */}
                </div>
            </div>
            <div className="Comment__content">
                {/* should be indented or padded left */}
                {props.parentComment && replyingTo(props.parentComment)}
                {props.content}
            </div>
            <div className="Comment__footer">
                {likeIcon()}
                {props.points}
                {bookmarkIcon()}

            </div>
            
        </div>

    );
}