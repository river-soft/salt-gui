import {
    REEXECUTE_SCRIPTS_REQUEST,
    REEXECUTE_SCRIPTS_SUCCESS,
    REEXECUTE_SCRIPTS_FAIL
} from '../constants/ReExecuteScripts';
import $ from 'jquery';

export function reExecuteScripts(jobResultIds) {

    return dispatch => {

        dispatch({
            type: REEXECUTE_SCRIPTS_REQUEST
        });

        $.ajax({
            url: '/reexecute-scripts',
            type: 'post',
            contentType: 'application/json',
            data: JSON.stringify(
                jobResultIds
            ),
            success: data => {
                dispatch({
                    type: REEXECUTE_SCRIPTS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: REEXECUTE_SCRIPTS_FAIL,
                    error: true,
                    payload: error.responseJSON
                });
            }
        });
    };
}