import {
    EDIT_SCRIPT_REQUEST,
    EDIT_SCRIPT_SUCCESS,
    EDIT_SCRIPT_FAIL
} from '../constants/EditScript';
import $ from 'jquery';

export function editScript(script) {

    return function (dispatch) {

        dispatch({
            type: EDIT_SCRIPT_REQUEST
        });

        $.ajax({
            url: '/salt-script',
            type: 'put',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(script),
            success: (data) => {
                dispatch({
                    type: EDIT_SCRIPT_SUCCESS,
                    payload: data
                });
            },
            error: (error) => {
                dispatch({
                    type: EDIT_SCRIPT_FAIL,
                    payload: error.responseJSON
                })
            }
        })

    }
}