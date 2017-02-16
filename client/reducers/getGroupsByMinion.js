import {
    GET_GROUPS_BY_MINION_SUCCESS,
    GET_GROUPS_BY_MINION_FAIL
} from '../constants/GetGroupsByMinion';

const initialState = {
    groups: '',
    error: ''
};

export default function getGroupsByMinion(state = initialState, action) {

    switch (action.type) {
        case GET_GROUPS_BY_MINION_SUCCESS:
            return {...state, groups: action.payload, error: ''};
        case GET_GROUPS_BY_MINION_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}