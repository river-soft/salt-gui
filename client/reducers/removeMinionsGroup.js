import {
    REMOVE_MINIONS_GROUP_SUCCESS,
    REMOVE_MINIONS_GROUP_FAIL
} from '../constants/RemoveMinionsGroup';

const initialState = {
    remove: false,
    error: ''
};

export default function removeMinionsGroup(state = initialState, action) {

    switch (action.type) {
        case REMOVE_MINIONS_GROUP_SUCCESS:
            return {...state, remove: true, error: ''};
        case REMOVE_MINIONS_GROUP_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}