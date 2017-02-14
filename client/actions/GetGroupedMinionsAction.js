import {
    GET_GROUPED_MINIONS_REQUEST,
    GET_GROUPED_MINIONS_SUCCESS,
    GET_GROUPED_MINIONS_FAIL
} from '../constants/GetGroupedMinions';
import $ from 'jquery';

export function getGroupedMinions() {

    return dispatch => {

        dispatch({type: GET_GROUPED_MINIONS_REQUEST});

        $.ajax({
            url: '/grouped-minions',
            type: 'get',
            success: data => {
                dispatch({
                    type: GET_GROUPED_MINIONS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: GET_GROUPED_MINIONS_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}