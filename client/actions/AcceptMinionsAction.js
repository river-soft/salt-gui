import {
    ACCEPT_MINIONS_REQUEST,
    ACCEPT_MINIONS_SUCCESS,
    ACCEPT_MINIONS_FAIL
} from '../constants/AcceptMinions';
import $ from 'jquery';

export function acceptMinions(minions, groups) {

    return dispatch => {

        dispatch({
            type: ACCEPT_MINIONS_REQUEST
        });

        $.ajax({
            url: '/accept-minions?names=' + minions + '&groups=' + groups,
            type: 'post',
            success: data => {
                dispatch({
                    type: ACCEPT_MINIONS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: ACCEPT_MINIONS_FAIL,
                    payload: error.responseJSON
                });
            }
        })
    }
}