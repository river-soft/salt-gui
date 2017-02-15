import {
    GET_GROUPED_MINIONS_SUCCESS,
    GET_GROUPED_MINIONS_FAIL
} from '../constants/GetGroupedMinions';

const initialState = {
    groupedMinions: '',
    error: ''
};

export default function groupedMinions(state = initialState, action) {

    switch (action.type) {
        case GET_GROUPED_MINIONS_SUCCESS:
            return {...state, groupedMinions: action.payload, error: ''};
        case GET_GROUPED_MINIONS_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}