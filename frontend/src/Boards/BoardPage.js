import React, { useContext, useState, useEffect } from "react";
import {useParams} from "react-router-dom";
import {AuthContext} from "../Authentication/AuthContext";
import axios from "axios";
//   components
import Navbar from "../common/Navbar";
import PostPreview from "../Posts/PostPreview";
// css files
import "../common/pages.css";
import "./Boards.css";

export default function Board(props){

    const [posts, setPosts] = useState([]);
    const [dummyTitle, setDummytitle] = useState("boardName");
    const auth = useContext(AuthContext);
    const {boardName} = useParams();
    console.log(boardName);
    let fetchPosts = ()=>{
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
        axios.get(`http://localhost:8081/boards/${boardName}/posts`, headers)
              .then(response => {
                console.log(response.data);
                setPosts(response.data._embedded.postResponseDtoList);
              })
              .catch((e) => console.log(e))
    };

    useEffect(fetchPosts, []); //empty array added to ensure useEffect is called once

    return(
        <div className="page board-page">
            {/* TODO add a dark background for the page */}
            <Navbar/>
            {/* TODO add whitespace between navbar and board-container */}
            {/* TODO decrease max width of container */}
            <div className="board-container">
                <h2>{boardName}</h2>
                {/* Post previews go here */}
                {posts.map((post)=>{
                    return(
                        //TODO Need to validate whether post is empty
                       <PostPreview key={post.postId.uuid} author={post.author} title={post.title} postId={post.postId.uuid}/>
                    )
                })}
            </div>
        </div>
    )

}