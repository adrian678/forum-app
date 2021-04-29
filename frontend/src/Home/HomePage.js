import React, {useState, useContext} from 'react';
import { Link, Redirect} from 'react-router-dom';
import axios from 'axios';
// import logo from "../images/blabber-logo.png"
import {AuthContext} from "../Authentication/AuthContext";
import "../common/Button.css"
import "../common/containers.css"
import Navbar from '../common/Navbar';

export default function HomePage(props){

    const [boards, setBoards] = useState([]);
    const auth = useContext(AuthContext);

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
        axios.get(`http://localhost:8081/boards`, headers)
              .then(response => {
                console.log(response.data);
                // setBoards(response.data._embedded.postResponseDtoList);
              })
              .catch((e) => console.log(e))
    };

    return(
        <div>
            <Navbar/>
            <div className="container">

            </div>
        </div>
    )

}