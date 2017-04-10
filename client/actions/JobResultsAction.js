import {
    JOB_RESULTS_COUNTS,
    JOB_RESULTS_SCRIPT
} from '../constants/JobResults';
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

            load();
        });

        let load = minutes => {

            client.send('/request/job-results-counts', {}, JSON.stringify({from: minutes}));
        };

        let getJobResults = jid => {
            let subscription = client.subscribe('/queue/job-results/update-all-results-by-job', (obj) => {
                dispatch({
                    type: JOB_RESULTS_SCRIPT,
                    payload: JSON.parse(obj.body)
                })
            });
            client.send('/request/find-all-results-by-job', {}, JSON.stringify({jid: jid}));

            return subscription
        };

        let unSubscribeJobResults = (subscription) => {
            subscription.unsubscribe();
        };

        return {
            client: client,
            getJobResults: getJobResults,
            unSubscribeJobResults: unSubscribeJobResults,
            load: load
        };
    }
}