import {
    GROUP_REMOVE_REQUEST,
    GROUP_REMOVE_SUCCESS,
    GROUP_REMOVE_FAIL
} from '../constants/GroupRemove';
import $ from 'jquery';

export function removeGroup(id) {

    return dispatch => {

        dispatch({type: GROUP_REMOVE_REQUEST});

        $.ajax({
            url: '/salt-script-group?id=' + id,
            type: 'delete',
            success: data => {
                dispatch({
                    type: GROUP_REMOVE_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: GROUP_REMOVE_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}