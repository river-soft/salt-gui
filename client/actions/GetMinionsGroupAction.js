import {
    MINIONS_GROUP_REQUEST,
    MINIONS_GROUP_SUCCESS,
    MINIONS_GROUP_FAIL
} from '../constants/GetMinionsGroup';
import $ from 'jquery';

export function getMinionsGroups() {

    return dispatch => {

        dispatch({
            type: MINIONS_GROUP_REQUEST
        });

        $.ajax({
            url: '/minions-groups',
            type: 'get',
            success: data => {
                dispatch({
                    type: MINIONS_GROUP_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: MINIONS_GROUP_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}