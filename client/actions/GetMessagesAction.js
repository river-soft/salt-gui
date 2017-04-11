import {
    GET_MESSAGES_REQUEST,
    GET_MESSAGES_SUCCESS,
    GET_MESSAGES_FAIL
} from '../constants/GetMessages';
import $ from 'jquery';
import LocalizedStrings from 'react-localization';

export function getMessages() {

    return dispatch => {

        dispatch({
            type: GET_MESSAGES_REQUEST
        });

        $.ajax({
            url: '/bundle-messages',
            type: 'get',
            success: data => {
                dispatch({
                    type: GET_MESSAGES_SUCCESS,
                    payload: new LocalizedStrings(data)
                })
            },
            error: error => {
                dispatch({
                    type: GET_MESSAGES_FAIL,
                    payload: error.responseJSON,
                    error: true
                })
            }
        })
    }
}