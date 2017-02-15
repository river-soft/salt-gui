import {
    GET_GROUPS_BY_MINION_REQUEST,
    GET_GROUPS_BY_MINION_SUCCESS,
    GET_GROUPS_BY_MINION_FAIL
} from '../constants/GetGroupsByMinion';
import $ from 'jquery';

export function getGroupsByMinion(minionName) {

    return dispatch => {

        dispatch({type: GET_GROUPS_BY_MINION_REQUEST});

        $.ajax({
            url: '/groups-by-minion',
            data: {name: minionName},
            type: 'get',
            success: data => {
                dispatch({
                    type: GET_GROUPS_BY_MINION_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: GET_GROUPS_BY_MINION_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}