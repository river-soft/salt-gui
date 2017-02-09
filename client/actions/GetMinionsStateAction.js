import {
    MINIONS_STATE_REQUEST,
    MINIONS_STATE_SUCCESS,
    // MINIONS_STATE_FAIL
} from '../constants/minions/GetMinionsState';
import Stomp from 'stompjs';
import SockJS from 'sockjs-client';

export function getMinionsState() {

    return (dispatch) => {


        let client = Stomp.over(new SockJS('/salt'));

        client.connect({}, () => {

            dispatch({
                type: MINIONS_STATE_REQUEST
            });

            client.subscribe('/queue/update-minions-count', obj => {
                dispatch({
                    type: MINIONS_STATE_SUCCESS,
                    payload: obj.body
                })
            })
        });
    }
}