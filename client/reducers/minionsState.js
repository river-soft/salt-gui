import {
    MINIONS_STATE_SUCCESS,
    MINIONS_STATE_FAIL
} from '../constants/minions/GetMinionsState';

const initialState = {
    states: '',
    error: ''
};

export default function getMinionsState(state = initialState, action) {

    switch (action.type) {
        case MINIONS_STATE_SUCCESS:
            return {...state, states: action.payload, error: ''};
        case MINIONS_STATE_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}