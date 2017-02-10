import {
    MINIONS_GROUP_SUCCESS,
    MINIONS_GROUP_FAIL
} from '../constants/GetMinionsGroup';

const initialState = {
    groups: '',
    error: ''
};

export default function minionsGroups(state = initialState, action) {

    switch (action.type) {
        case MINIONS_GROUP_SUCCESS:
            return {...state, groups: action.payload, error: ''};
        case MINIONS_GROUP_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}