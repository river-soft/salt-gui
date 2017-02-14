import {
    MINION_DETAILS_SUCCESS,
    MINION_DETAILS_FAIL
} from '../constants/MinionDetails';

const initialState = {
    minionDetails: '',
    error: ''
};

export default function minionDetails(state = initialState, action) {

    switch (action.type) {
        case MINION_DETAILS_SUCCESS:
            return {...state, minionDetails: action.payload, error: ''};
        case MINION_DETAILS_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}