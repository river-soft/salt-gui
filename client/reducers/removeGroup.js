import {
    GROUP_REMOVE_SUCCESS,
    GROUP_REMOVE_FAIL
} from '../constants/GroupRemove';

const initialState = {
    removed: false,
    error: ''
};

export default function removegroup(state = initialState, action) {

    switch (action.type) {
        case GROUP_REMOVE_SUCCESS:
            return {...state, removed: true, error: ''};
        case GROUP_REMOVE_FAIL:
            return {...state, error: action.payload};
        default:
            return state;
    }
}