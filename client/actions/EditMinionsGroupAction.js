import {
    EDIT_MINIONS_GROUP_REQUEST,
    EDIT_MINIONS_GROUP_SUCCESS,
    EDIT_MINIONS_GROUP_FAIL
} from '../constants/EditMinionsGroup';
import $ from 'jquery';

export function editMinionsGroup(id, name) {

    return dispatch => {

        dispatch({type: EDIT_MINIONS_GROUP_REQUEST});

        $.ajax({
            url: '/minion-group',
            data: {
                id: id,
                name: name
            },
            type: 'put',
            success: data => {
                dispatch({
                    type: EDIT_MINIONS_GROUP_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: EDIT_MINIONS_GROUP_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}