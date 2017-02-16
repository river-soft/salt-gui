import {
    MINION_DETAILS_REQUEST,
    MINION_DETAILS_SUCCESS,
    MINION_DETAILS_FAIL
} from '../constants/MinionDetails';
import $ from 'jquery';

export function getMinionDetails(name) {

    return dispatch => {

        dispatch({type: MINION_DETAILS_REQUEST});

        $.ajax({
            url: '/minion-details',
            data: {name: name},
            type: 'get',
            success: data => {
                dispatch({
                    type: MINION_DETAILS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: MINION_DETAILS_FAIL,
                    payload: error.responseJSON,
                    error: true
                });
            }
        })
    }
}