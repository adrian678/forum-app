import React, {Component, useState, useEffect} from "react";
import {
    BrowserRouter as Router,
    Switch,
    Route,
    Link,
    useHistory,
    useLocation,
    useParams
  } from "react-router-dom";

  export default function PostModal(props) {
    let history = useHistory();
    const location = useLocation();
    let {id} = useParams();
    const [dataFetched, setDataFetched] = useState(false)
    const [post, setPost] = useState(null);
    // May be able to just remove the next 4 lines as pass them in a props object
    const [postTitle, setPostTitle] = useState("");
    const [postDate, setPostDate] = useState("");
    const [postPoints, setPostPoints] = useState(1);
    const [comments, setComments] = useState([]);

    let back = (e) => {
        e.stopPropagation();
        history.goBack();
    }

    let stopProp = (event) =>{
        event.stopPropagation();
    }

    // useEffect(() => {
    //     console.log("location");
    //     console.log(location);
    //     Axios.get(`http://localhost:8080/posts/${id}/`)
    //     .then((response) => {
    //         setPost(response.data);
    //         console.log(response.data);
    //     }).catch(e => console.log(e));
    //     Axios.get(`http://localhost:8080/posts/${id}/comments`)
    //     .then((response) => {
    //         setComments(response.data);
    //         console.log(comments);
    //     }).catch(e => console.log(e));
    //     setDataFetched(true);
    // }, [dataFetched])

    return (
        <div onClick={back} className="modal-outer">
            <div className="post-modal-inner" onClick={stopProp}>
                <div className="">
                    <Link to={`/posts/${location.state.pid}`} className="underlined">timestamp: {location.state.timestamp}</Link>
                    <Link to={`/users/${location.state.user}`} className="underlined"> {String(location.state.user)}</Link>

                </div>
                <div className="post-min-title">
                        {location.state.title}
                </div>
                <div className="post-min-content">
                    {location.state.content}
                </div>
                <div className="post-min-footer">
                        {location.state.points}
                    {/* <FaRegArrowAltCircleUp onClick={this.incrementPoints}/> */}
                </div>
                {/* <NewCommentPrompt pid={location.state.pid} user={location.state.user}/> */}
                {comments.map((comment) => {
                    return(
                        <div className="comment">
                            {comment.content}
                        </div>
                    )
                })}
                {comments.length === 0 && <div>Be the first to discuss the issue!</div>}

            </div>
        </div>
    );
}
