import {
    EDIT_MINION_GROUPS_REQUEST,
    EDIT_MINION_GROUPS_SUCCESS,
    EDIT_MINION_GROUPS_FAIL
} from '../constants/EditMinionGroups';
import $ from 'jquery';

export function editMinionGroups(obj) {

    return dispatch => {

        dispatch({type: EDIT_MINION_GROUPS_REQUEST});

        $.ajax({
            url: '/change-minion-groups',
            data: JSON.stringify(obj),
            contentType: 'application/json; charset=utf-8',
            type: 'put',
            success: data => {
                dispatch({
                    type: EDIT_MINION_GROUPS_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: EDIT_MINION_GROUPS_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}