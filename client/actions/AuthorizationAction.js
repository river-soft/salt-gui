import {
    AUTHORIZATION_REQUEST,
    // AUTHORIZATION_SUCCESS,
    // AUTHORIZATION_FAIL
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
            data: JSON.stringify({
                username: userName,
                password: password
            }),
            contentType: 'application/json',
            // success: data => {
            //     debugger;
            //     dispatch({
            //         type: AUTHORIZATION_SUCCESS,
            //         payload: data
            //     });
            // },
            // error: error => {
            //     dispatch({
            //         type: AUTHORIZATION_FAIL,
            //         payload: error,
            //         error: true
            //     });
            // }
        }).done(response => {
            debugger;
            console.log(response.json());
        })
    }
}