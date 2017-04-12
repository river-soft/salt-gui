import {
    JOB_RESULTS_COUNTS,
    JOB_RESULTS_SCRIPT
} from '../constants/JobResults';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';
import {containsRole} from '../helpers'
import cookie from 'react-cookie';

export function jobResults() {

    return dispatch => {

        let client = Stomp.over(SockJS('/salt')),
            token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        client.connect({}, () => {

            if (containsRole(user.roles, ['ROLE_SHOW_JOB_RESULTS_COUNTERS', 'ROLE_ROOT'])) {

                client.subscribe('/queue/job-results/update-counts-job-results', obj => {
                    dispatch({
                        type: JOB_RESULTS_COUNTS,
                        payload: JSON.parse(obj.body)
                    })
                });

                load();
            }
        });

        let load = minutes => {

            client.send('/request/job-results-counts', {}, JSON.stringify({from: minutes}));
        };

        let getJobResults = jid => {

            if (containsRole(user.roles, ['ROLE_SHOW_RESULTS_BY_JOB', 'ROLE_ROOT'])) {

                let subscription = client.subscribe('/queue/job-results/update-all-results-by-job', (obj) => {
                    dispatch({
                        type: JOB_RESULTS_SCRIPT,
                        payload: JSON.parse(obj.body)
                    })
                });
                client.send('/request/find-all-results-by-job', {}, JSON.stringify({jid: jid}));

                return subscription
            }
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