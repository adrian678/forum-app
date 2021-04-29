import React, { useContext, useState, useEffect } from "react";
import {useParams} from "react-router-dom";
import axios from "axios";
import Navbar from "../common/Navbar";
import Avatar from "../resources/avatar.svg";

export default function UserPage(props){

    let {username} = useParams();

    // let fetchUserPosts= ()=>{
    //     axios.get(`http://localhost:8081/posts/${postId}/comments`, headers)
    //           .then(response => {
    //             console.log(response.data);
    //             setComments(response.data._embedded.commentResponseDtoList);
    //           })
    //           .catch((e) => console.log(e))
    // }


    return(
        <div className="page UserPage">
            <Navbar/>
            <img src={Avatar} alt="Avatar svg image"/>
            <button className="button--med">
                follow
            </button>

            {/* <div className="board-container">
                <h2>{boardName}</h2>
                
                {posts.map((post)=>{
                    return(
                        //TODO Need to validate whether post is empty
                        <Link key={post.postId.uuid} to={{pathname: `/posts/${post.postId.uuid}`, 
                        state: {
                          background : location,
                          pid: post.postId.uuid,
                          title: post.title,
                          user: post.author,
                          content: post.content
                        },}}>
                          <PostPreview key={post.postId.uuid} author={post.author} title={post.title} postId={post.postId.uuid}/>
                        </Link>
                       
                    )
                })}
            </div> */}
        </div>
    )
}