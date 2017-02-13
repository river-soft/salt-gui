import {
    DELETE_MINIONS_REQUEST,
    DELETE_MINIONS_SUCCESS,
    DELETE_MINIONS_FAIL
} from '../constants/DeleteMinions';
import $ from 'jquery';

export function deleteMinions(minions) {

    return dispatch => {

        dispatch({
            type: DELETE_MINIONS_REQUEST
        });

        $.ajax({
            url: '/delete-minions?names=' + minions,
            type: 'post',
            success: data => {
                dispatch({
                    type: DELETE_MINIONS_SUCCESS,
                    action: data.payload
                });
            },
            error: error => {
                dispatch({
                    type: DELETE_MINIONS_FAIL,
                    action: error.responseJSON,
                    error: true
                });
            }
        })
    }
}