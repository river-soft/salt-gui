import {
    GROUP_CREATE_REQUEST,
    GROUP_CREATE_SUCCESS,
    GROUP_CREATE_FAIL
} from '../constants/GroupCreate';
import $ from 'jquery';

export function createGroup(model) {

    return (dispatch) => {

        dispatch({
            type: GROUP_CREATE_REQUEST
        });

        $.ajax({
            url: '/salt-script-group',
            type: 'post',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(model),
            success: function (data) {

                dispatch({
                    type: GROUP_CREATE_SUCCESS,
                    payload: data
                });
            },
            error: function (data) {

                dispatch({
                    type: GROUP_CREATE_FAIL,
                    error: new Error(data)
                });
            }

        })
    }
}