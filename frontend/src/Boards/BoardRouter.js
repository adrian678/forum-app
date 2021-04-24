import React from "react";
import {
  Switch,
  Route,
  useRouteMatch
} from "react-router-dom";
import RulesForm from "./RulesForm";
import BoardPage from "./BoardPage"

export default function BoardRouter(props){
    let { path} = useRouteMatch();

    return(
    <Switch>
        <Route exact path={path}>
            <BoardPage/>
        </Route>
        <Route path={`${path}/rules`}>
          <RulesForm/>
        </Route>
    </Switch>
    );
}