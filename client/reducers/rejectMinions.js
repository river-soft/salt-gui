import {
    REJECT_MINIONS_SUCCESS,
    REJECT_MINIONS_FAIL
} from '../constants/RejectMinions';

const initialState = {
    rejected: false,
    error: ''
};

export default function rejectMinions(state = initialState, action) {

    switch (action.type) {
        case REJECT_MINIONS_SUCCESS:
            return {...state, rejected: true, error: ''};
        case REJECT_MINIONS_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}