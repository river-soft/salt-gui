import {
    AUTHORIZATION_REQUEST,
    AUTHORIZATION_SUCCESS,
    AUTHORIZATION_FAIL
} from '../constants/Authorization';
import $ from 'jquery';

export function authorization(userName, password) {

    return dispatch => {

        dispatch({
            type: AUTHORIZATION_REQUEST
        });

        $.ajax({
            url: '/login',
            type: 'post',
            data: {
                username: userName,
                password: password
            },
            headers: {
                'Authorization': 'Basic' + btoa(userName + ':' + password)
            },
            success: (res, status, response) => {
                let user = {
                    userName: response.getResponseHeader('userName'),
                    roles: response.getResponseHeader('roles')
                };

                dispatch({
                    type: AUTHORIZATION_SUCCESS,
                    payload: user
                });
            },
            error: error => {
                dispatch({
                    type: AUTHORIZATION_FAIL,
                    payload: error,
                    error: true
                });
            }
        });
    }
}