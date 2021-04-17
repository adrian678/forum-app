import React, { useContext, useState, useEffect } from "react";
import {useParams} from "react-router-dom";
import axios from "axios";

import {AuthContext} from "../Authentication/AuthContext";
import Navbar from "../common/Navbar";
import Comment from "../Comments/Comment";
import {RiArrowLeftCircleFill} from "react-icons/ri";

import "./PostPage.css";

export default function PostPage(props){

    const[comments, setComments] = useState([{commentId:"1", }])
    const {postId} = useParams();       //TODO on page refresh, this is undefined. 
    const auth = useContext(AuthContext);

    let fetchComments = ()=>{
        let headers = null;
        // if logged in, add jwt to requests
        if(auth.token){
          headers = {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer '+ auth.token
          }
        }
        //TODO remove console.log statement
        console.log(headers);
        // make api call
        axios.get(`http://localhost:8081/posts/${postId}/comments`, headers)
              .then(response => {
                console.log(response.data);
                setComments(response.data._embedded.commentResponseDtoList);
              })
              .catch((e) => console.log(e))
    };

    useEffect(fetchComments, []);

    return(
        <div className="page PostPage">
          <Navbar/>
          <div className="post-container">
            {/* TODO left arrow should show conditionally on modals */}
            <RiArrowLeftCircleFill size="2.5em" color="gray"/> 
            {comments.map(comment =>{
              return <Comment key={comment.commentId} author={comment.author} content={comment.content}/>
            })}
          </div>
        </div>

    );
}