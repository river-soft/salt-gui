import {
    JOB_DETAILS_REQUEST,
    JOB_DETAILS_SUCCESS,
    JOB_DETAILS_FAIL
} from '../constants/JobDetails';
import $ from 'jquery';

export function jobDetails(result_id) {

    return dispatch => {

        dispatch({
            type: JOB_DETAILS_REQUEST
        });

        $.ajax({
            url: '/find-details-by-job-result',
            data: {result_id: result_id},
            type: 'get',
            success: data => {
                dispatch({
                    type: JOB_DETAILS_SUCCESS,
                    payload: data
                });
            },
            error: error => {
                dispatch({
                    type: JOB_DETAILS_FAIL,
                    payload: error.responseJSON
                })
            }
        })
    }
}