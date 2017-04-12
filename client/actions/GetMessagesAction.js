import {
    GET_MESSAGES_REQUEST,
    GET_MESSAGES_SUCCESS,
    GET_MESSAGES_FAIL
} from '../constants/GetMessages';
import $ from 'jquery';
import LocalizedStrings from 'react-localization';
import cookie from 'react-cookie';

export function getMessages() {

    return dispatch => {

        dispatch({
            type: GET_MESSAGES_REQUEST
        });

        $.ajax({
            url: '/bundle-messages',
            type: 'get',
            success: data => {

                let strings = new LocalizedStrings(data),
                    locale = cookie.load('locale') || 'ru';

                strings.setLanguage(locale);

                dispatch({
                    type: GET_MESSAGES_SUCCESS,
                    payload: strings
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