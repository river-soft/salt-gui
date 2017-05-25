import {
    EXECUTE_SCRIPTS_REQUEST,
    EXECUTE_SCRIPTS_SUCCESS,
    EXECUTE_SCRIPTS_FAIL
} from '../constants/ExecuteScripts';
import $ from 'jquery';

export function executeScripts(minions, scripts) {

    let req = {
        minions: minions,
        scripts: scripts
    };

    return dispatch => {
        dispatch({
            type: EXECUTE_SCRIPTS_REQUEST
        });

        $.ajax({
            url: '/execute-scripts',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(req),
            success: data => {
                dispatch({
                    payload: data,
                    type: EXECUTE_SCRIPTS_SUCCESS
                });
            },
            error: error => {
                debugger;
                dispatch({
                    payload: error.responseJSON,
                    error: true,
                    type: EXECUTE_SCRIPTS_FAIL
                })
            }
        })
    }

}