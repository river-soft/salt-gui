import {
    MINIONS_STATE_REQUEST,
    MINIONS_STATE_SUCCESS,
    MINIONS_STATE_FAIL
} from '../../constants/minions/GetMinionsState';
import $ from 'jquery';

export function getMinionsState() {

    return (dispatch) => {

        dispatch({
            type: MINIONS_STATE_REQUEST
        });

        $.ajax({
            url: '/counts-minions-by-state',
            type: 'get',
            success: (data) => {
                dispatch({
                    type: MINIONS_STATE_SUCCESS,
                    payload: data
                })
            },
            error: (error) => {
                dispatch({
                    type: MINIONS_STATE_FAIL,
                    error: true,
                    payload: error.responseJSON
                })
            }
        })
    }
}