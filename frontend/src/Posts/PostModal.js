import React, {useState, useEffect} from "react";
import {
    Link,
    useHistory,
    useLocation,
    useParams
  } from "react-router-dom";
  import axios from "axios";
  import "../common/modals.css";
  import Comment from "../Comments/Comment";

  export default function PostModal(props) {
    let history = useHistory();
    const location = useLocation();
    // let {id} = useParams();
    // const [dataFetched, setDataFetched] = useState(false)
    // const [post, setPost] = useState(null);
    // May be able to just remove the next 4 lines as pass them in a props object
    const {postId} = useParams();
    const [post, setPost] = useState(null);
    // const [postDate, setPostDate] = useState("");
    // const [postPoints, setPostPoints] = useState(1);
    const [comments, setComments] = useState([]);

    let back = (e) => {
        e.stopPropagation();
        history.goBack();
    };

    let stopProp = (event) =>{
        event.stopPropagation();
    };

    useEffect(() => {
        console.log("location");
        console.log(location);
        axios.get(`http://localhost:8081/posts/${postId}/`)
        .then((response) => {
            setPost(response.data);
            console.log(response.data);
        }).catch(e => console.log(e));
        axios.get(`http://localhost:8081/posts/${postId}/comments`)
        .then((response) => {
            setComments(response.data._embedded.commentResponseDtoList);
            console.log("response");
            console.log(response);
            console.log("comments");
            console.log(comments);
        })
        .then(()=>console.log(comments))
        .catch(e => console.log(e));
    }, []);

    return (
        <div onClick={back} className="modal-outer">
            <div className="post-modal-inner" onClick={stopProp}>
                <div className="">
                    {/* <Link to={`/posts/${location.state.pid}`} className="underlined">timestamp: {location.state.timestamp}</Link> */}
                    <Link to={`/users/${location.state.user}`} className="underlined"> {String(location.state.user)}</Link>

                </div>
                <div className="post-min-title">
                        {location.state.title}
                </div>
                {/* <div className="post-min-content">
                    {location.state.content}
                </div> */}
                {/* <div className="post-min-footer"> */}
                        {/* {location.state.points} */}
                    {/* <FaRegArrowAltCircleUp onClick={this.incrementPoints}/> */}
                {/* </div> */}
                {/* <NewCommentPrompt pid={location.state.pid} user={location.state.user}/> */}
                {/* {comments.lenth > 0 && comments.map(comment => <p>{comment.content}</p>)} */}
                {comments.map((comment) => {
                    return(
                        <Comment author={comment.author} 
                        commentId={comment.commentId}
                        content={comment.content}
                        parentComment={comment.parentComment}
                        likedByUser={comment.likedByUser}
                        savedByUser={comment.savedByUser}
                        points={comment.points}
                        createdAt={comment.createdAt}
                        />
                    );
                })}
                {comments.length === 0 && <div>Be the first to discuss the issue!</div>}

            </div>
        </div>
    );
}
