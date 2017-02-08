import {
    SCRIPT_REMOVE_REQUEST,
    SCRIPT_REMOVE_SUCCESS,
    SCRIPT_REMOVE_FAIL
} from '../constants/ScriptRemove';
import $ from 'jquery';

export function scriptRemove(script) {

    return (dispatch) => {

        dispatch({
            type: SCRIPT_REMOVE_REQUEST
        });

        $.ajax({
            url: '/salt-script?id=' + script.id,
            type: 'delete',
            success: () => {
                dispatch({
                    type: SCRIPT_REMOVE_SUCCESS,
                    payload: script
                })
            },
            error: (data) => {
                dispatch({
                    type: SCRIPT_REMOVE_FAIL,
                    error: true,
                    payload: data.responseJSON
                })
            }
        })
    }
}