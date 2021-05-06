import React, {useState, useContext, useEffect} from 'react';
import { Link, Redirect, useLocation} from 'react-router-dom';
import axios from 'axios';
// import logo from "../images/blabber-logo.png"
import {AuthContext} from "../Authentication/AuthContext";

//CSS imports
import "../common/pages.css";
import "../Home/HomePage.css";
import "../common/Variables.css";
import "../common/Button.css";
import "../common/containers.css";
import Navbar from '../common/Navbar';
import PostPreview from "../Posts/PostPreview";

export default function HomePage(props){

    const [posts, setPosts] = useState([]);
    const auth = useContext(AuthContext);
    let location = useLocation();

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
        axios.get(`http://localhost:8081/posts`, headers)
              .then(response => {
                console.log(response.data);
                setPosts(response.data._embedded.postResponseDtoList);
              })
              .catch((e) => console.log(e))
    };
    useEffect(fetchPosts, []);

    // let linkStyle = {
    //   margin: "2rem 0px 2rem 0px",
    //   background-color:"white"
    // }

    return(
        <div className="page page--violet">
            <Navbar/>
            <div className="HomePage__container container--gray-bg container--full-width">
              <div className="container--70width  container--content-center">
                {posts.map((post)=>{
                      return(
                          //TODO Need to validate whether post is empty
                          <Link style={{margin: "2rem 0px 2rem 0px", background:"white" }} key={post.postId.uuid} to={{pathname: `/posts/${post.postId.uuid}`, 
                          state: {
                            background : location,
                            pid: post.postId.uuid,
                            title: post.title,
                            user: post.author,
                            content: post.content
                          },}}>
                            <PostPreview key={post.postId.uuid} 
                            author={post.author} 
                            title={post.title} 
                            postId={post.postId.uuid}
                            url={`/posts/${post.postId.uuid}`}
                            linkState= {{
                              background : location,
                              pid: post.postId.uuid,
                              title: post.title,
                              user: post.author,
                              content: post.content
                            }}/>
                          </Link>
                        
                      )
                  })}
              </div>
            </div>
        </div>
    )

}