import React from "react";
import {  Switch,  Route,  useLocation} from "react-router-dom";
import Signup from "./Authentication/Signup";
import BoardPage from "./Boards/BoardPage";
import PostPage from "./Posts/PostPage";
import PostModal from "./Posts/PostModal";
import BoardRouter from "./Boards/BoardRouter"

export default function ModalSwitch() {
  let location = useLocation();
  let background = location.state && location.state.background;

  return(
    <div>
      <Switch location={background || location}>

        <Route path="/signup" children={<Signup/>}/>
        <Route path="/boards/:boardName" children={<BoardRouter/>}/>
        <Route path="/posts/:postId" children={<PostPage/>}/>
        {/* <Route component={Error}/> */}  {/* Write Error component */}

      </Switch>
      {background &&  <Route path="/posts/:postId" children={<PostModal/>} />}
    </div>
  )
}