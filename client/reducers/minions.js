import {
    ACCEPTED_MINIONS,
    COUNTS_GROUP,
    COUNTS_STATUS,
    DENIED_MINIONS,
    REJECTED_MINIONS,
    UNACCEPTED_MINIONS
} from '../constants/Minions';

const initialState = {
    countsStatus: '',
    countsGroup: '',
    acceptedMinions: '',
    deniedMinions: '',
    rejectedMinions: '',
    unacceptedMinions: ''
};

export default function minions(state = initialState, action) {

    switch (action.type) {
        case COUNTS_STATUS:
            return {...state, countsStatus: action.payload};
        case COUNTS_GROUP:
            return {...state, countsGroup: action.payload};
        case ACCEPTED_MINIONS:
            return {...state, acceptedMinions: action.payload};
        case DENIED_MINIONS:
            return {...state, deniedMinions: action.payload};
        case REJECTED_MINIONS:
            return {...state, rejectedMinions: action.payload};
        case UNACCEPTED_MINIONS:
            return {...state, unacceptedMinions: action.payload};
        default:
            return state
    }
}