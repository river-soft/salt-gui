import {
    FILES_REQUEST,
    FILES_SUCCESS,
    FILES_FAIL
} from '../constants/FilesTree';
import $ from 'jquery'

export function filesRequest() {

    return function (dispatch) {

        dispatch({
            type: FILES_REQUEST
        });

        $.ajax({
            url: '/grouped-scripts',
            type: 'get',
            success: function(data) {

                dispatch({
                    type: FILES_SUCCESS,
                    payload: data
                });
            },
            error: function(data) {

                dispatch({
                    type: FILES_FAIL,
                    error: true,
                    payload: new Error(data)
                });
            }
        })
    }
}