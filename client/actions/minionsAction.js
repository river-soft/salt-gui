import {
    ACCEPTED_MINIONS,
    COUNTS_GROUP,
    COUNTS_STATUS,
    DENIED_MINIONS,
    REJECTED_MINIONS,
    UNACCEPTED_MINIONS
} from '../constants/Minions';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

export function minions() {

    return dispatch => {

        let client = Stomp.over(new SockJS('/salt'));

        client.connect({}, () => {

            client.subscribe('/queue/minions/update-counts-status', obj => {
                dispatch({
                    type: COUNTS_STATUS,
                    payload: JSON.parse(obj.body)
                })
            });
            client.subscribe('/queue/minions/update-counts-group', obj => {
                dispatch({
                    type: COUNTS_GROUP,
                    payload: JSON.parse(obj.body)
                })
            });
            client.subscribe('/queue/minions/update-accepted-minions', obj => {
                dispatch({
                    type: ACCEPTED_MINIONS,
                    payload: JSON.parse(obj.body)
                })
            });
            client.subscribe('/queue/minions/update-denied-minions', obj => {
                dispatch({
                    type: DENIED_MINIONS,
                    payload: JSON.parse(obj.body)
                })
            });
            client.subscribe('/queue/minions/update-rejected-minions', obj => {
                dispatch({
                    type: REJECTED_MINIONS,
                    payload: JSON.parse(obj.body)
                })
            });
            client.subscribe('/queue/minions/update-unaccepted-minions', obj => {
                dispatch({
                    type: UNACCEPTED_MINIONS,
                    payload: JSON.parse(obj.body)
                })
            });
        });
    }
}