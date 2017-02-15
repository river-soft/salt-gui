import {
    CREATE_MINIONS_GROUP_REQUEST,
    CREATE_MINIONS_GROUP_SUCCESS,
    CREATE_MINIONS_GROUP_FAIL
} from '../constants/CreateMinionsGroup';
import $ from 'jquery';

export function createMinionsGroup(name) {

    return dispatch => {

        dispatch({type: CREATE_MINIONS_GROUP_REQUEST});

        $.ajax({
            url: '/minion-group',
            data: {name: name},
            type: 'post',
            success: data => {
                dispatch({
                    type: CREATE_MINIONS_GROUP_SUCCESS,
                    payload: data
                })
            },
            error: error => {
                dispatch({
                    type: CREATE_MINIONS_GROUP_FAIL,
                    payload: error.responseJSON,
                    error: true
                });
            }
        })
    }
}