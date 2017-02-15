import {
    REMOVE_MINIONS_GROUP_REQUEST,
    REMOVE_MINIONS_GROUP_SUCCESS,
    REMOVE_MINIONS_GROUP_FAIL
} from '../constants/RemoveMinionsGroup';
import $ from 'jquery';

export function removeMinionsGroup(groupId) {

    return dispatch => {

        dispatch({type: REMOVE_MINIONS_GROUP_REQUEST});

        $.ajax({
            url: '/minion-group?id=' + groupId,
            type: 'delete',
            success: data => {
                dispatch({
                    type: REMOVE_MINIONS_GROUP_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: REMOVE_MINIONS_GROUP_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}