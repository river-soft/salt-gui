import {
    ACCEPT_MINIONS_SUCCESS,
    ACCEPT_MINIONS_FAIL
} from '../constants/AcceptMinions';

const initialState = {
    minions: '',
    error: ''
};

export default (state = initialState, action) => {

    switch (action.type) {
        case ACCEPT_MINIONS_SUCCESS:
            return {...state, minions: action.payload, error: ''};
        case ACCEPT_MINIONS_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}