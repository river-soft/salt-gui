import {JOB_RESULTS_COUNTS} from '../constants/JobResults';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

export function jobResults() {

    return dispatch => {

        let client = Stomp.over(SockJS('/salt'));

        client.connect({}, () => {

            client.subscribe('/queue/job-results/update-counts-job-results', obj => {
                dispatch({
                    type: JOB_RESULTS_COUNTS,
                    payload: JSON.parse(obj.body)
                })
            });
        });

        return {
            client: client,
            dispatch: dispatch
        };
    }
}