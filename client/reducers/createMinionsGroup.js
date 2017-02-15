import {
    CREATE_MINIONS_GROUP_SUCCESS,
    CREATE_MINIONS_GROUP_FAIL
} from '../constants/CreateMinionsGroup';

const initialState = {
    createGroup: false,
    error: ''
};

export default function createMinionsGroup(state = initialState, action) {

    switch (action.type) {
        case CREATE_MINIONS_GROUP_SUCCESS:
            return {...state, createGroup: action.payload, error: ''};
        case CREATE_MINIONS_GROUP_FAIL:
            return {...state, error: action.payload};
        default:
            return state
    }
}