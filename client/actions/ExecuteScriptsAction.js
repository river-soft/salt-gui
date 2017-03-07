import {
    EXECUTE_SCRIPTS_REQUEST,
    EXECUTE_SCRIPTS_SUCCESS,
    EXECUTE_SCRIPTS_FAIL
} from '../constants/ExecuteScripts';
import $ from 'jquery';

export function executeScripts(minions, scripts) {

    return dispatch => {
        dispatch({
            type: EXECUTE_SCRIPTS_REQUEST
        });

        $.ajax({
            url: '/execute-scripts?minions=' + minions + '&scripts=' + scripts,
            type: 'post',
            success: data => {
                dispatch({
                    payload: data,
                    type: EXECUTE_SCRIPTS_SUCCESS
                });
            },
            error: error => {
                dispatch({
                    payload: error.responseJSON,
                    error: true,
                    type: EXECUTE_SCRIPTS_FAIL
                })
            }
        })
    }

}