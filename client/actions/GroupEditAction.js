import {
    GROUP_EDIT_REQUEST,
    GROUP_EDIT_SUCCESS,
    GROUP_EDIT_FAIL
} from '../constants/GroupEdit';
import $ from 'jquery';

export function editGroup(id, name) {

    return dispatch => {

        dispatch({type: GROUP_EDIT_REQUEST});

        $.ajax({
            url: '/salt-script-group?id=' + id + '&name=' + name,
            type: 'put',
            success: data => {
                dispatch({
                    type: GROUP_EDIT_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: GROUP_EDIT_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}