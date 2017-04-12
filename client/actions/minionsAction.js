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
import {containsRole} from '../helpers';
import cookie from 'react-cookie';

export function minions() {

    return dispatch => {

        let client = Stomp.over(SockJS('/salt'));

        let token = cookie.load('accessToken'),
            user = token ? JSON.parse(atob(token)) : null;

        if (user && typeof user.roles === 'string') {
            user.roles = user.roles.replace(/[\[\]]/g, '').split(',');
        }

        client.connect({}, () => {

                if (containsRole(user.roles, ['ROLE_MAIN_MINIONS_COUNTS_STATUS', 'ROLE_ROOT'])) {

                    client.subscribe('/queue/minions/update-counts-status', obj => {
                        dispatch({
                            type: COUNTS_STATUS,
                            payload: JSON.parse(obj.body)
                        })
                    });
                }

                if (containsRole(user.roles, ['ROLE_MAIN_MINIONS_COUNTS_GROUP', 'ROLE_ROOT'])) {

                    client.subscribe('/queue/minions/update-counts-group', obj => {
                        dispatch({
                            type: COUNTS_GROUP,
                            payload: JSON.parse(obj.body)
                        })
                    });
                }

                containsRole(user.roles, ['ROLE_MAIN_MINIONS_ACCEPTED_TAB', 'ROLE_ROOT']) ?
                    client.subscribe('/queue/minions/update-accepted-minions', obj => {
                        dispatch({
                            type: ACCEPTED_MINIONS,
                            payload: JSON.parse(obj.body)
                        })
                    }) : null;

                if (containsRole(user.roles, ['ROLE_MAIN_MINIONS_DENIED_TAB', 'ROLE_ROOT'])) {

                    client.subscribe('/queue/minions/update-denied-minions', obj => {
                        dispatch({
                            type: DENIED_MINIONS,
                            payload: JSON.parse(obj.body)
                        })
                    });
                }

                if (containsRole(user.roles, ['ROLE_MAIN_MINIONS_REJECTED_TAB', 'ROLE_ROOT'])) {
                    client.subscribe('/queue/minions/update-rejected-minions', obj => {
                        dispatch({
                            type: REJECTED_MINIONS,
                            payload: JSON.parse(obj.body)
                        })
                    });
                }

                if (containsRole(user.roles, ['ROLE_MAIN_MINIONS_UNACCEPTED_TAB', 'ROLE_ROOT'])) {

                    client.subscribe('/queue/minions/update-unaccepted-minions', obj => {
                        dispatch({
                            type: UNACCEPTED_MINIONS,
                            payload: JSON.parse(obj.body)
                        })
                    });
                }

                load();
            }
        );

        let load = function () {
            client.send('/request/minions-all-data', {}, '')
        };

        return client;
    }
}