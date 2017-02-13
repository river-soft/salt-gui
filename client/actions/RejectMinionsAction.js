import {
    REJECT_MINIONS_REQUEST,
    REJECT_MINIONS_SUCCESS,
    REJECT_MINIONS_FAIL
} from '../constants/RejectMinions';
import $ from 'jquery';

export function rejectMinions(minions) {

    return dispatch => {

        dispatch({
            type: REJECT_MINIONS_REQUEST
        });

        $.ajax({
            url: '/reject-minions?names=' + minions,
            type: 'post',
            success: data => {
                dispatch({
                    type: REJECT_MINIONS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: REJECT_MINIONS_FAIL,
                    payload: error.responsJSON,
                    error: true
                });
            }
        })
    }
}