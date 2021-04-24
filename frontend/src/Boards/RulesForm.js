import React, {useState, useEffect, useContext} from 'react';
import { useParams} from 'react-router-dom';
import axios from 'axios';
import {BiTrash} from "react-icons/bi";
import {BsFillPlusCircleFill} from "react-icons/bs";

import {AuthContext} from "../Authentication/AuthContext"

export default function RulesForm(props){

    let params = useParams();
    let auth = useContext(AuthContext);
    let [board, setBoard] = useState(null);
    let [rules, setRules] = useState([]);
    let [count, setCount] = useState(0);
   
    //want the rules list to have three operations: insert, remove, modify
    //for insert:
        //need the index of where to insert
        //copy everything to left of index, concat new element, copy and concat everything to right of index

        //remove element
        //copy list, remove from copied list

        //modify element
        //onChange function that 

        //can the 'this' be used as input to the indexOf function?
        //when making the list/map by mapping data.rules, perhaps i can pass the rule into each function rules.map(rule)=> component onChange(rule)?


    let setListItemContent = (itemNumber)=>{
        let updateList = (itemNumber, newContent) =>{
            let left = rules.slice(0, itemNumber);
        }
    };
    // let createRulesMap = (board) =>{
    //     let rules = [];
    //     board.rules.forEach((rule)=>{
    //         rules.push({key: rules.length, value: rule})
    //     });
    // };

    // let removeRule = (rule)=>{

    //     let newRules = rules.splice(rule, 1);

    // };
    let fetchRules = ()=>{
        let jwt = auth.isAuthenticated ? auth.token : null;
        let authHeaders = {'Authorization': `Bearer ${jwt}`};
        let contentType; //copy contentType header for Application/JSON
        axios.get(`http://localhost:8081/boards/${params.boardName}`, {headers: authHeaders})
        .then(response => {
            setBoard(response.data);
            setRules(response.data.rules);
            console.log(response);
        }) //pull the object from the response
        // .then(()=>console.log(board))
        .catch(err=>console.log(err)); //TODO need to display that an error occurred fetching board/ board rules

    };

    let addRule = function(key){
        function innerFunction(e){
            let newRules = [...rules.slice(0, key+1), "", ...rules.slice(key+1)];
            setRules(newRules);
        }
        return innerFunction;
    }

    let removeRule = function(key){
        function innerFunction(e){
            let newRules = rules.slice(0, key).concat(rules.slice(key+1));
            setRules(newRules);
        }
        return innerFunction;
    }

    let modifyRule = function(key){
        function innerFunction(e){
            let newRules = [...rules.slice(0, key), e.target.value, ...rules.slice(key+1)];
            setRules(newRules);
        }
        return innerFunction;
    }

    let sendReplaceRequest = ()=>{
        let jwt = auth.isAuthenticated ? auth.token : null;
        let authHeaders = {'Authorization': `Bearer ${jwt}`};
        let contentType; //copy contentType header for Application/JSON
        axios.put(`http://localhost:8081/boards/${params.boardName}/rules`, {headers: authHeaders})
        .then(response => {
            console.log(response);
        })
        .catch(err=>console.log(err)); //TODO need to display that an error occurred fetching board/ board rules
    }

    let replaceRulesButton = (
        // TODO create a yellow or hazard button for replacing rules
        <button >
            Modify Rules
        </button>
    )

    useEffect(fetchRules, []);

    return(
        <div>
            Board Rules page
            {/* TODO put the board rules into another list or map that will be mutable */}
            <BsFillPlusCircleFill onClick={addRule(0)}/>
            {board && rules.map(rule =>{
                    return(
                        // If I want a drag-and-drop functionality, then I will need a map of keys & listOrder
                        // TODO will need widgets/elements for deleting items
                        
                        <div key={count++} rule={rule} onChange={modifyRule(count)}>
                            <p>{rule}</p>
                            <textarea value={rule}/>
                            {/* TODO add new link in backend response for posting to replace resource */}
                            <BsFillPlusCircleFill onClick={addRule(count)}/>
                            <BiTrash onClick={removeRule(count)}/>
                        </div>
                    )
                    // If link exists, display a button to PUT to rules
                    
            })
            }
            {/* {board && board.data._links["replace rules"]} */}
        </div>
    );
}